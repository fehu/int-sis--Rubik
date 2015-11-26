name := "rubik-root"

publishArtifact := false

CommonSettings()


lazy val root = project in file(".") aggregate (GL, nxt, nxtR, nxtGL)


lazy val GL     = project in file("gl")

lazy val nxt    = project in file("nxt")

lazy val nxtR   = project in file("nxt-r") dependsOn nxt

lazy val nxtGL  = project in file("nxt-gl") dependsOn (nxt, GL)




