# Rubik-NXT
_(A project for master studies at ITESM)_

__Control for a LEGO NXT robot with goal of solving Rubik's cubes.__

Depends on [A* project](https://github.com/fehu/int-sis--AStar).



Subprojects
-----------

### [GL](gl)
Rubik's Cube representation with OpenGL.

### [NXT](nxt)
Control for a LEGO NXT robot. 
Uses [NXJ PC API](http://www.lejos.org/nxt/pc/api/) for connection via USB.

Requires **NXJ_HOME** environment variables set.
 
### [NXT-R](nxt-r)
Gathers statistics abous cube's side colors. 
Uses [JRI](https://rforge.net/JRI/) for plotting with [R](https://www.r-project.org/).

Requires the following environment variables set: **NXJ_HOME**, **R_HOME**, **R_PACKAGES_HOME**.

### [NXT-GL](nxt-gl)
Applications that use both [NXT](nxt) and [GL](gl).

