name := "Rubik-root"

publishArtifact := false

CommonSettings()


lazy val aStarRubik = ProjectRef(file("../A-Star"), "rubik")

lazy val root = project in file(".") aggregate (openGL, nxt, nxtR, nxtGL)


lazy val openGL = project in file("gl") dependsOn aStarRubik

lazy val nxt    = project in file("nxt") dependsOn aStarRubik

lazy val nxtR   = project in file("nxt-r") dependsOn nxt

lazy val nxtGL  = project in file("nxt-gl") dependsOn (nxt, openGL)




