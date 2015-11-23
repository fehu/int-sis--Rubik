package feh.tec.nxt.run

import feh.tec.nxt.RubikCubeImage
import feh.tec.nxt.RubikCubeImage.ColorMap
import feh.tec.rubik.ogl.run.RubikCubeTestGLDefault
import feh.util._
import feh.tec.rubik.RubikCube._
import feh.tec.rubik.RubikCube.SideName._
import feh.tec.rubik.RubikCubeInstance

object FromCubeImage {

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

    apply(grouped.toSeq, parent, description)
  }
  def apply[T](cubes: Seq[(CubeId, CubeWithOrientation[T])],
               parent: Option[RubikCubeInstance[T]],
               description: String): RubikCubeInstance[T] = RubikCubeInstance(cubes.toMap, parent, description)


}

object ImageTst extends RubikCubeTestGLDefault{
  
  def sides = Seq(
    (Set(Front, Left),        (Middle(Front,Left),        CubeOrientation(Front,Left,null))),
    (Set(Up, Left, Front),    (Corner(Up,Back,Right),     CubeOrientation(Up,Front,Left))),
    (Set(Up, Right, Back),    (Corner(Back,Back,Left),    CubeOrientation(Up,Back,Right))),
    (Set(Up, Right),          (Middle(Back,Right),        CubeOrientation(Up,Right,null))),
    (Set(Left),               (Center(Up),                CubeOrientation(Left,null,null))),
    (Set(Back, Down),         (Middle(Back,Back),         CubeOrientation(Back,Down,null))),
    (Set(Down),               (Center(Back),              CubeOrientation(Down,null,null))),
    (Set(Back, Right),        (Middle(Back,Left),         CubeOrientation(Back,Right,null))),
    (Set(Up, Left),           (Middle(Up,Down),           CubeOrientation(Up,Left,null))),
    (Set(Front, Right),       (Middle(Right,Right),       CubeOrientation(Front,Right,null))),
    (Set(Back, Right, Down),  (Corner(Back,Front,Left),   CubeOrientation(Back,Down,Right))),
    (Set(Back),               (Center(Back),              CubeOrientation(Back,null,null))),
    (Set(Front),              (Center(Left),              CubeOrientation(Front,null,null))),
    (Set(Down, Front),        (Middle(Back,Front),        CubeOrientation(Down,Front,null))),
    (Set(Up),                 (Center(Right),             CubeOrientation(Up,null,null))),
    (Set(Down, Left),         (Middle(Down,Left),         CubeOrientation(Down,Left,null))),
    (Set(Down, Right),        (Middle(Front,Right),       CubeOrientation(Down,Right,null))),
    (Set(Up, Right, Front),   (Corner(Front,Up,Right),    CubeOrientation(Up,Front,Right))),
    (Set(Up, Left, Back),     (Corner(Up,Front,Right),    CubeOrientation(Up,Back,Left))),
    (Set(Up, Front),          (Middle(Up,Back),           CubeOrientation(Up,Front,null))),
    (Set(Back, Left, Down),   (Corner(Back,Down,Left),    CubeOrientation(Back,Down,Left))),
    (Set(Down, Left, Front),  (Corner(Back,Front,Left),   CubeOrientation(Down,Front,Left))),
    (Set(Up, Back),           (Middle(Right,Back),        CubeOrientation(Up,Back,null))),
    (Set(Right),              (Center(Right),             CubeOrientation(Right,null,null))),
    (Set(Back, Left),         (Middle(Back,Up),           CubeOrientation(Back,Left,null))),
    (Set(Down, Right, Front), (Corner(Back,Right,Right),  CubeOrientation(Down,Front,Right)))
  )


  val rubik = FromCubeImage(ImageTst.sides, None, "test")
  
  run()

}