package feh.tec.nxt

import feh.tec.nxt.LegoRobotRubik._
import lejos.nxt._

object RobotTest extends App{

  lazy val ms = Motor.A
  lazy val mr = Motor.B
  lazy val mf = Motor.C

  implicit lazy val ls = new LightSensor(SensorPort.S2)

  implicit lazy val lsm = LightSensorMotor(ms)
  implicit lazy val crm = CubeRotationMotor(mr)
  implicit lazy val cfm = CubeFlipMotor(mf)

  implicit lazy val motors = Motors(lsm, crm, cfm)

  implicit lazy val rotateBottomDescriptor = RotateBottomDescriptor(316)
  implicit lazy val flipDescriptor = FlipDescriptor(-70, Seq(30, 30, -30, -30)) // Seq(30, 30, -30, -30)
  implicit lazy val holdDescriptor = HoldDescriptor(20)

  implicit lazy val readColorsSequenceDescriptor = ReadColorsSequenceDescriptor(
    indices = Seq(1 -> 1, 1 -> 0, 0 -> 0, 0 -> 1, 0 -> 2, 1 -> 2, 2 -> 2, 2 -> 1, 2 -> 0),
    centerLightAbsAngle = 44,
    rotAngleDelta0 = 30,      // 50
    rotAngleDeltaOdd = 158,  // 148 | 145 | 158
    rotAngleDeltaEven = 158, // 168 | 165 | 158
    lightAngleAbsOdd = 19,   // 17  | 19 | 20
    lightAngleAbsEven = 27,  // 23       | 26
    finalRotation = -34        // -55
  )



  def moveSensorTo(p: (Int, Int)) = CubeSideAction.motorPositionsMap(p) match {
    case LightSensorPosition(sa, ra) =>
      ms.rotateTo(sa)
      mr.rotateTo(ra)
  }

  def gatherColor() = {
    ls.setFloodlight(false)
    Thread.sleep(100)
    val cLOff = ls.readNormalizedValue
    ls.setFloodlight(true)
    Thread.sleep(100)
    val cLOn = ls.readNormalizedValue
    (cLOff, cLOn)
  }

//  def gatherColors() = {
//    ls.setFloodlight(false)
//    val res = CubeSideAction.foreachCube{
//      p =>
//        Thread.sleep(300)
//        val cLOff = ls.readNormalizedValue
//        ls.setFloodlight(true)
//        Thread.sleep(300)
//        val cLOn = ls.readNormalizedValue
//        ls.setFloodlight(false)
//        Thread.sleep(300)
//        p -> (cLOff, cLOn)
//    }
//
//    ms.rotateTo(0)
//    mr.rotateTo(1255)
//
//    res
//  }

}

/*

Red:    256 255 265 258 258 262 | 255-262
Blue:   151 150 151 153 154 145 | 145-154
Green:  158 157 164 178 160     | 157-178
Yellow: 273 275 278 262 270     | 262-278
Orange: 251 251                 | 251-251
White:  264 254 265             | 254-265

 */

/*

Red:
Blue:
Green:
Yellow:
Orange:
White:


 */