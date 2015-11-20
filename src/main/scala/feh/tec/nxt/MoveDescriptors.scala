package feh.tec.nxt

case class ReadColorsSequenceDescriptor(indices             : Seq[(Int, Int)],
                                        centerLightAbsAngle : Int,
                                        rotAngleDelta0      : Int,
                                        rotAngleDeltaOdd    : Int,
                                        rotAngleDeltaEven   : Int,
                                        lightAngleAbsOdd    : Int,
                                        lightAngleAbsEven   : Int,
                                        finalRotation       : Int
                                         )
{
  def oddAngles = rotAngleDeltaOdd -> lightAngleAbsOdd
  def evenAngles = rotAngleDeltaEven -> lightAngleAbsEven
}


case class RotateBottomDescriptor(angle: Int)

case class FlipDescriptor(safeLightSensorAngle: Int, angles: Seq[Int])

case class HoldDescriptor(holdAngle: Int)