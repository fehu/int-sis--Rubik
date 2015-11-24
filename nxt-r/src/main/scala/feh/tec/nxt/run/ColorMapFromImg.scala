package feh.tec.nxt.run

import feh.tec.nxt.RubikCubeImage.Side
import feh.tec.nxt.RubikCubeImageFromFile
import feh.tec.rubik.RubikCube.SideName
import feh.util.{AbsolutePath, Path}
import feh.util.file._

object ColorMapFromImg extends App with ColorStats{

  val fileName = args.head
  val filePath = Path.absolute(fileName)
  val img = RubikCubeImageFromFile(filePath.file)


  def plotsDir = AbsolutePath(sys.props("user.dir")) / "plots"

  def filePrefix = filePath.splittedName._1 + "-"


  val sideNames = SidesMaps.default.readOrder.map(_.name)
  val sideOrd = Ordering.by(sideNames.indexOf[SideName])

  // todo: needs the actual colors for each sub-cube side

  val sides: Seq[Side[Int]] = img.sides
    .flatMap(_.colors.values)
    .groupBy(_._2)
    .mapValues(_.map(_._1))
    .toSeq.sortBy(_._1)(sideOrd)
    .map{
      case (_, vals) => Side((
        for {
          i <- 0 until 3
          j <- 0 until 3
        } yield (i, j) -> vals(i*3+j)
      ).toMap)
    }

  boxplot()
}
