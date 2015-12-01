package feh.tec.nxt.run

import java.text.DateFormat
import java.util.Date

import feh.tec.nxt.{NameUtils, RubikCubeImageNXT}
import feh.tec.nxt.run.RobotConfig.Default._
import feh.tec.rubik.RubikCube.SideName
import feh.tec.rubik.RubikCubeImage.SidesMap
import feh.tec.rubik.RubikCubeInstance.InitialDescription
import feh.tec.rubik.{RubikCubeImage, RubikCubeInstance}
import feh.tec.rubik.ogl.run.RubikCubeTestGLDefault
import feh.util.Path
import feh.util.file._

object BuildCubeImage extends RubikCubeTestGLDefault[RubikCubeInstance[SideName]]{
  var blink_? = false

  if (args.isEmpty) {
    println("no Cube Color Map file provided")
    sys.exit(255)
  }

  implicit val cMap = ColorMaps.SideNames.fromFile(args.head)
  implicit lazy val sMap = SidesMaps.default

  if (!blink_?) ls.setFloodlight(true)

  val rawImage = RubikCubeImageNXT.readSomeImage(gatherColor(blink_?))
  val cubesImg = RubikCubeImageNXT.readImage(rawImage)

  val timeStr = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(new Date())

  val rawImgStr = RubikCubeImage.toString(rawImage)
  val cubesImgStr = RubikCubeImage.toString(cubesImg)

  val fStr = timeStr + "\n\n:RAW\n" + rawImgStr + "\n:COLORS\n" + cubesImgStr

  val fPath = Path.relative("../logs") / NameUtils.formatDateFile("rubik-image-", "rci") // todo: path should be configurable

  fPath.file withOutputStream File.write.utf8(fStr)

  val initialCubes = RubikCubeImage.readCubes(cubesImg)

  initialCubes.foreach(println)

  val rubik = RubikCubeInstance(initialCubes, None, InitialDescription("initial"))

  run()

}
