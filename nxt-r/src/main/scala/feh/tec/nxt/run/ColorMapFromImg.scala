package feh.tec.nxt.run

import feh.tec.nxt.RubikCubeImage.Side
import feh.tec.nxt.RubikCubeImageFromFile
import feh.util.{AbsolutePath, Path}
import feh.util.file._

object ColorMapFromImg extends App with ColorStats{

  val fileName = args.head
  val filePath = Path.absolute(fileName)
  val img = RubikCubeImageFromFile.raw(filePath.file)


  def plotsDir = AbsolutePath(sys.props("user.dir")) / "plots"

  def filePrefix = filePath.splittedName._1 + "-"

  // todo: needs the actual colors for each sub-cube side

  val sides: Seq[Side[Int]] = Nil // img.sides.map(_.map(_.toInt))

  boxplot()
}
