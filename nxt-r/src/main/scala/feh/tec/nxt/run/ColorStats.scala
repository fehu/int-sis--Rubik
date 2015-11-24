package feh.tec.nxt.run

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import feh.tec.nxt.RubikCubeImage
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
    for((RubikCubeImage.Side(colors), i) <- sides zip rSideNames)
      yield {
        R.assign(i.toString, colors.values.toArray)
        i -> colors.values
      }
    ).toMap

  lazy val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME

  lazy val names = rSideNames.map(n => '\"' + n.toString + '\"')

  def boxplot() = {
    colors
    R.withPng(plotsDir / (filePrefix + LocalDateTime.now.format(dateFormat) + ".png")){
      _.eval(s"boxplot(${rSideNames.mkString(",")}, names=${names.mkString("c(", ",", ")")})")
    }
  }


}