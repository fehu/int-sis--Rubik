package feh.tec.nxt.run

import feh.tec.nxt.{CreateRubikInstance, RubikCubeImageFromFile}
import feh.tec.rubik.ogl.run.RubikCubeTestGLDefault
import feh.util.Path
import feh.util.file._

object ShowCubeImage extends RubikCubeTestGLDefault{

  val filePath = Path(args.head, File.separatorChar)
  val file = filePath.file

  val img = RubikCubeImageFromFile(file).map(_._2)

  implicit def sidesMap = SidesMaps.default

  val rubik = CreateRubikInstance(img, None, filePath.splittedName._1)

  run()
}
