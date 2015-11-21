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
    centerLightAbsAngle = 43, // 48
    rotAngleDelta0 = 75,      // 50
    rotAngleDeltaOdd = 158,  // 148 | 145 | 158
    rotAngleDeltaEven = 158, // 168 | 165 | 158
    lightAngleAbsOdd = 20,   // 17  | 19 | 20 | 25 | 20
    lightAngleAbsEven = 27,  // 23       | 26 | 33 | 28
    finalRotation = -79      // -55
  )


  def mean(s: Iterable[Int]) = s.sum.toFloat / s.size
  def stdev(s: Iterable[Int], m: Float) = s.map(m - _).map(x => x*x).sum / (s.size - 1)
  def meanAndVar(s: Iterable[Int]) = {val m = mean(s); val std = stdev(s, m); m -> math.sqrt(std) }



  def moveSensorTo(p: (Int, Int)) = CubeSideAction.motorPositionsMap(p) match {
    case LightSensorPosition(sa, ra) =>
      ms.rotateTo(sa)
      mr.rotateTo(ra)
  }

  def gatherColor() = {
    ls.setFloodlight(true)
    Thread.sleep(300)
    val c  = ls.readValue
    val cN = ls.readNormalizedValue
    ls.setFloodlight(false)
//    (cLOff, cLOn)
    c -> cN
  }

  while (true){
    val c = gatherColor()
    println(c) // + " - " + (c._2 - c._1)
    Thread.sleep(500)
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

Vector((451,460),              (394,403),              (571,592),              (440,553),              (479,503),              (437,451))
Vector((292,388,(352.4,31.5)), (287,339,(305.1,17.5)), (478,545,(506.2,24.0)), (462,523,(490.3,22.0)), (328,436,(379.0,32.7)), (252,361,(316.7,32.7)))
Vector((298,376,(350.0,26.3)), (290,340,(306.0,17.1)), (472,540,(504.2,25.3)), (463,527,(496.3,21.4)), (1,433,(373.1,31.4)),   (234,379,(308.7,49.4)))
Vector((281,382,(347.7,34.3)), (283,340,(307.8,17.6)), (473,546,(504.8,26.9)), (475,524,(494.7,19.2)), (381,430,(396.8,17.8)), (264,353,(317.2,30.4)))

Vector((432,440,(435.2,2.7)),  (373,406,(382.0,11.3)), (558,567,(563.4,2.7)),  (374,391,(379.8,5.8)),  (498,508,(503.1,3.0)),  (403,427,(412.9,7.7)))
Vector((422,446,(428.8,8.0)),  (368,379,(371.2,4.0)),  (523,541,(529.6,6.1)),  (355,378,(365.1,7.7)),  (465,483,(472.4,4.8)),  (369,387,(379.1,5.2)))
Vector((422,433,(427.3,3.2)),  (370,382,(373.0,4.4)),  (524,544,(534.5,7.0)),  (355,372,(363.0,6.1)),  (469,481,(474.4,3.4)),  (370,399,(384.3,8.9)))

 */