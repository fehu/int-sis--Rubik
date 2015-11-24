Rubik-NXT-R
===========

Uses [JRI](https://rforge.net/JRI/) for plotting with [R](https://www.r-project.org/).


Gathers statistics abous cube's side colors and creates boxplot files.

Requires the following environment variables set: 
- **NXJ_HOME**: for example `/home/user/leJOS_NXJ_0.9.1beta-3`
- **R_HOME**: for example `/usr/local/lib64/R`; R needs to be compiled with `--enable-R-shlib` option
- **R_JAVA**: for example `/home/user/R/x86_64-pc-linux-gnu-library/3.2/rJava`
- **LD_LIBRARY_PATH**: for example `$R_HOME/lib`

Writes images into [plots](/plots).

Thanks to _Roelof Oomen_ for [his code](/nxt-r/src/main/scala/rinterface/RInterface.scala).

### Executable **BuildColorMap**

Reads colors from the cube and build a **ColorsMap**, that establishes limits between sides colors.

Takes one optional argument: **file** to write the map in.

