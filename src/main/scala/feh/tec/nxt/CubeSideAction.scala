package feh.tec.nxt

import feh.tec.nxt.LegoRobotRubik.{LightSensorPosition, CubeRotationMotor, LightSensorMotor}


object CubeSideAction{

  def foreachCube[R](f: ((Int, Int)) => R)
                    (implicit lsm: LightSensorMotor, crm: CubeRotationMotor): Seq[R] =
    motorPositions.map{
      case (p, LightSensorPosition(sa, ra)) =>
        lsm.get.rotateTo(sa, true)
        crm.get.rotateTo(ra, false)
        f(p)
    }


  private def mpRot0 = 50
  private def mpRotC  = 165
  private def mpRotM  = 145


  lazy val motorPositionsMap = motorPositions.toMap
  lazy val motorPositions = Seq(
    (1, 1) -> LightSensorPosition(40, 0),
    (1, 0) -> LightSensorPosition(23, mpRot0),      // 50
    (0, 0) -> LightSensorPosition(17, mpRot0+mpRotC), // 210
    (0, 1) -> LightSensorPosition(23, mpRot0+mpRotC+mpRotM), // 350
    (0, 2) -> LightSensorPosition(17, mpRot0+mpRotC*2+mpRotM), // 510
    (1, 2) -> LightSensorPosition(23, mpRot0+mpRotC*2+mpRotM*2),
    (2, 2) -> LightSensorPosition(17, mpRot0+mpRotC*3+mpRotM*2),
    (2, 1) -> LightSensorPosition(23, mpRot0+mpRotC*3+mpRotM*3),
    (2, 0) -> LightSensorPosition(17, mpRot0+mpRotC*4+mpRotM*3)
  )

}