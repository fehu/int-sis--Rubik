package feh.tec.nxt

import feh.tec.nxt.RubikCubeImage.{SidesMap, ColorMap}
import feh.tec.rubik.RubikCube._
import feh.tec.rubik.RubikCubeInstance
import feh.util._

object CreateRubikInstance {

  def fromRaw[T, C](sidesMap: Map[SideName, Map[(Int, Int), T]],
                    parent: Option[RubikCubeInstance[C]],
                    description: String)
                   (implicit cm: ColorMap[T, C], wsn: WithSideName[C]): RubikCubeInstance[C] =
    apply(sidesMap.mapValues(_.mapValues(cm.colorFor)), parent, description)

  def apply[T: WithSideName](sidesMap: Map[SideName, Map[(Int, Int), T]],
                             parent: Option[RubikCubeInstance[T]],
                             description: String): RubikCubeInstance[T] =
  {
    val idsO = sidesMap.flatMap{
      case (side, tMap) =>
        val idsMap = sideCubes(side)
        tMap.mapKeys(idsMap andThen (_ -> side))
    }
    val grouped = RubikCubeImage groupCubes idsO.groupBy(_._1).values.flatMap(_.map{
      case ((id, side), x) => id -> (side, x)
    }).toMap

    apply(grouped, parent, description)
  }
  def apply[T](cubes: Map[CubeId, CubeWithOrientation[T]],
               parent: Option[RubikCubeInstance[T]],
               description: String): RubikCubeInstance[T] = RubikCubeInstance(cubes, parent, description)


  def apply[T: WithSideName](img: RubikCubeImage[T],
                             parent: Option[RubikCubeInstance[T]],
                             description: String)
                            (implicit sMap: SidesMap): RubikCubeInstance[T] =
    RubikCubeInstance(RubikCubeImage.readCubes(img), parent, description)

}