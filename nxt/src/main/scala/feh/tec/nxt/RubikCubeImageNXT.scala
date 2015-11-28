package feh.tec.nxt

import feh.tec.nxt.LegoRobotRubik.{Motors, remotely}
import feh.tec.rubik.RubikCube._
import feh.tec.rubik.RubikCubeImage
import feh.tec.rubik.RubikCubeImage.{Side, SidesMap, ColorMap}
import feh.util._
import lejos.nxt.LightSensor


object RubikCubeImageNXT{
  def readCubes[T, C](gatherColor: => T)
                     (implicit motors: Motors,
                      ls: LightSensor,
                      rd: RobotDescriptor,
                      cMap: ColorMap[T, C],
                      sMap: SidesMap,
                      sName: WithSideName[C] ): Map[CubeId, CubeWithOrientation[C]] =
    RubikCubeImage.readCubes(readImage(gatherColor))

  def readImage[T, C](gatherColor: => T)
                     (implicit motors: Motors,
                               ls: LightSensor,
                               rd: RobotDescriptor,
                               cmap: ColorMap[T, C] ): RubikCubeImage[C] = readImage(readSomeImage(gatherColor))

  def readImage[T, C](img: RubikCubeImage[T])
                     (implicit motors: Motors,
                      ls: LightSensor,
                      rd: RobotDescriptor,
                      cmap: ColorMap[T, C] ): RubikCubeImage[C] = img.map(cmap.colorFor)

  def readSomeImage[T](gatherColor: => T)
                      (implicit motors: Motors,
                                ls: LightSensor,
                                rd: RobotDescriptor): RubikCubeImage[T] =
  {
    import rd._

    def flip(n: Int) = {
//      ls.setFloodlight(false)
      remotely.flipCube(n)
//      ls.setFloodlight(true)
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
