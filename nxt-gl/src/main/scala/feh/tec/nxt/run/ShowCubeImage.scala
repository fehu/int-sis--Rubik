package feh.tec.nxt.run

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import feh.tec.nxt.RubikCubeImageFromFile
import feh.tec.rubik.RubikCube.SideName
import feh.tec.rubik.RubikCubeInstance.{InitialDescription, Rotation, RotationAngle}
import feh.tec.rubik.ogl.App3DControls.{KeyEvent, MutableState, MutableStateHook}
import feh.tec.rubik.ogl.run.RubikCubeTestGLDefault
import feh.tec.rubik.solve.{RubikCubeHeuristics, RubikCube_A_*}
import feh.tec.rubik.{RubikCube, CreateRubikInstance, RubikCubeInstance}
import feh.util.Path
import feh.util.file._
import org.lwjgl.input.Keyboard

import scala.concurrent.duration._

trait WithCubeImage[C <: RubikCube[SideName, C]] extends RubikCubeTestGLDefault[C]{
  lazy val filePath = Path(args.head, File.separatorChar)
  lazy val file = filePath.file

  lazy val img = RubikCubeImageFromFile.colorsOnly(file)

  implicit def sidesMap = SidesMaps.default
  def mkInitialCube = CreateRubikInstance(img, None, InitialDescription(filePath.splittedName._1))
}

// todo: move to GL
object ShowCubeImage extends WithCubeImage[RubikCubeInstance[SideName]]{


  val rubik = mkInitialCube

  run()
}

// todo: move to GL
object SolveCubeImage extends WithCubeImage[RubikCubeInstance.MutableContainer[SideName]]{

  val sleepTime = 1.second

  val initial = CreateRubikInstance(img, None, InitialDescription(filePath.splittedName._1))
  val rubik = new RubikCubeInstance.MutableContainer(initial)


  val solver = new RubikCube_A_*.WithTricks[SideName](RubikCubeHeuristics.SomeTricks.Stage1)
  solver.DEBUG = true

  val res = solver.search(initial)

  println(res)
  val solution = res._1.get

  val solveSeq = (solution +: solver.listParents(solution)).reverse.toList


  class ShowSolutionController(interrupt: Boolean,
                               waitTime: FiniteDuration,
                               solutionSeq: => SolutionSeq,
                               reInit: () => Unit)
    extends Actor
  {
    var running = false

    val showAct = context.system.actorOf(Props(new ShowSolutionActor(waitTime, self)))

    def receive: Receive = {
      case "show" if !running =>
        running = true
        show()
      case "show" if interrupt => showAct ! "interrupt"
      case "show" =>

      case "finished"    => running = false
      case "interrupted" if interrupt && running => show()
    }

    def show() = {
      reInit()
      context.system.scheduler.scheduleOnce(waitTime, showAct, solutionSeq)(context.dispatcher)
    }
  }

  case class SolutionSeq(get: List[RubikCubeInstance[SideName]])

  class ShowSolutionActor(waitTime: FiniteDuration, controller: ActorRef) extends Actor{
    var interrupted = false

    def receive: Actor.Receive = {
      case _: SolutionSeq if interrupted =>
        interrupted = false
        controller ! "interrupted"
      case SolutionSeq(h :: t) =>
        val done = h.description match {
          case Rotation(RotationAngle.Rot90, side) => rubik.rotate(side); true
          case _ => false
        }
        if (done) context.system.scheduler.scheduleOnce(waitTime, self, SolutionSeq(t))(context.dispatcher)
        else self ! SolutionSeq(t)
      case SolutionSeq(Nil) => controller ! "finished"
      case "interrupt" => interrupted = true
    }
  }


  lazy val aSys = ActorSystem.create()
  lazy val showSolutionActor = aSys.actorOf(Props(
    new ShowSolutionController(
      interrupt = true,
      sleepTime,
      SolutionSeq(solveSeq),
      () => rubik.set(initial)
    )
  ))


  protected lazy val showSolutionRequested = new MutableState(false)

  override protected def onKeyPressed: PartialFunction[KeyEvent, Unit] = super.onKeyPressed orElse {
    case KeyEvent(Keyboard.KEY_F1) => showSolutionRequested set true
  }

  override protected def initApp(): Unit = {
    super.initApp()
    showSolutionActor

    controlHooks += MutableStateHook(showSolutionRequested, ifTrue{
      showSolutionRequested set false
      showSolutionActor ! "show"
    })
  }


  run()

}