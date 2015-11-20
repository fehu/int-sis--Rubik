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


libraryDependencies += "feh.util" %% "util" % "1.0.9-SNAPSHOT"


lazy val nxjHome = sys.env.getOrElse("NXJ_HOME", sys.error("No 'NXT_HOME' environment variable set"))

lazy val nxjJars = Seq(
  file(nxjHome) / "lib" / "pc" ** "*.jar",
  file(nxjHome) / "lib" / "pc" / "3rdparty" ** "*.jar"
).map(_.classpath).reduceLeft(_ ++ _)

unmanagedJars in Compile := nxjJars

unmanagedJars in Runtime := nxjJars



initialCommands in console :=
  """import lejos.nxt._
    |import feh.tec.nxt._
    |
    |import CubeSideAction._
    |import LegoRobotRubik._
    |
    |import RobotTest._
  """.stripMargin
