package feh.tec.nxt.run

import feh.tec.nxt.RubikCubeImageFromFile
import feh.tec.rubik.RubikCube.SideName
import feh.tec.rubik.ogl.run.RubikCubeTestGLDefault
import feh.tec.rubik.solve.{RubikCubeHeuristics, RubikCube_A_*}
import feh.tec.rubik.{CreateRubikInstance, RubikCubeInstance}
import feh.util.Path
import feh.util.file._

trait WithCubeImage extends RubikCubeTestGLDefault{
  lazy val filePath = Path(args.head, File.separatorChar)
  lazy val file = filePath.file

  lazy val img = RubikCubeImageFromFile(file).map(_._2)

  implicit def sidesMap = SidesMaps.default
  def mkInitialCube = CreateRubikInstance(img, None, filePath.splittedName._1)
}


object ShowCubeImage extends WithCubeImage{


  val rubik = mkInitialCube

  run()
}


object SolveCubeImage extends WithCubeImage{

  val initial = CreateRubikInstance(img, None, filePath.splittedName._1)
  val rubik = new RubikCubeInstance.MutableContainer(initial)


  val solver = new RubikCube_A_*.WithTricks[SideName](RubikCubeHeuristics.SomeTricks.Stage1)
  solver.DEBUG = true

  val res = solver.search(initial)

  println(res)
  val solution = res._1.get

  val sParents = solver.listParents(solution)

  sParents.foreach(println)



}