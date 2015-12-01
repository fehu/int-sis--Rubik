package feh.tec.nxt.run

import java.text.DateFormat
import java.util.Date

import akka.actor.ActorSystem
import feh.tec.nxt.NameUtils
import feh.tec.rubik.ogl.App3DControls.{MutableStateHook, MutableState, KeyEvent}
import feh.tec.rubik.{RubikCubeImage, RubikSubCubesDefault, MutableRubikCube, RubikCube}
import feh.tec.rubik.RubikCube.SideName
import feh.util.Path
import feh.util.file._
import org.lwjgl.input.Keyboard
import akka.actor.ActorDSL._

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
        val timeStr = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(time)
        val img = RubikCubeImage(snap)
        val cubesImgStr = RubikCubeImage.toString(img)
        val fStr = timeStr + "\n:COLORS\n" + cubesImgStr
        val fPath = Path.relative("../snapshots") / NameUtils.formatDateFile("rubik-snapshot-", ".rci")
        fPath.file withOutputStream File.write.utf8(fStr)
      })
    })
  }

  run()
}
