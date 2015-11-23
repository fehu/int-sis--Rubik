package feh.tec.nxt.run

import feh.tec.nxt.RubikCubeImage
import feh.tec.nxt.run.RobotConfig.Default._
import feh.tec.rubik.RubikCube._
import feh.tec.rubik.ogl.{DefaultRubikColorScheme, RubikCubeTestGL, ShaderProg}
import feh.tec.rubik.{RubikCubeInstance, RubikSubCubesDefault}
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.ContextAttribs
import org.macrogl.Matrix

object BuildCubeTest extends RubikCubeTestGL[SideName]{
  var blink_? = true

  if (args.isEmpty) {
    println("no SideNames file provided")
    sys.exit(255)
  }

  implicit val cMap = ColorMaps.SideNames.fromFile(args.head)
  implicit lazy val sMap = SidesMaps.default

  if (!blink_?) ls.setFloodlight(true)
  val initialCubes = RubikCubeImage.readCubes(gatherColor(blink_?))

  val initialRubik = RubikCubeInstance(initialCubes, None, "initial")





  val displayX = 800
  val displayY = 600

  val fps = 30

  val contextAttributes = new ContextAttribs(2, 1)

  val projectionTransform = Matrix.perspectiveProjection(50, displayX.toFloat / displayY, 0.1, 100.0)
  val camera = new Matrix.Camera(8, 8, 8)

  def cameraSpeed = 5.0
  def mouseSensibility = 0.05
  def exitKey = Keyboard.KEY_ESCAPE


  val shader: ShaderProg = Shaders.forGLSL("1.2")

  implicit def withSideName = RubikSubCubesDefault.WithSideNameIdentity
  implicit def colors = DefaultRubikColorScheme


  val rubik = new RubikCubeInstance.MutableContainer(initialRubik)

  protected def initialMouseXShift = -displayX / 100
  protected def initialMouseYShift = -displayY / 100

  run()

}
