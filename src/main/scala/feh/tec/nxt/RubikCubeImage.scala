package feh.tec.nxt

import feh.tec.nxt.LegoRobotRubik.{remotely, LightSensorPosition, Motors}
import feh.util._
import lejos.nxt.LightSensor

case class RubikCubeImage(sides: Seq[RubikCubeImage.Side])

object RubikCubeImage{
  case class Side(colors: Map[(Int, Int), Any]) // java.awt.Color


  def readImage(gatherColor: => Any) // java.awt.Color
               (implicit motors: Motors,
                         ls: LightSensor,
                         rcd: ReadColorsSequenceDescriptor,
                         fd: FlipDescriptor,
                         rbd: RotateBottomDescriptor): RubikCubeImage =
  {
    def flip(n: Int) = {
      ls.setFloodlight(false)
//      Thread.sleep(100)
      remotely.flipCube(n)
//      motors.lsm.get.rotateTo(0)
//      Thread.sleep(100)
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


  def gatherSideColors(gatherColor: => Any) // java.awt.Color
                      (implicit motors: Motors, rcd: ReadColorsSequenceDescriptor) = //Side(Map())
    {
      val center = {motors.lsm.get.rotateTo(rcd.centerLightAbsAngle); gatherColor}
      Thread.sleep(100)

      def rotate(ar: Int, al: Int) = {
        motors.lsm.get.rotateTo(al, true)
        motors.crm.get.rotate(ar, false)
      }
      motors.crm.get.rotate(rcd.rotAngleDelta0)
      val rest = Y[(Int, List[(Int, Int)]), Map[(Int, Int), Any]]( // java.awt.Color
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
