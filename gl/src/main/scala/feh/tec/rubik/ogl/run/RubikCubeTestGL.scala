package feh.tec.rubik.ogl.run

import feh.tec.rubik.RubikCube.SideName._
import feh.tec.rubik.RubikCube._
import feh.tec.rubik.RubikCubeInstance.NoDescription
import feh.tec.rubik.ogl._
import feh.tec.rubik.{CreateRubikInstance, MutableRubikCube, RubikCubeInstance, RubikSubCubesDefault}
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.ContextAttribs
import org.macrogl._

object RubikCubeTestGLAppRunner extends App{ PrepareNatives.andThen{ RubikCubeTestGLApp.main(args) } }



object RubikCubeTestGLApp extends RubikCubeTestGLDefault{

  val mutRubik = new MutableRubikCube[SideName](RubikSubCubesDefault.cubes)
  val rubik = new RubikCubeInstance.MutableContainer(mutRubik.snapshot)

  run()

}

trait RubikCubeTestGLDefault extends RubikCubeTestGL[SideName]{

  lazy val displayX = 800
  lazy val displayY = 600

  lazy val fps = 30

  val contextAttributes = new ContextAttribs(2, 1)
  //    .withForwardCompatible(true)
  //    .withProfileCore(true)
  //    .withProfileES(true)

  val projectionTransform = Matrix.perspectiveProjection(50, displayX.toFloat / displayY, 0.1, 100.0)
  val camera = new Matrix.Camera(8, 8, 8)

  def cameraSpeed = 5.0
  def mouseSensibility = 0.05

  def exitKey = Keyboard.KEY_ESCAPE
  def disableMouseKey = Some(Keyboard.KEY_F4)

  val shader: ShaderProg = Shaders.forGLSL("1.2")
  //        "lightDirection"  -> (0.0f, -1.0f, -1.0f),

  implicit def withSideName = RubikSubCubesDefault.WithSideNameIdentity
  implicit def colors = DefaultRubikColorScheme

  protected def initialMouseXShift = -displayX / 100
  protected def initialMouseYShift = -displayY / 100

}

object Tst extends RubikCubeTestGLDefault{

  /** rotated: Front 90. */
  def rotation_1 = Map(
    Front -> Map(
      (0, 2) -> Front,
      (1, 2) -> Front,
      (2, 2) -> Front,
      (0, 1) -> Front,
      (1, 1) -> Front,
      (2, 1) -> Front,
      (0, 0) -> Front,
      (1, 0) -> Front,
      (2, 0) -> Front),
    Right -> Map(
      (0, 2) -> Up,
      (1, 2) -> Right,
      (2, 2) -> Right,
      (0, 1) -> Up,
      (1, 1) -> Right,
      (2, 1) -> Right,
      (0, 0) -> Up,
      (1, 0) -> Right,
      (2, 0) -> Right),
    Left -> Map(
      (0, 2) -> Left,
      (1, 2) -> Left,
      (2, 2) -> Down,
      (0, 1) -> Left,
      (1, 1) -> Left,
      (2, 1) -> Down,
      (0, 0) -> Left,
      (1, 0) -> Left,
      (2, 0) -> Down),
    Up -> Map(
      (0, 2) -> Up,
      (1, 2) -> Up,
      (2, 2) -> Up,
      (0, 1) -> Up,
      (1, 1) -> Up,
      (2, 1) -> Up,
      (0, 0) -> Left,
      (1, 0) -> Left,
      (2, 0) -> Left),
    Down -> Map(
      (0, 2) -> Right,
      (1, 2) -> Right,
      (2, 2) -> Right,
      (0, 1) -> Down,
      (1, 1) -> Down,
      (2, 1) -> Down,
      (0, 0) -> Down,
      (1, 0) -> Down,
      (2, 0) -> Down),
    Back -> Map(
      (0, 2) -> Back,
      (1, 2) -> Back,
      (2, 2) -> Back,
      (0, 1) -> Back,
      (1, 1) -> Back,
      (2, 1) -> Back,
      (0, 0) -> Back,
      (1, 0) -> Back,
      (2, 0) -> Back)
  )

  lazy val solvedCube = Map(
    Front -> Map(
      (0, 2) -> Front,
      (1, 2) -> Front,
      (2, 2) -> Front,
      (0, 1) -> Front,
      (1, 1) -> Front,
      (2, 1) -> Front,
      (0, 0) -> Front,
      (1, 0) -> Front,
      (2, 0) -> Front),
    Right -> Map(
      (0, 2) -> Right,
      (1, 2) -> Right,
      (2, 2) -> Right,
      (0, 1) -> Right,
      (1, 1) -> Right,
      (2, 1) -> Right,
      (0, 0) -> Right,
      (1, 0) -> Right,
      (2, 0) -> Right),
    Left -> Map(
      (0, 2) -> Left,
      (1, 2) -> Left,
      (2, 2) -> Left,
      (0, 1) -> Left,
      (1, 1) -> Left,
      (2, 1) -> Left,
      (0, 0) -> Left,
      (1, 0) -> Left,
      (2, 0) -> Left),
    Up -> Map(
      (0, 2) -> Up,
      (1, 2) -> Up,
      (2, 2) -> Up,
      (0, 1) -> Up,
      (1, 1) -> Up,
      (2, 1) -> Up,
      (0, 0) -> Up,
      (1, 0) -> Up,
      (2, 0) -> Up),
    Down -> Map(
      (0, 2) -> Down,
      (1, 2) -> Down,
      (2, 2) -> Down,
      (0, 1) -> Down,
      (1, 1) -> Down,
      (2, 1) -> Down,
      (0, 0) -> Down,
      (1, 0) -> Down,
      (2, 0) -> Down),
    Back -> Map(
      (0, 2) -> Back,
      (1, 2) -> Back,
      (2, 2) -> Back,
      (0, 1) -> Back,
      (1, 1) -> Back,
      (2, 1) -> Back,
      (0, 0) -> Back,
      (1, 0) -> Back,
      (2, 0) -> Back)
  )


  //  val mutRubik = new MutableRubikCube[SideName](RubikSubCubesDefault.cubes)
  val rubik = new RubikCubeInstance.MutableContainer(CreateRubikInstance(rotation_1, None, NoDescription))

  run()

}