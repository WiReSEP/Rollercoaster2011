package de.rollercoaster.gui;

import de.rollercoaster.graphics.View;
import java.awt.Canvas;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.Color.*;
import java.awt.geom.Rectangle2D;
import java.io.File;

public class RollercoasterFrame extends JFrame {
  private final View graphics;
  //private final JPanel panel;
  private JFileChooser fc = new JFileChooser();
  private JPanel jPanel1 = new JPanel(null);
    private JTextArea jTextArea1 = new JTextArea("");
  private JPanel jPanel2 = new JPanel(null);
    private JButton jButton1 = new JButton("Start");
    private JButton jButton2 = new JButton("Stop");
  private JMenuBar jmb = new JMenuBar();
    private JMenu datei = new JMenu("Datei");
      private JMenuItem datei1 = new JMenuItem("Konstruktion öffen");
      private JMenuItem datei2 = new JMenuItem("Konstruktion schließen");
      private JMenuItem datei3 = new JMenuItem("beenden");
    private JMenu simulation = new JMenu("Simulation");
      private JMenuItem sim1 = new JMenuItem("Simulation starten");
      private JMenuItem sim2 = new JMenuItem("Simulation stoppen");
      //private JMenuItem sim1 = new JMenuItem("Simulation starten");
    private JMenu ansicht = new JMenu("Ansicht");
      private JMenuItem ansicht1 = new JCheckBoxMenuItem("Log andocken.");
      //private JMenuItem ansicht2 = new JMenuItem("Simulation stoppen");
    private JMenu hilfe = new JMenu("Hilfe");
      private JMenuItem hilfe1 = new JMenuItem("Info");

  public RollercoasterFrame(String title, final View view) {
    super(title);

    this.graphics = view;
    //this.panel = new JPanel(new FlowLayout());
    view.init();
    Canvas graphicsCanvas = view.getCanvas();

    /*panel.add(graphicsCanvas);
    panel.add(new JButton("Swing Component"));      // add some Swing

        add(panel);
        pack();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    */
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                graphics.dispose();
            }
        });
    int frameWidth = 800;
    int frameHeight = 600;
    setSize(frameWidth, frameHeight);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (d.width - getSize().width) / 2;
    int y = (d.height - getSize().height) / 2;
    setLocation(x, y);
    setMinimumSize(new Dimension(800,600));
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    // Anfang Komponenten

    //graphicsCanvas.setPreferredSize(new Dimension(640,480));
    cp.add(graphicsCanvas,BorderLayout.WEST);

    jPanel1.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
    jPanel1.setLayout(new FlowLayout());
    ResizeListener rl = new ResizeListener();
    jPanel1.addComponentListener(rl);
    cp.add(jPanel1, BorderLayout.CENTER);

    jPanel2.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
    jPanel2.setLayout(new FlowLayout());
    cp.add(jPanel2, BorderLayout.SOUTH);

    ButtonListener bl = new ButtonListener();
    jButton1.setMargin(new Insets(2, 2, 2, 2));
    jButton1.addActionListener(bl);
    jPanel2.add(jButton1);

    jButton2.setMargin(new Insets(2, 2, 2, 2));
    jButton2.addActionListener(bl);
    jPanel2.add(jButton2);

    jTextArea1.setEditable(false);
    jTextArea1.setLineWrap(true);
    jTextArea1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
    jPanel1.add(jTextArea1);

    //Menü
    setJMenuBar(jmb);
    MenuListener ml = new MenuListener();
    jmb.add(datei);

    datei1.addActionListener(ml);
    datei.add(datei1);

    datei2.addActionListener(ml);
    datei.add(datei2);

    datei.addSeparator();

    datei3.addActionListener(ml);
    datei.add(datei3);

    jmb.add(simulation);

    sim1.addActionListener(ml);
    simulation.add(sim1);

    sim2.addActionListener(ml);
    simulation.add(sim2);

    jmb.add(ansicht);

    jmb.add(hilfe);

    hilfe1.addActionListener(ml);
    hilfe.add(hilfe1);

    ansicht1.addItemListener(ml);
    ansicht1.setSelected(true);// .isSelected()
    ansicht.add(ansicht1);


    // Ende Komponenten

    setResizable(true);
    setVisible(true);
  }

  class ButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == jButton1) { //Simulation starten
        JOptionPane.showMessageDialog(null, "Starte Simulation.");
        jTextArea1.append("Simulation gestartet.");
      } else if (e.getSource() == jButton2) { //Simulation stoppen
        JOptionPane.showMessageDialog(null, "Stoppe Simulation.");
        jTextArea1.append("Simulation gestoppt.");
      }
    }
  }

  class MenuListener implements ActionListener, ItemListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == datei1) { //Konstruktion laden
        if (fc.showOpenDialog(RollercoasterFrame.this)==JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          JOptionPane.showMessageDialog(RollercoasterFrame.this, "Lade Datei.");
          //This is where a real application would open the file.
        }
      } else if (e.getSource() == datei2) { //Konstruktion laden
        JOptionPane.showMessageDialog(RollercoasterFrame.this, "Datei schließen.");
      } else if (e.getSource() == datei3) { //beenden
        System.exit(0);
      }
    }

    public void itemStateChanged(ItemEvent e) {
      if (e.getSource() == ansicht1) {
        if (ansicht1.isSelected()) {
          /*try {
            jPanel1.remove(jTextArea1);
          }
          catch (Exception ex) {
            JOptionPane.showMessageDialog(gui.this, "konnte nicht entfernen. "+ex);
          }
          //if (jTextArea1.getRootPane() != null) jTextArea1.getRootPane().getContentPane().remove(jTextArea1);
          //jPanel1.add(jTextArea1);     */
          //JOptionPane.showMessageDialog(gui.this, "selected.");
        } else {
          //if (jTextArea1.getRootPane() != null) jTextArea1.getRootPane().getContentPane().remove(jTextArea1);
          //JFrame test = new JFrame();
          //test.getContentPane().add(jTextArea1, BorderLayout.CENTER);

          //JOptionPane.showMessageDialog(gui.this, "not selected.");
        }
      }
    }
  }


  class ResizeListener extends ComponentAdapter {
    public void componentResized(ComponentEvent e) {
      jTextArea1.setPreferredSize(new Dimension(jPanel1.getWidth()-10,jPanel1.getHeight()/3-5));
    }
  }
}
