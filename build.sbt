import java.io.File

organization := "feh.tec"

name := "NXT-Rubik"

scalaVersion := "2.11.7"



resolvers ++= Seq(
  "Sonatype OSS Snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype OSS Releases"   at "https://oss.sonatype.org/content/repositories/releases",
  "Fehu's github repo"      at "http://fehu.github.io/repo"
)



lazy val rubik = ProjectRef(file("../A-Star"), "rubik")

lazy val root = project in file(".") dependsOn rubik


libraryDependencies ++= Seq(
  "feh.util" %% "util" % "1.0.9-SNAPSHOT"
//  "org.nuiton.thirdparty" % "JRI" % "0.9-6"
)


def fileFromEnvVar(v: String, errMsg: => String = null) = {
  val err = Option(errMsg).getOrElse(s"No '$v' environment variable set")
  file(sys.env.getOrElse(v, sys.error(err)))
}

lazy val rHome = fileFromEnvVar("R_HOME")

lazy val rPackagesHome = fileFromEnvVar("R_PACKAGES_HOME")

lazy val (rJava, rJavaGDHome) = (rPackagesHome / "rJava", rPackagesHome / "JavaGD")

lazy val rJavaGDJars = Seq( rJavaGDHome / "java" ** "*.jar" )

lazy val jriJars = Seq( rJava / "jri" ** "*.jar" )

//lazy val rLib = fileFromEnvVar("R_LIB")


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




lazy val nxjHome = fileFromEnvVar("NXJ_HOME", "No 'NXT_HOME' environment variable set")

lazy val nxjJars = Seq(
  nxjHome / "lib" / "pc" ** "*.jar",
  nxjHome / "lib" / "pc" / "3rdparty" ** "*.jar"
)


def myJars = nxjJars ++ rJavaGDJars ++ jriJars

lazy val myJarsClasspath = myJars.map(_.classpath).reduceLeft(_ ++ _)

unmanagedJars in Compile := myJarsClasspath

unmanagedJars in Runtime := myJarsClasspath



initialCommands in console :=
  """import lejos.nxt._
    |import feh.tec.nxt._
    |
    |import CubeSideAction._
    |import LegoRobotRubik._
    |
    |import RobotTest._
  """.stripMargin
