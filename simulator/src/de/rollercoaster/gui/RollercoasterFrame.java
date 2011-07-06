package de.rollercoaster.gui;

import de.rollercoaster.data.SerializedTrack;
import de.rollercoaster.data.Track;
import de.rollercoaster.graphics.View;
import de.rollercoaster.graphics.CameraMode;


//nur für die Präsentation wird dies importiert
import de.rollercoaster.graphics.RollercoasterView;
import de.rollercoaster.physics.TrajectoryObserver;
import de.rollercoaster.physics.TrajectoryPoint;
import de.rollercoaster.simulation.Simulation;


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
import javax.swing.UIManager.LookAndFeelInfo;
import java.io.FileNotFoundException;

public class RollercoasterFrame extends JFrame implements ActionListener, ItemListener {
    
    String[] columnNames ={"","aktuell","Minimum","Maximum"};
    Object[] [] data = {
    {"Geschwindigkeit", new Double(0),new Double(0),new Double(0)},
    {">Zeit", new Double(0),new Double(0),new Double(0)},
    {">Beschl.", new Double(0),new Double(0),new Double(0)},
    {">Winkel", new Double(0),new Double(0),new Double(0)},
    {"-----------------------","","",""},
    {"Beschleunigung", new Double(0),new Double(0),new Double(0)},
    {">Zeit", new Double(0),new Double(0),new Double(0)},
    {">Geschw.", new Double(0),new Double(0),new Double(0)},
    {">Winkel", new Double(0),new Double(0),new Double(0)},
    };
    String[] cameras = { "Uebersicht", "Kamerafahrt"};
    private final Simulation sim;
    private final RollercoasterView graphics;
    private JFileChooser fc = new JFileChooser();
    private JPanel rightPanel = new JPanel();
    private JSplitPane sp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private Graph graph = new Graph();
    private JTextArea log = new JTextArea("");
    private final JTable minMaxTable = new JTable(data, columnNames)  {
   public boolean isCellEditable(int x, int y) {
    return false;
   }};
    private JLabel overview = new JLabel("Uebersicht\n(Funktion noch nicht vorhanden)");
    
    private JPanel bottomPanel = new JPanel(null);
    private JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");
    private JComboBox cameraBox = new JComboBox(cameras);
    
    private JMenuBar jmb = new JMenuBar();
    private JMenu datei = new JMenu("Datei");
    private JMenuItem datei1 = new JMenuItem("Konstruktion oeffnen");
    private JMenuItem datei2 = new JMenuItem("Konstruktion schliessen");
    private JMenuItem datei3 = new JMenuItem("beenden");
    private JMenu simulation = new JMenu("Simulation");
    private JMenuItem sim1 = new JMenuItem("Simulation starten");
    private JMenuItem sim2 = new JMenuItem("Simulation stoppen");
    private JMenu ansicht = new JMenu("Ansicht");
    private JMenuItem ansicht1 = new JCheckBoxMenuItem("Log andocken",true);
    private JMenuItem ansicht2 = new JCheckBoxMenuItem("HUD anzeigen",true);
    private JMenu ansicht3 = new JMenu("Kameraperspektive");
    private JRadioButtonMenuItem ansicht3a = new JRadioButtonMenuItem("Uebersicht",true);
    private JRadioButtonMenuItem ansicht3b = new JRadioButtonMenuItem("Kamerafahrt");
    private JMenuItem ansicht4 = new JMenuItem("Deko laden");
    private JMenuItem ansicht5 = new JCheckBoxMenuItem("Deko anzeigen",true);
    private JMenuItem ansicht6 = new JMenuItem("Pattern laden");
    private JMenuItem ansicht7 = new JMenuItem("Boundingpattern laden");
    private JMenuItem ansicht9 = new JMenuItem("Jointverzeichnis setzen");
    private JMenuItem ansicht10 = new JCheckBoxMenuItem("Stuetzen anzeigen",true);
    
    
    private JMenu hilfe = new JMenu("Hilfe");
    private JMenuItem hilfe1 = new JMenuItem("Info");

    public RollercoasterFrame(String title, Simulation sim) {
      super(title);
      JPopupMenu.setDefaultLightWeightPopupEnabled(false);
      
      try {
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
          if ("Nimbus".equals(info.getName())) {
            UIManager.setLookAndFeel(info.getClassName());
            break;
          }
        }
      } catch (Exception e) {
        // If Nimbus is not available, you can set the GUI to another look and feel.
      }
      
