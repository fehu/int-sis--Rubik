package feh.tec.nxt

import lejos.nxt.remote.RemoteMotor


object LegoRobotRubik {

  sealed trait RobotRemoteMotor{ def get: RemoteMotor }
  
  case class LightSensorMotor(get: RemoteMotor)  extends RobotRemoteMotor
  case class CubeRotationMotor(get: RemoteMotor) extends RobotRemoteMotor
  case class CubeFlipMotor(get: RemoteMotor)     extends RobotRemoteMotor


  case class Motors(lsm: LightSensorMotor, crm: CubeRotationMotor, cfm: CubeFlipMotor)

  case class LightSensorPosition(lightAngle: Int, cubeRotAngle: Int)


  object remotely{

    def setSensorAt(p: LightSensorPosition)(implicit motors: Motors): Unit = {
      motors.lsm.get.rotateTo(p.lightAngle, true)
      motors.crm.get.rotateTo(p.cubeRotAngle, false)
    }

    def flipCube(n: Int)(implicit motors: Motors, d: FlipDescriptor): Unit = {
      motors.lsm.get.rotateTo(d.safeLightSensorAngle)
      for(_ <- 1 to n) {
//        Thread.sleep(200)
        d.angles.foreach{ i => motors.cfm.get.rotate(i, false); /*Thread.sleep(100)*/ }
      }
    }

    object rotate{
      def clockwise90()(implicit motors: Motors, d: RotateBottomDescriptor): Unit  = motors.crm.get.rotate(d.angle)
      def counterclockwise90()(implicit motors: Motors, d: RotateBottomDescriptor): Unit  = motors.crm.get.rotate(-d.angle)
      def halfTurn()(implicit motors: Motors, d: RotateBottomDescriptor): Unit  = motors.crm.get.rotate(2*d.angle)
    }

    def holdCube()(implicit motors: Motors, d: HoldDescriptor) = motors.cfm.get.rotate(d.holdAngle)
    def releaseCube()(implicit motors: Motors, d: HoldDescriptor) = motors.cfm.get.rotate(-d.holdAngle)

    def holdingCube(f: => Unit)(implicit motors: Motors, d: HoldDescriptor): Unit = {
      holdCube()
      f
      releaseCube()
    }
  }
}
