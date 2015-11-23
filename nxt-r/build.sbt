import CommonSettings._
import java.io.File

name := "Rubik-NXT-R"

CommonSettings()



lazy val rJava = fileFromEnvVar("R_JAVA")

lazy val jriJars = Seq[PathFinder]( rJava / "jri" ** "*.jar" )


myJars ++= jriJars


lazy val libsPath = Seq( rJava / "jri" )


// doesn't work
//lazy val moreEnvVars = "LD_LIBRARY_PATH" -> {
//  val LD_LIBRARY_PATH = sys.env.getOrElse("LD_LIBRARY_PATH", "")
//  LD_LIBRARY_PATH + (rHome / "lib").absString + File.pathSeparatorChar
//}
//envVars in Compile += moreEnvVars
//envVars in Runtime += moreEnvVars
//envVars in console += moreEnvVars



initialize ~= { _ =>
  fileFromEnvVar("R_HOME")          // ensure is set
  fileFromEnvVar("LD_LIBRARY_PATH") // ensure is set
  sys.props += "java.library.path" -> libsPath.map(_.absString).mkString(File.pathSeparator)
// doesn't work
//  val LD_LIBRARY_PATH = sys.env.getOrElse("LD_LIBRARY_PATH", "") // .map(_ + File.pathSeparatorChar)
//  System.setProperty("LD_LIBRARY_PATH", LD_LIBRARY_PATH + (rHome / "lib").absString + File.pathSeparatorChar)
}



initialCommands in console :=
  """import lejos.nxt._
    |import feh.tec.nxt._
    |
    |import CubeSideAction._
    |import LegoRobotRubik._
    |
  """.stripMargin
