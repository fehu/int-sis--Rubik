package feh.tec.nxt.run

import RobotConfig.Default._
import feh.tec.nxt.RubikCubeImage
import feh.tec.rubik.RubikCubeInstance
import feh.tec.rubik.RubikSubCubesDefault.WithSideNameIdentity

object BuildCubeTest {
  var blink_? = false

  implicit lazy val cMap = ColorMaps.SideNames.customGray
  implicit lazy val sMap = SidesMaps.default

  if (!blink_?) ls.setFloodlight(true)
  val initialCubes = RubikCubeImage.readCubes(gatherColor(blink_?))

  val initialRubik = RubikCubeInstance(initialCubes, None, "initial")



//  val initialCubes = RubikCube.sideCubes.map{
//    case (side, cIds) =>
//  }

}
