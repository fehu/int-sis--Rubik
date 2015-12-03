package feh.tec.nxt.run

import java.util.Date

import feh.tec.nxt.RubikCubeImageNXT
import feh.tec.nxt.run.RobotConfig.Default._
import feh.tec.rubik.RubikCube.SideName
import feh.tec.rubik.RubikCubeImageIO.{ColorsImage, ImageType, RawImage}
import feh.tec.rubik.RubikCube.InitialDescription
import feh.tec.rubik.ogl.run.RubikCubeTestGLDefault
import feh.tec.rubik.{RubikCubeImage, RubikCubeImageIO, RubikCubeInstance}
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

  val time = new Date()

  val imgs = Map[RubikCubeImage[_], ImageType](
    rawImage -> RawImage(implicitly),
    cubesImg -> ColorsImage
  )

  RubikCubeImageIO.write(imgs, t => (Path.relative("../logs") / ("rubik-image-" + t + ".rci")).file, time)

  val initialCubes = RubikCubeImage.readCubes(cubesImg)

  initialCubes.foreach(println)

  val rubik = RubikCubeInstance(initialCubes, None, InitialDescription("initial"))

  run()

}
