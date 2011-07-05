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

public class RollercoasterFrame extends JFrame implements ActionListener, ItemListener {
    
    String[] columnNames ={"","aktuell","Minimum","Maximum"};
    Object[] [] data = {
    {"Geschwindigkeit", new Integer(0),new Integer(0),new Integer(0)},
    {">Zeit", new Integer(0),new Integer(0),new Integer(0)},
    {">Beschl.", new Integer(0),new Integer(0),new Integer(0)},
    {">Winkel", new Integer(0),new Integer(0),new Integer(0)},
    {"-----------------------","","",""},
    {"Beschleunigung", new Integer(0),new Integer(0),new Integer(0)},
    {">Zeit", new Integer(0),new Integer(0),new Integer(0)},
    {">Geschw.", new Integer(0),new Integer(0),new Integer(0)},
    {">Winkel", new Integer(0),new Integer(0),new Integer(0)},
    };
    String[] cameras = { "Outer", "Inner"};
    private final Simulation sim;
    private final RollercoasterView graphics;
    private JFileChooser fc = new JFileChooser();
    private JPanel rightPanel = new JPanel();
    private JSplitPane sp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private JSplitPane sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private Graph graph = new Graph();
    private JTextArea log = new JTextArea("");
    private final JTable minMaxTable = new JTable(data, columnNames);
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
    private JMenuItem ansicht1 = new JCheckBoxMenuItem("Log andocken");
    private JMenuItem ansicht2 = new JCheckBoxMenuItem("HUD anzeigen");
    private JMenu ansicht3 = new JMenu("Kameraperspektive");
    private JRadioButtonMenuItem ansicht3a = new JRadioButtonMenuItem("Uebersicht",true);
    private JRadioButtonMenuItem ansicht3b = new JRadioButtonMenuItem("Kamerafahrt");
    
    private JMenu hilfe = new JMenu("Hilfe");
    private JMenuItem hilfe1 = new JMenuItem("Info");

    public RollercoasterFrame(String title, Simulation sim) {
      super(title);
      JPopupMenu.setDefaultLightWeightPopupEnabled(false);

      this.sim = sim;

      View view = sim.getView();
      this.graphics = (RollercoasterView) view;
      view.init();
      Canvas graphicsCanvas = view.getCanvas();
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
      graph.setPreferredSize(new Dimension(100,100));

      log.setEditable(false);
      log.setLineWrap(true);
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
      sp2.setTopComponent(new JScrollPane(minMaxTable));
      sp2.setBottomComponent(sp3);
      sp3.setDividerSize(8);
      sp3.setTopComponent(log);
      sp3.setBottomComponent(overview);
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

                  //TODO: hier Tabelle aktualisieren
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
      ansicht1.setSelected(true);
      ansicht2.addItemListener(this);
      ansicht2.setSelected(true);
      ansicht3a.addActionListener(this);
      ansicht3.add(ansicht3a);
      ansicht3b.addActionListener(this);
      ansicht3.add(ansicht3b);
      ansicht.add(ansicht1);
      ansicht.add(ansicht2);
      ansicht.add(ansicht3);
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
         for (int j=1; j < 4; j++) minMaxTable.setValueAt(new Integer(0),i,j);
       }

    }

        public void actionPerformed(ActionEvent e) {
            if ((e.getSource() == startButton)||(e.getSource() == sim1)) { //Simulation starten/pausieren
                if (!sim.isStarted()) {
                  //if sim.isStopped() reset();
                  sim.start();
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
                //TODO: menuitems setzen
                int cam = cameraBox.getSelectedIndex();
                if (cam == 0) {
                  graphics.setCameraMode(CameraMode.OVERVIEW);
                  ansicht3a.setSelected(true);
                  ansicht3b.setSelected(false);
                  log.append("Kamera auf Außenansicht gesetzt.\n");
                } else {
                  graphics.setCameraMode(CameraMode.INTERIOR);
                  ansicht3b.setSelected(true);
                  ansicht3a.setSelected(false);
                  log.append("Kamera auf Innenansicht gesetzt.\n");
                }
            } else if ((e.getSource() == ansicht3a) || (e.getSource() == ansicht3b)) { //Kameraperspektive
              //TODO: die buttons gehen noch nicht richtig
              if (ansicht3a.isSelected()) {
                graphics.setCameraMode(CameraMode.OVERVIEW);
                cameraBox.setSelectedIndex(0);
                log.append("Kamera auf Außenansicht gesetzt.\n");
              } else {
                graphics.setCameraMode(CameraMode.INTERIOR);
                cameraBox.setSelectedIndex(1);
                log.append("Kamera auf Innenansicht gesetzt.\n");
              }
            }
        }

        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() == ansicht1) {
                if (ansicht1.isSelected()) {

                } else {

                }
            }
        }
    

   /* class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            //log.setPreferredSize(new Dimension(rightPanel.getWidth() - 10, rightPanel.getHeight() / 3 - 5));
           // graph.setPreferredSize(new Dimension(rightPanel.getWidth() - 10, rightPanel.getHeight() / 3 - 5));
        }
    }*/

}
