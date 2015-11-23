package feh.tec.nxt

import feh.tec.nxt.LegoRobotRubik.{remotely, LightSensorPosition, Motors}
import feh.tec.rubik.RubikCube._
import feh.util._
import lejos.nxt.LightSensor

case class RubikCubeImage[T](sides: Seq[RubikCubeImage.Side[T]]){
  def map[R](f: T => R): RubikCubeImage[R] = copy(sides.map(_.map(f)))
}

object RubikCubeImage{
  case class Side[T](colors: Map[(Int, Int), T]){
    def map[R](f: T => R): Side[R] = copy(colors.mapValues(f))
  }

  case class ColorMap[T, C](colorFor: PartialFunction[T, C])
  case class ColorMapCreationError(reason: String) extends RuntimeException("Failed to create color map: " + reason)
  
  case class ReadSide(name: SideName,
                      flipX: Boolean = false,
                      flipY: Boolean = false,
                      transposed: Boolean = false
                       )
  case class SidesMap(readOrder: Seq[ReadSide])


  case class CubeSide[C](cubeId: CubeId, orientation: SideName, color: C){
    def id = cubeId -> orientation
  }

  def readCubes[T, C](gatherColor: => T)
                     (implicit motors: Motors,
                               ls: LightSensor,
                               rd: RobotDescriptor,
                               cMap: ColorMap[T, C],
                               sMap: SidesMap,
                               sName: WithSideName[C] ): Map[CubeId, CubeWithOrientation[C]] =
  {
    val RubikCubeImage(sides) = readImage(gatherColor)

    def flip(x: Int) = x match{
      case 2 => 0
      case 1 => 1
      case 0 => 2
    }

    def mkSide(side: SideName, colors: Map[(Int, Int), C]) = {
      val idsMap = sideCubes(side)
      colors.map{ case (pos, c) => CubeSide(idsMap(pos), side, c) }
    }

    val cubesSides = sMap.readOrder zip sides flatMap {

      case (ReadSide(side, false, false, false), Side(colors)) =>
        mkSide(side, colors)

      case (ReadSide(side, flipX, flipY, transpose), Side(colors)) =>
        val idsMap = sideCubes(side)
        val flipped =
          if(flipX || flipY) colors.mapKeys{
              case (x, y) => (if (flipX) flip(x) else x, if (flipY) flip(y) else y )
            }
          else colors

        val transposed =
          if(transpose) flipped.mapKeys(_.swap)
          else flipped

        mkSide(side, transposed)
    }

    cubesSides.groupBy(_.cubeId).mapValues{
      case Seq(CubeSide(_, o1, c1), CubeSide(_, o2, c2), CubeSide(_, o3, c3))=>
        Corner(c1, c2, c3) -> CubeOrientation(o1, o2, o3)
      case Seq(CubeSide(_, o1, c1), CubeSide(_, o2, c2)) =>
        Middle(c1, c2) -> CubeOrientation(o1, o2, null)
      case Seq(CubeSide(_, o, c)) =>
        Center(c) -> CubeOrientation(o, null, null)
    }
  }


  def readImage[T, C](gatherColor: => T)
                     (implicit motors: Motors,
                               ls: LightSensor,
                               rd: RobotDescriptor,
                               cmap: ColorMap[T, C] ): RubikCubeImage[C] =
    readSomeImage(gatherColor).map(cmap.colorFor)

  def readSomeImage[T](gatherColor: => T)
                      (implicit motors: Motors,
                                ls: LightSensor,
                                rd: RobotDescriptor): RubikCubeImage[T] =
  {
    import rd._

    def flip(n: Int) = {
      ls.setFloodlight(false)
      remotely.flipCube(n)
      ls.setFloodlight(true)
    }

    // Up
    val up = gatherSideColors(gatherColor)
    // Back, Down, Front
    val sides1 = for(i <- 2 to 4) yield {
      flip(1)
      gatherSideColors(gatherColor)
    }
    flip(1)
    remotely.rotate.clockwise90()
    flip(1)
    val left = gatherSideColors(gatherColor)
    flip(2)
    val right = gatherSideColors(gatherColor)
    flip(1)
    remotely.rotate.counterclockwise90()

    motors.lsm.get.rotateTo(0)

    RubikCubeImage(up +: (sides1 ++ Seq(left, right)))
  }


  def gatherSideColors[T](gatherColor: => T)
                         (implicit motors: Motors, rcd: ReadColorsSequenceDescriptor): Side[T] =
    {
      val center = {motors.lsm.get.rotateTo(rcd.centerLightAbsAngle); Thread.sleep(100); gatherColor}

      def rotate(ar: Int, al: Int) = {
        motors.lsm.get.rotateTo(al, true)
        motors.crm.get.rotate(ar, false)
      }
      motors.crm.get.rotate(rcd.rotAngleDelta0)
      val rest = Y[(Int, List[(Int, Int)]), Map[(Int, Int), T]](
        rec => {
        case (_, Nil) => Map.empty
        case (i, p :: ps) =>
          val g = gatherColor
          (rotate _).tupled( if (i % 2 == 1) rcd.oddAngles else rcd.evenAngles )
          rec(i+1, ps) + (p -> g)
        }
      )(1 -> rcd.indices.tail.toList)

      motors.crm.get.rotate(rcd.finalRotation, false)

      Side(rest + (rcd.indices.head -> center))
    }

}
