name := "Rubik-OpenGL"

version := "0.3-SNAPSHOT"

CommonSettings()


libraryDependencies ++= Seq(
  "com.storm-enroute" %% "macrogl" % "0.4-SNAPSHOT"
)

LWJGLPlugin.lwjglSettings

mainClass in assembly := Some("feh.tec.rubik.ogl.run.RubikCubeTestGLAppRunner")

test in assembly := {}


//lwjgl.os := ("osx", "lib")
//lwjgl.os := ("windows", "dll")

//assemblyJarName in assembly := "A-star-rubik-win.jar"




lwjgl.nativesDir in assembly := target.value / "classes" / "lwjgl-resources"

assembly := {
  lwjgl.copyNatives.value
  assembly.value
}


assemblyMergeStrategy in assembly := {
  case PathList("feh", "tec", "puzzles", xs @ _*)               => MergeStrategy.discard
  case PathList("feh", "dsl", xs @ _*)                          => MergeStrategy.discard
  case PathList("feh", "dsl", xs @ _*)                          => MergeStrategy.discard
  case PathList("scala", "xml", xs @ _*)                        => MergeStrategy.discard
  case PathList("akka", xs @ _*)                                => MergeStrategy.discard
//  case PathList("lwjgl-resources", _, x) if x contains "openal" => MergeStrategy.discard
  case PathList(x) if x endsWith ".properties"                  => MergeStrategy.discard
  case PathList(x) if x endsWith ".txt"                         => MergeStrategy.discard
  case PathList(x) if x endsWith ".conf"                        => MergeStrategy.discard
  case PathList(x) if x endsWith ".dll"                         => MergeStrategy.discard
  case PathList(x) if x endsWith ".so"                          => MergeStrategy.discard
  case PathList(x) if x endsWith ".dylib"                       => MergeStrategy.discard
  case PathList(x) if x endsWith ".jnilib"                      => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}