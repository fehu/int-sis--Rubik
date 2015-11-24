package feh.tec.nxt.run

import java.text.DateFormat
import java.util.Date

import feh.tec.nxt.RubikCubeImage
import feh.tec.nxt.RubikCubeImage.SidesMap
import feh.tec.nxt.run.RobotConfig.Default._
import feh.tec.rubik.RubikCubeInstance
import feh.tec.rubik.ogl.run.RubikCubeTestGLDefault
import feh.util.file._

object BuildCubeImage extends RubikCubeTestGLDefault{
  var blink_? = true

  if (args.isEmpty) {
    println("no SideNames file provided")
    sys.exit(255)
  }

  implicit val cMap = ColorMaps.SideNames.fromFile(args.head)
  implicit lazy val sMap = SidesMaps.default

  if (!blink_?) ls.setFloodlight(true)

  val rawImage = RubikCubeImage.readSomeImage(gatherColor(blink_?))
  val cubesImg = RubikCubeImage.readImage(rawImage)

  val timeStr = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(new Date())


  def imgToString[T](img: RubikCubeImage[T]) = img.sides.zip(implicitly[SidesMap].readOrder).flatMap{
    case (RubikCubeImage.Side(colors), readSide) =>
      val cStrs = colors.toSeq.map{ case ((x, y), color) => Seq(x, y, color).mkString(", ") }
      ("-- " + readSide.name) +: cStrs
  }.mkString("\n")


  val rawImgStr = imgToString(rawImage)
  val cubesImgStr = imgToString(cubesImg)

  val fStr = timeStr + "\n\n" + rawImgStr + "\n\n" + cubesImgStr

  new File("cubes-image.log") withOutputStream File.write.utf8(fStr)

  val initialCubes = RubikCubeImage.readCubes(cubesImg)

  initialCubes.foreach(println)

  val rubik = RubikCubeInstance(initialCubes, None, "initial")

  run()

}