      this.sim = sim;

      this.graphics = (RollercoasterView) sim.getView();
      graphics.init();
      Canvas graphicsCanvas = graphics.getCanvas();
      this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                graphics.dispose();
            }
      });
      int frameWidth = 800;
      int frameHeight = 600;
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      setSize(frameWidth, frameHeight);
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      int x = (d.width - getSize().width) / 2;
      int y = (d.height - getSize().height) / 2;
      setLocation(x, y);
      setMinimumSize(new Dimension(800,600));
      Container cp = getContentPane();
      cp.setLayout(new BorderLayout());
      // Anfang Komponenten

      cp.add(graphicsCanvas,BorderLayout.WEST);

      rightPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
      rightPanel.setLayout(new BorderLayout());
      //ResizeListener rl = new ResizeListener();
      //rightPanel.addComponentListener(rl);
      cp.add(rightPanel, BorderLayout.CENTER);

      bottomPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
      bottomPanel.setLayout(new FlowLayout());
      cp.add(bottomPanel, BorderLayout.SOUTH);

      /////////////////bottomPanel//////////////////////////////////////////////
      startButton.setMargin(new Insets(2, 2, 2, 2));
      startButton.addActionListener(this);
      bottomPanel.add(startButton);

      stopButton.setMargin(new Insets(2, 2, 2, 2));
      stopButton.addActionListener(this);
      bottomPanel.add(stopButton);

      cameraBox.addActionListener(this);
      cameraBox.setLightWeightPopupEnabled (false);
      bottomPanel.add(cameraBox);

      //////////////////////rightPanel//////////////////////////////////////////
      graph.setBounds(10, 10, 100, 100);
      graph.addCurve(100, 0, Color.yellow, "v");
      graph.addCurve(300, 0, Color.blue, "a");
      graph.setPreferredSize(new Dimension(100,200));

      log.setEditable(false);
      log.setLineWrap(true);
      log.setPreferredSize(new Dimension(100,100));
      log.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

      minMaxTable.setShowHorizontalLines(false);
      minMaxTable.setShowVerticalLines(false);
      int maxWidth = 0;
      for (int i = 0; i < 9; i++) {
          Object cellValue = minMaxTable.getValueAt(i, 0);
          if (cellValue != null) {
              maxWidth = Math.max(minMaxTable.getCellRenderer(i, 0).getTableCellRendererComponent(minMaxTable, cellValue, false, false, i, 0).getPreferredSize().width + minMaxTable.getIntercellSpacing().width, maxWidth);
          }
      }
      minMaxTable.getColumnModel().getColumn(0).setMinWidth(maxWidth + 5);
      minMaxTable.getColumnModel().getColumn(0).setMaxWidth(maxWidth + 5);

      sp1.setDividerSize(8);
      sp1.setDividerLocation(0.4);
      sp1.setTopComponent(graph);
      sp1.setBottomComponent(sp2);
      sp2.setDividerSize(8);
      sp2.setTopComponent(new JPanel() {
        {
          setLayout(new BorderLayout());
          add(new JScrollPane(minMaxTable) {
            {
              setPreferredSize(new Dimension(minMaxTable.getWidth(),minMaxTable.getRowHeight()*11));
            }
          }, BorderLayout.NORTH);
          add(new JScrollPane(log), BorderLayout.CENTER);
        }
      });
      sp2.setBottomComponent(overview);
      rightPanel.add(sp1, BorderLayout.CENTER);

      /////////////////////////////////Bewegung/////////////////////////////////
      //new Thread(graph).start();  //Demo
      sim.addObserver(new TrajectoryObserver() {

          private double lastTime = 0.0;

          @Override
          public void update(TrajectoryPoint newState) {
              if (newState.getTime() > lastTime + 1.0) {
                  lastTime = newState.getTime();

                  graph.addPoint(0, newState.getTime(), newState.getVelocity().length());
                  graph.addPoint(1, newState.getTime(), newState.getAcceleration().length());

                  minMaxTable.setValueAt(newState.getTime(), 1 ,1);
                  minMaxTable.setValueAt(newState.getVelocity().length(), 2, 1);
                  //minMaxTable.setValueAt(newState.get   Methode für den Winkel fehlt noch
                  minMaxTable.setValueAt(newState.getTime(), 6, 1);
                  minMaxTable.setValueAt(newState.getAcceleration().length(), 7, 1);
                  //minMaxTable.setValueAt(newState.get   Methode für den Winkel fehlt noch

                  if (newState.getVelocity().length() < ((Double)minMaxTable.getValueAt(2, 2))) {
                   minMaxTable.setValueAt(newState.getTime(), 1, 2);
                   minMaxTable.setValueAt(newState.getVelocity().length(), 3, 2);
                   //minMaxTable.setValueAt(newState.get   Methode für den Winkel fehlt noch
                  } else if (newState.getVelocity().length() > ((Double)minMaxTable.getValueAt(2, 3))) {
                   minMaxTable.setValueAt(newState.getTime(), 1 , 3);
                   minMaxTable.setValueAt(newState.getVelocity().length(), 3, 3);
                   //minMaxTable.setValueAt(newState.get   Methode für den Winkel fehlt noch
                   }
                   if (newState.getAcceleration().length() < ((Double)minMaxTable.getValueAt(7, 2))) {
                    minMaxTable.setValueAt(newState.getTime(), 6, 2);
                    minMaxTable.setValueAt(newState.getAcceleration().length(), 7, 2);
                    //minMaxTable.setValueAt(newState.get   Methode für den Winkel fehlt noch
                   } else if (newState.getAcceleration().length() > ((Double)minMaxTable.getValueAt(7, 3))) {
                    minMaxTable.setValueAt(newState.getTime(), 6 , 3);
                    minMaxTable.setValueAt(newState.getAcceleration().length(), 7, 3);
                    //minMaxTable.setValueAt(newState.get   Methode für den Winkel fehlt noch
                   }
              }

          }
      });

      /////////////////////////////////Menü/////////////////////////////////////
      setJMenuBar(jmb);
      jmb.add(datei);

      datei1.addActionListener(this);
      datei.add(datei1);

      datei2.addActionListener(this);
      datei.add(datei2);
      datei3.addActionListener(this);
      datei.add(datei3);
      datei.addSeparator();

      sim1.addActionListener(this);
      simulation.add(sim1);

      sim2.addActionListener(this);
      simulation.add(sim2);

      jmb.add(simulation);

      hilfe1.addActionListener(this);
      hilfe.add(hilfe1);

      ansicht1.addItemListener(this);
      ansicht2.addItemListener(this);
      ansicht5.addItemListener(this);
      ansicht10.addItemListener(this);
      ansicht3a.addActionListener(this);
      ansicht3b.addActionListener(this);
      ansicht4.addActionListener(this);
      ansicht6.addActionListener(this);
      ansicht7.addActionListener(this);
      //ansicht8.addActionListener(this);
      ansicht9.addActionListener(this);
      //ansicht5.setSelected(graphics.getShowStateDekoration());
      //ansicht10.setSelected(graphics.getShowStatePoles());
      ButtonGroup bgcam = new ButtonGroup();
      bgcam.add(ansicht3a);
      bgcam.add(ansicht3b);
      ansicht3.add(ansicht3a);
      ansicht3.add(ansicht3b);
      ansicht.add(ansicht1);
      ansicht.add(ansicht2);
      ansicht.add(ansicht3);
      ansicht.add(ansicht4);
      ansicht.add(ansicht5);
      ansicht.add(ansicht6);
      ansicht.add(ansicht7);
      //ansicht.add(ansicht8);
      ansicht.add(ansicht9);
      ansicht.add(ansicht10);
      jmb.add(ansicht);

      jmb.add(hilfe);
      // Ende Komponenten

      setResizable(true);
      setVisible(true);
    }

    public void reset() {
       startButton.setLabel("Start");
       sim1.setLabel("Simulation starten");
       graph.clearCurve(0);
       graph.clearCurve(1);
       for (int i=0; i <= 7; i++) {
         if (i==4) continue;
         for (int j=1; j < 4; j++) minMaxTable.setValueAt(new Double(0),i,j);
       }

    }
    
    public void setCam(CameraMode mode) {
      if (mode == CameraMode.OVERVIEW) {
        ansicht3a.setSelected(true);
        cameraBox.setSelectedIndex(0);
      } else {
        ansicht3b.setSelected(true);
        cameraBox.setSelectedIndex(1);
      }
      graphics.setCameraMode(mode);
    }

        public void actionPerformed(ActionEvent e) {
            if ((e.getSource() == startButton)||(e.getSource() == sim1)) { //Simulation starten/pausieren
                if (!sim.isStarted()) {
                  //if sim.isStopped() reset();
                  sim.start();
                  setCam(graphics.getCameraMode());
                  log.append("Simulation gestartet.\n");
                  startButton.setLabel("Pause");
                  sim1.setLabel("Simulation pausieren");
                } else {
                  sim.stop();
                  log.append("Simulation pausiert.\n");
                  startButton.setLabel("Start");
                  sim1.setLabel("Simulation starten");
                }
            } else if ((e.getSource() == stopButton)||(e.getSource() == sim2)) { //Simulation stoppen
                sim.reset();
                sim.stop(); //das soll weg
                reset(); //das soll hier raus
                log.append("Simulation gestoppt.\n");
            } else if (e.getSource() == datei1) { //Konstruktion laden
                if (fc.showOpenDialog(RollercoasterFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    Track track = new SerializedTrack(file);
                    sim.setTrack(track);
                    log.append("Konstruktion "+file+" geladen.");
                }
            } else if (e.getSource() == datei2) { //Konstruktion schließen
                    sim.setTrack(null);
                    log.append("Konstruktion geschlossen.\n");
            } else if (e.getSource() == datei3) { //beenden
                System.exit(0);
            } else if (e.getSource() == cameraBox) { //Camera
              int cam = cameraBox.getSelectedIndex();
              if (cam == 0) setCam(CameraMode.OVERVIEW);
              else setCam(CameraMode.INTERIOR);
            } else if ((e.getSource() == ansicht3a) || (e.getSource() == ansicht3b)) { //Kameraperspektive
              if (ansicht3a.isSelected()) setCam(CameraMode.OVERVIEW);
              else setCam(CameraMode.INTERIOR);
            } else if (e.getSource() == ansicht4) { //Deko laden
              if (fc.showOpenDialog(RollercoasterFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    graphics.loadDeko(file.toString());
                    log.append("Deko "+file+" geladen.");
                }
            } else if (e.getSource() == ansicht6) { //Pattern laden
              if (fc.showOpenDialog(RollercoasterFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                      graphics.setPattern(file.toString(),null);
                    } catch (FileNotFoundException f) {
                      log.append("Merkwürdigerweise ist die Datei nicht da");
                    }
                    log.append("Pattern "+file+" geladen.");
                }
            } else if (e.getSource() == ansicht7) { //Boundingpattern laden
              if (fc.showOpenDialog(RollercoasterFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                      graphics.setPattern(null,file.toString());
                    } catch (FileNotFoundException f) {
                      log.append("Merkwürdigerweise ist die Datei nicht da");
                    }
                    log.append("Boundingpattern "+file+" geladen.");
                }
            } else if (e.getSource() == ansicht9) {
              /*final JFrame frame = new JFrame("Lade Joint");
              frame.setSize(200,120);
              frame.setLayout(new BorderLayout());
              frame.add(new JLabel("relativen Pfad für Joints"),BorderLayout.NORTH);
              final JTextField txt = new JTextField();
              txt.setHorizontalAlignment(JTextField.CENTER);
              frame.add(txt,BorderLayout.CENTER);
              final JButton ok = new JButton("OK");
              ok.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                graphics.setJoint(txt.getText());
               }
              });
              frame.add(ok,BorderLayout.SOUTH);
              frame.setResizable(false);
              frame.setVisible(true);  */
              String s = (String)JOptionPane.showInputDialog(this, "relativen Pfad fuer Joints:\n", "Pfad Joints",
                                  JOptionPane.PLAIN_MESSAGE, null, null, "");
              if ((s != null) && (s.length() > 0)) {
                  graphics.setJoint(s);
              }

            }
        }

        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() == ansicht1) {
                if (ansicht1.isSelected()) {

                } else {

                }
            } else if (e.getSource() == ansicht5) {
              graphics.setShowStateDekoration(ansicht5.isSelected());
            } else if (e.getSource() == ansicht10) {
              graphics.setShowStatePoles(ansicht10.isSelected());
            }
        }
    

   /* class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            //log.setPreferredSize(new Dimension(rightPanel.getWidth() - 10, rightPanel.getHeight() / 3 - 5));
           // graph.setPreferredSize(new Dimension(rightPanel.getWidth() - 10, rightPanel.getHeight() / 3 - 5));
        }
    }*/

}
