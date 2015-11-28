package feh.tec.nxt.run

import feh.tec.rubik.RubikCube.SideName
import feh.tec.rubik.RubikCubeImage.{ReadSide, SidesMap, ColorMap}
import feh.util.file._
import Ordering.Implicits._

object ColorMaps {

  object SideNames{

    def fromFile(path: String): ColorMap[Int, SideName] = {
      val file = new File(path)
      val strData = file.withInputStream(File.read[Seq[String]]).get
      val compareType = strData.head
      val pfs = strData.tail.map(_.split(',').map(_.trim) match {
        case Array(min, max, sideName) =>
          val (mx, mn) = (min.toInt, max.toInt)
          val side = SideName.fromString(sideName)
          if (compareType startsWith "strict")
            ({
              case i if i betweenStrict (mx, mn) => side
            } : PartialFunction[Int, SideName])
          else
            ({
              case i if i between (mx, mn) => side
            } : PartialFunction[Int, SideName])

      })
      ColorMap(pfs reduceLeft (_ orElse _))
    }

  }


  implicit class BetweenWrapper[T: Ordering](t: T){
    def between(min: T, max: T) = t >= min && t <= max
    def betweenStrict(min: T, max: T) = t >= min && t < max
  }
}


object SidesMaps {

  def default = SidesMap(Seq(
    ReadSide(SideName.Up),
    ReadSide(SideName.Back, flipX = true, flipY = true),
    ReadSide(SideName.Down),
    ReadSide(SideName.Front),
    ReadSide(SideName.Right, flipY = true, flipX = true),
    ReadSide(SideName.Left)
  ))

}
