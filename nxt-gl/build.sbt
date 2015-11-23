name := "Rubik-NXT-GL"

CommonSettings()

LWJGLPlugin.lwjglSettings

initialCommands in console :=
  """import feh.tec.nxt._
    |import feh.tec.nxt.run._
    |import feh.tec.rubik.ogl._
  """.stripMargin