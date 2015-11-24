package feh.tec.nxt.run

import feh.tec.nxt.LegoRobotRubik.{Motors, CubeFlipMotor, CubeRotationMotor, LightSensorMotor}
import feh.tec.nxt._
import lejos.nxt.{SensorPort, LightSensor, Motor}

object RobotConfig {


  object Default{


    implicit lazy val ls = new LightSensor(SensorPort.S2)

    implicit lazy val lsm = LightSensorMotor(Motor.A)
    implicit lazy val crm = CubeRotationMotor(Motor.B)
    implicit lazy val cfm = CubeFlipMotor(Motor.C)

    implicit lazy val motors = Motors(lsm, crm, cfm)

    implicit lazy val rotateBottomDescriptor = RotateBottomDescriptor(316)
    implicit lazy val flipDescriptor = FlipDescriptor(-70, Seq(30, 30, -30, -30)) // Seq(30, 30, -30, -30)
    implicit lazy val holdDescriptor = HoldDescriptor(20)

    implicit lazy val readColorsSequenceDescriptor = ReadColorsSequenceDescriptor(
      indices = Seq(1 -> 1, 1 -> 0, 0 -> 0, 0 -> 1, 0 -> 2, 1 -> 2, 2 -> 2, 2 -> 1, 2 -> 0),
      centerLightAbsAngle = 43, // 48
      rotAngleDelta0 = 40,      // 50
      rotAngleDeltaOdd = 158,  // 148 | 145 | 158
      rotAngleDeltaEven = 158, // 168 | 165 | 158
      lightAngleAbsOdd = 16,   // 17  | 19 | 20 | 25 | 20
      lightAngleAbsEven = 25,  // 23       | 26 | 33 | 28
      finalRotation = -79      // -55
    )

    implicit lazy val robotDescriptor = RobotDescriptor()

    def gatherColor(blink_? : Boolean) = {
      if(blink_?) ls.setFloodlight(true)
      Thread.sleep(300)
      val cN = ls.readNormalizedValue
      if(blink_?) ls.setFloodlight(false)
      cN
    }
  }

}
