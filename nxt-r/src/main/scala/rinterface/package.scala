import feh.util.{AbsolutePath, Path}
import org.rosuda.JRI.Rengine

package object rinterface {

  implicit class RengineWrapper(r: Rengine){
    def withPng(path: AbsolutePath)(f: Rengine => Unit): Unit = {
      r.eval(s"""png("$path")""")
      f(r)
      r.eval("dev.off()")
    }
  }

}
