/**
 * @author Roelof Oomen, with code from rJava examples
 *
 */
package rinterface

import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.awt.Component
import java.awt.FileDialog
import java.awt.Frame
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream

import org.rosuda.JRI.RMainLoopCallbacks
import org.rosuda.JRI.Rengine
//import org.rosuda.javaGD.GDInterface
//import org.rosuda.javaGD.JGDBufferedPanel

import javax.swing.JFrame
import javax.swing.WindowConstants

/**
 * Callback class for R interface.
 * @param out Stream to output R messages to, e.g. StdOut: System.out
 */
//TODO: JGR manages to have for example the R "demo()" window functioning, I don't.
private class RConsole( out: PrintStream ) extends RMainLoopCallbacks {

  def rWriteConsole( re: Rengine, text: String, oType: Int ) {
    out.println( text )
  }

  def rBusy( re: Rengine, which: Int ) {
    //TODO: Change cursor to hourglass here, but that would mean I would need to merge this class with
    // the class implementing the frame with the console.
    //.out.println( "rBusy(" + which + ")" )
  }

  def rReadConsole( re: Rengine, prompt: String, addToHistory: Int ): String = {
    out.print( prompt )
    try {
      val br = new BufferedReader( new InputStreamReader( System.in ) )
      val s = br.readLine()
      return if ( s == null || s.length() == 0 ) s else s + "\n"
    } catch {
      case e: Exception => out.println( "jriReadConsole exception: " + e.getMessage() )
    }
    return null
  }

  def rShowMessage( re: Rengine, message: String ) {
    out.println( "Error: \"" + message + "\"" )
  }

  def rChooseFile( re: Rengine, newFile: Int ) = {
    val fd = new FileDialog( new Frame(),
      if ( newFile == 0 ) "Select a file" else "Select a new file",
      if ( newFile == 0 ) FileDialog.LOAD else FileDialog.SAVE )
    fd.setVisible( true )
    var res = ""
    if ( fd.getDirectory() != null )
      res = fd.getDirectory()
    if ( fd.getFile() != null )
      res = if ( res == null ) fd.getFile() else ( res + fd.getFile() )
    res
  }

  def rFlushConsole( re: Rengine ) {
    out.flush
  }

  def rLoadHistory( re: Rengine, filename: String ) {
  }

  def rSaveHistory( re: Rengine, filename: String ) {
  }
}

/*
/**
 * Class providing the Frame that will function as a "Java Graphics Device" in R.
 * This class is not required, but can be used to extend the functionality of the
 * JavaGD frame, or to embed it in another window.
 */
class RWindow extends GDInterface with WindowListener {

  var fGD: JFrame = null

  override def gdOpen( w: Double, h: Double ) {
    if ( fGD != null ) gdClose()
    fGD = new JFrame( "JavaGD" )
    fGD.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE )
    fGD.addWindowListener( this )
    c = new JGDBufferedPanel( w, h )
    fGD.getContentPane().add( c.asInstanceOf[Component] )
    fGD.pack()
    fGD.setVisible( true )
  }

  override def gdClose() {
    super.gdClose()
    if ( fGD != null ) {
      c = null
      fGD.removeAll()
      fGD.dispose()
      fGD = null
    }
  }

  /** listener response to "Close" - effectively invokes <code>dev.off()</code> on the device */
  def windowClosing( e: WindowEvent ) {
    if ( c != null )
      executeDevOff()
  }
  def windowClosed( e: WindowEvent ) {}
  def windowOpened( e: WindowEvent ) {}
  def windowIconified( e: WindowEvent ) {}
  def windowDeiconified( e: WindowEvent ) {}
  def windowActivated( e: WindowEvent ) {}
  def windowDeactivated( e: WindowEvent ) {}

}
*/

/**
 * Create link to R
 * @param out Stream to output R messages to, e.g. StdOut: System.out
 */
class RInterface( out: PrintStream ) {

  var engine: Rengine = null

  def startR: Boolean = {

    // just making sure we have the right version of everything
    if ( !Rengine.versionCheck() ) {
      System.err.println( "** Version mismatch - Java files don't match library version." )
      false
    } else {
      System.out.println( "Creating Rengine" )
      // 1) we don't pass the arguments from the command line
      // 2) we won't use the main loop at first, we'll start it later
      // (that's the "false" as second argument)
      // 3) the callbacks are implemented by the TextConsole class above
      //engine = new Rengine( null, false, new TextConsole() )
      engine = new Rengine( Array[String]( "--vanilla" ), false, new RConsole( out ) )

      System.out.println( "Rengine created, waiting for R" )
      // the engine that creates R is a new thread, so we should wait until it's ready
      if ( !engine.waitForR() ) {
        System.out.println( "Cannot load R" )
        false
      } else {
        true
      }
    }
  }

/*
  /**
   * Open a Java Graphics Device in R
   */
  def openJavaGD {
    engine.eval( "Sys.setenv('JAVAGD_CLASS_NAME'='rinterface/RWindow')" )
    engine.eval( "library(JavaGD)" )
    engine.eval( "JavaGD(width=1000, height=600, ps=12)" )
  }
*/

  override def finalize {
    // Seems not to be called ??
    if ( engine != null ) {
      if ( engine.isAlive ) {
        // Non-blocking eval
        engine.idleEval( "q(\"no\")" )
      }
      engine.end();
    }
  }
}

