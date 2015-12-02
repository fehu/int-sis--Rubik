package feh.tec.nxt.run

import java.util.Date

import akka.actor.ActorDSL._
import akka.actor.ActorSystem
import feh.tec.rubik.RubikCube.SideName
import feh.tec.rubik.RubikCubeImageIO.{ColorsImage, ImageType}
import feh.tec.rubik._
import feh.tec.rubik.ogl.App3DControls.{KeyEvent, MutableState, MutableStateHook}
import feh.util.Path
import feh.util.file._
import org.lwjgl.input.Keyboard

// todo: move to GL
object CreateCubeImages extends WithCubeImage[MutableRubikCube[SideName]]{
  implicit val aSystem = ActorSystem()

  val rubik = new MutableRubikCube(RubikSubCubesDefault.cubes)

  protected val snapshotRequested = new MutableState(false)

  override protected def onKeyPressed = super.onKeyPressed orElse {
    case KeyEvent(Keyboard.KEY_RETURN) => snapshotRequested set true
  }

  override protected def initApp() = {
    super.initApp()

    controlHooks += MutableStateHook(snapshotRequested, ifTrue{
      snapshotRequested set false
      val time = new Date()
      val snap = rubik.snapshot

      actor(new Act{
        val imgs = Map[RubikCubeImage[_], ImageType]( RubikCubeImage(snap) -> ColorsImage )
        RubikCubeImageIO.write(imgs, t => (Path.relative("../snapshots") / ("rubik-snapshot-" + t + ".rci")).file, time)
      })
    })
  }

  run()
}
