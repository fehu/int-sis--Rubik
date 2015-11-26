import CommonSettings._

name := "rubik-nxt"

CommonSettings()





resolvers ++= Seq(
  "Sonatype OSS Snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype OSS Releases"   at "https://oss.sonatype.org/content/repositories/releases",
  "Fehu's github repo"      at "http://fehu.github.io/repo"
)


libraryDependencies ++= Seq(
  "feh.util" %% "util" % "1.0.9-SNAPSHOT"
//  "org.nuiton.thirdparty" % "JRI" % "0.9-6"
)






lazy val nxjHome = fileFromEnvVar("NXJ_HOME", "No 'NXT_HOME' environment variable set")

lazy val nxjJars = Seq(
  nxjHome / "lib" / "pc" ** "*.jar",
  nxjHome / "lib" / "pc" / "3rdparty" ** "*.jar"
)


myJars ++= nxjJars



initialCommands in console :=
  """import lejos.nxt._
    |import feh.tec.nxt._
    |
    |import CubeSideAction._
    |import LegoRobotRubik._
    |
  """.stripMargin
