import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.jme3.system.JmeCanvasContext;

public class EngineContainer extends JPanel implements WindowListener, ComponentListener {

  private Graphics3D engine = null;
  private Canvas canvas = null;
  private JmeCanvasContext context = null;
  

  public EngineContainer (int resx, int resy) {
    super (new FlowLayout());
    engine = new Graphics3D();
    context = engine.getCanvasObject();
    canvas = context.getCanvas();
    add(canvas);
    
  }
  
/* Das Probleme scheint hier verursacht zu werden  */
  @Override
  public void removeNotify () {
     super.removeNotify();
     engine.stop();
  }



  /* Aufgrund fehlender mehrfachvererbung müssen die Interfaces leider direkt und ohne Adapter implementiert werden*/
  //WindowListener Interface:
  public void windowActivated(WindowEvent e) {}
  public void windowClosed(WindowEvent e) {}
  public void windowClosing(WindowEvent e) {
    engine.stop(); 
  }

  public void windowDeactivated(WindowEvent e) {}
  public void windowDeiconified(WindowEvent e) {}
  public void windowIconified(WindowEvent e) {}
  public void windowOpened(WindowEvent e) {}

  
  //ComponentListener Interface:
  public void componentHidden(ComponentEvent e) {}
  public void componentMoved(ComponentEvent e) {}
  public void componentResized(ComponentEvent e) {}
  public void componentShown(ComponentEvent e) {}


} 

