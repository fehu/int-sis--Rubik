package feh.tec.nxt.run

import feh.tec.nxt.RubikCubeImage.{ReadSide, SidesMap, ColorMap}
import feh.tec.rubik.RubikCube.SideName
import Ordering.Implicits._

object ColorMaps {

  object SideNames{

    def customGray = ColorMap[Int, SideName]{
      case i if i between (425, 470) => SideName.Up
      case i if i between (560, 600) => SideName.Back
      case i if i between (520, 560) => SideName.Down
      case i if i between (350, 400) => SideName.Front
      case i if i between (475, 520) => SideName.Right
      case i if i between (400, 425) => SideName.Left
  }

  }


  private implicit class BetweenWrapper[T: Ordering](t: T){
    def between(min: T, max: T) = t >= min && t <= max
  }
}


object SidesMaps {

  def default = SidesMap(Seq(
    ReadSide(SideName.Up),
    ReadSide(SideName.Back, flipX = true, flipY = true),
    ReadSide(SideName.Down),
    ReadSide(SideName.Front),
    ReadSide(SideName.Right, transposed = true, flipY = true),
    ReadSide(SideName.Left, transposed = true)
  ))

}
