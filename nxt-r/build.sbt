import CommonSettings._
import java.io.File

name := "Rubik-NXT-R"

CommonSettings()



lazy val rHome = fileFromEnvVar("R_HOME")

lazy val rPackagesHome = fileFromEnvVar("R_PACKAGES_HOME")

lazy val (rJava, rJavaGDHome) = (rPackagesHome / "rJava", rPackagesHome / "JavaGD")

lazy val rJavaGDJars = Seq[PathFinder]( rJavaGDHome / "java" ** "*.jar" )

lazy val jriJars = Seq[PathFinder]( rJava / "jri" ** "*.jar" )

//lazy val rLib = fileFromEnvVar("R_LIB")


myJars ++= jriJars ++ rJavaGDJars


lazy val libsPath = Seq(
  rJavaGDHome / "libs",
  rJava / "jri"
)


lazy val moreEnvVars = "LD_LIBRARY_PATH" -> {
  val LD_LIBRARY_PATH = sys.env.getOrElse("LD_LIBRARY_PATH", "")
  LD_LIBRARY_PATH + (rHome / "lib").absString + File.pathSeparatorChar
}


// doesn't work
envVars in Compile += moreEnvVars

// doesn't work

envVars in Runtime += moreEnvVars

// doesn't work
envVars in console += moreEnvVars



initialize ~= { _ =>
  sys.props += "java.library.path" -> libsPath.map(_.absString).mkString(File.pathSeparator)
  val LD_LIBRARY_PATH = sys.env.getOrElse("LD_LIBRARY_PATH", "") // .map(_ + File.pathSeparatorChar)
  // doesn't work
  System.setProperty("LD_LIBRARY_PATH", LD_LIBRARY_PATH + (rHome / "lib").absString + File.pathSeparatorChar)
}



initialCommands in console :=
  """import lejos.nxt._
    |import feh.tec.nxt._
    |
    |import CubeSideAction._
    |import LegoRobotRubik._
    |
  """.stripMargin
