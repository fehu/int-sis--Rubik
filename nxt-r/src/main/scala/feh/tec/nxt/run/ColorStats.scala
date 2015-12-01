package feh.tec.nxt.run

import feh.tec.nxt.NameUtils
import feh.tec.rubik.RubikCubeImage
import feh.util.AbsolutePath
import rinterface._

trait ColorStats {
  def sides: Seq[RubikCubeImage.Side[Int]]

  def filePrefix: String

  def plotsDir: AbsolutePath

  lazy val RInterface = new RInterface(System.out)
  RInterface.startR

  lazy val R = RInterface.engine

  //  Rengine.DEBUG = 1

  lazy val rSideNames = SidesMaps.default.readOrder.map(_.name)

  //  val colors = rSideNames.zip(1 to 6).toMap.mapValues(List.fill[Int](9)(_))
  lazy val colors = (
    for((RubikCubeImage.Side(colors, sideOpt), side) <- sides zip rSideNames)
      yield {
        val i = sideOpt.getOrElse(side)
        R.assign(i.toString, colors.values.toArray)
        i -> colors.values
      }
    ).toMap

  lazy val names = rSideNames.map(n => '\"' + n.toString + '\"')

  def boxplot() = {
    colors
    R.withPng(plotsDir / NameUtils.formatDateFile(filePrefix, ".png")){
      _.eval(s"boxplot(${rSideNames.mkString(",")}, names=${names.mkString("c(", ",", ")")})")
    }
  }


}
