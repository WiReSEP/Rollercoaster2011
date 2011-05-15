import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class SwingFrame extends JFrame {
  //@Override 
  public SwingFrame (String title) {
    super(title);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setSize(300, 300);
    setLocation(20,20);
    setVisible(true);
  }

  public static void main (String[] args) {
    SwingFrame myframe = new SwingFrame("Test");
    
    JPanel panel = new JPanel(new FlowLayout()); // a panel
    // add JME canvas
    EngineContainer ec = new EngineContainer (640,480);
    panel.add (ec);
    panel.add(new JButton("Swing Component"));      // add some Swing
    
    myframe.add(panel);
    myframe.pack();
  }
} 

