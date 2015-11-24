package feh.tec.nxt

import feh.tec.nxt.RubikCubeImage.Side
import feh.tec.rubik.RubikCube.SideName
import feh.util.file._


trait RubikCubeImageFromFile {

  def raw(lines: List[String]): RubikCubeImage[Int] = {
    val data = readImageLines(extractChapter("RAW", lines), None)
    RubikCubeImage(
      data.groupBy(_._1)
        .mapValues(l => Side(l.map(p => p._2 -> p._3.toInt).toMap))
        .values.toSeq
    )
  }

  def colors(lines: List[String]): RubikCubeImage[SideName] = {
    val data = readImageLines(extractChapter("COLORS", lines), None)
    RubikCubeImage(
      data.groupBy(_._1)
        .mapValues(l => Side(l.map(p => p._2 -> SideName.fromString(p._3)).toMap))
        .values.toSeq
    )
  }

  type FullImage = RubikCubeImage[(Int, SideName)]

  def apply(lines: List[String]): FullImage = raw(lines) merge colors(lines)
  def apply(file: File): FullImage = apply( file.withInputStream(File.read[Seq[String]]).get.toList )


  protected def extractChapter(chapter: String, lines: List[String]) = lines
    .dropWhile(s => !s.startsWith(":" + chapter))
    .tail
    .takeWhile(s => !s.startsWith(":"))

  protected def readImageLines(lines: List[String], side: Option[SideName]): List[(SideName, (Int, Int), String)] =
    lines match {
      case h :: t if h.trim.isEmpty => readImageLines(t, side)
      case h :: t if h startsWith "-- " =>
        val side = SideName.fromString(h.drop(3).trim)
        readImageLines(t, Some(side))
      case h :: t if side.isDefined => h.split(',') match {
        case Array(x, y, v) => (side.get, x.trim.toInt -> y.trim.toInt,  v.trim) :: readImageLines(t, side)
      }
      case Nil => Nil
    }
}

object RubikCubeImageFromFile extends RubikCubeImageFromFile