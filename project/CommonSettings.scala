import sbt._
import sbt.Keys._

object CommonSettings{

  def apply() = Seq(
    organization := "feh.tec",
    scalaVersion := "2.11.7",
    resolvers    ++= Seq(
      "Fehu's github repo" at "http://fehu.github.io/repo",
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
    ),
    scalacOptions in (Compile, doc) ++= Seq("-diagrams", "-diagrams-max-classes", "50", "-diagrams-max-implicits", "20"),
    
    myJars := Nil,
    myJarsClasspath <<= myJars.map(_.map(_.classpath).reduceLeftOption(_ ++ _).getOrElse(Nil)),
    unmanagedJars in Compile := myJarsClasspath.value,
    unmanagedJars in Runtime := myJarsClasspath.value,

    libraryDependencies ++= Seq(
        "feh.tec" %% "a-star" % "0.7-SNAPSHOT",
        "feh.tec" %% "a-star-rubik" % "0.4-SNAPSHOT"
      )
  )

  
  def fileFromEnvVar(v: String, errMsg: => String = null) = {
    val err = Option(errMsg).getOrElse(s"No '$v' environment variable set")
    file(sys.env.getOrElse(v, sys.error(err)))
  }

  lazy val myJars = taskKey[Seq[PathFinder]]("classpath jars")
  lazy val myJarsClasspath = taskKey[Classpath]("my classpath")

}