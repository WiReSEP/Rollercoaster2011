package de.rollercoaster.gui;

import de.rollercoaster.data.SerializedTrack;
import de.rollercoaster.data.Track;
import de.rollercoaster.graphics.View;
import de.rollercoaster.graphics.CameraMode;


//nur f\u00dcr die Pr\u00c4sentation wird dies importiert
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
import javax.swing.table.DefaultTableModel;

public class RollercoasterFrame extends JFrame implements ActionListener, ItemListener {
  String[] columnNames = {"", "aktuell", "Minimum", "Maximum"};
  Object[][] data = {
    {"Geschwindigkeit", new Double(0), new Double(0), new Double(0)},
    {">Zeit", new Double(0), new Double(0), new Double(0)},
    {"-----------------------", "", "", ""},
    {"Beschleunigung", new Double(0), new Double(0), new Double(0)},
    {">Zeit", new Double(0), new Double(0), new Double(0)},
    {"-----------------------", "", "", ""},
    {">Winkel", new Double(0), new Double(0), new Double(0)},
    {">Zeit", new Double(0), new Double(0), new Double(0)}};
  String[] cameras = {"\u00dcbersicht", "Kamerafahrt"};
  private final Simulation sim;
  private final RollercoasterView graphics;
  private JFileChooser fc = new JFileChooser();
  private JPanel rightPanel = new JPanel();
  private JSplitPane sp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
  private JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
  private Graph graph = new Graph();
  private JTextArea log = new JTextArea("");
  private final JTable minMaxTable = new JTable(data, columnNames) {
    public boolean isCellEditable(int x, int y) {
      return false;
    }
  };
  private JLabel overview = new JLabel("\u00dcbersicht\n(Funktion noch nicht vorhanden)");
  private JPanel bottomPanel = new JPanel(null);
  private JButton startButton = new JButton("Start");
  private JButton stopButton = new JButton("Stop");
  private JComboBox cameraBox = new JComboBox(cameras);
  private JMenuBar jmb = new JMenuBar();
  private JMenu datei = new JMenu("Datei");
  private JMenuItem datei1 = new JMenuItem("Konstruktion  \u00F6ffnen");
  private JMenuItem datei2 = new JMenuItem("Konstruktion schliessen");
  private JMenuItem datei3 = new JMenuItem("Beenden");
  private JMenu simulation = new JMenu("Simulation");
  private JMenuItem sim1 = new JMenuItem("Simulation starten");
  private JMenuItem sim2 = new JMenuItem("Simulation stoppen");
  private JMenu ansicht = new JMenu("Ansicht");
  private JMenuItem ansicht1 = new JCheckBoxMenuItem("Log andocken", true);
  private JMenuItem ansicht11 = new JCheckBoxMenuItem("Graph andocken", true);
  private JMenuItem ansicht12 = new JCheckBoxMenuItem("Tabelle andocken", true);
  private JMenuItem ansicht13 = new JCheckBoxMenuItem("\u00dcbersicht andocken", true);
  private JMenuItem ansicht2 = new JCheckBoxMenuItem("HUD anzeigen", false);
  private JMenu ansicht3 = new JMenu("Kameraperspektive");
  private JRadioButtonMenuItem ansicht3a = new JRadioButtonMenuItem("\u00dcbersicht", true);
  private JRadioButtonMenuItem ansicht3b = new JRadioButtonMenuItem("Kamerafahrt");
  private JMenuItem ansicht4 = new JMenuItem("Deko laden");
  private JCheckBoxMenuItem ansicht5 = new JCheckBoxMenuItem("Deko anzeigen", true);
  private JMenuItem ansicht6 = new JMenuItem("Pattern laden");
  private JMenuItem ansicht7 = new JMenuItem("Boundingpattern laden");
  private JMenuItem ansicht9 = new JMenuItem("Jointverzeichnis setzen");
  private JCheckBoxMenuItem ansicht10 = new JCheckBoxMenuItem("St\u00fctzen anzeigen", true);
  private JMenuItem ansicht15 = new JMenuItem("Graph-Einstellungen");
  private JMenu ansicht16 = new JMenu("Look&Feel");
  private JMenu hilfe = new JMenu("Hilfe");
  private JMenuItem hilfe1 = new JMenuItem("Info");
  private JFrame logframe;
  private JFrame graphframe;
  private JFrame tabelleframe;
  private JFrame uebersichtframe;
  private JFrame grapheinstellungen;
  private boolean newMinMax;

  public RollercoasterFrame(String title, Simulation sim) {
    super(title);

    JPopupMenu.setDefaultLightWeightPopupEnabled(false);

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
    setMinimumSize(new Dimension(800, 600));
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    // Anfang Komponenten

    cp.add(graphicsCanvas, BorderLayout.WEST);

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
    cameraBox.setLightWeightPopupEnabled(false);
    bottomPanel.add(cameraBox);

    //////////////////////rightPanel//////////////////////////////////////////
    graph.setBounds(10, 10, 100, 100);
    graph.addCurve(100, 0, Color.yellow, "v");
    graph.addCurve(300, 0, Color.blue, "a");
    graph.setPreferredSize(new Dimension(100, 200));

    log.setEditable(false);
    log.setLineWrap(true);
    log.setPreferredSize(new Dimension(100, 100));
    log.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

    minMaxTable.setShowHorizontalLines(false);
    minMaxTable.setShowVerticalLines(false);
    int maxWidth = 0;
    for (int i = 0; i < 8; i++) {
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
            setPreferredSize(new Dimension(minMaxTable.getWidth(), minMaxTable.getRowHeight() * 11));
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
      public void update(TrajectoryPoint newState) throws NullPointerException {
        try {
        if (newState.getTime() > lastTime + 1.0) {
          lastTime = newState.getTime();

          int val = graph.getCurveID("v");
          if (val >= 0) {
            graph.addPoint(val, newState.getTime(), newState.getVelocity().length());
          }
          val = graph.getCurveID("a");
          if (val >= 0) {
            graph.addPoint(val, newState.getTime(), newState.getAcceleration().length());
          }
          val = graph.getCurveID("j");
          if (val >= 0) {
            graph.addPoint(val, newState.getTime(), newState.getJerk().length());
          }

          
          //aktuell
          minMaxTable.setValueAt(Math.round((newState.getVelocity().length())*100.)/100., 0, 1);
          minMaxTable.setValueAt(Math.round((newState.getTime())*100.)/100., 1, 1);
          minMaxTable.setValueAt(Math.round((newState.getAcceleration().length())*100.)/100., 3, 1);
          minMaxTable.setValueAt(Math.round((newState.getTime())*100.)/100., 4, 1);
//          minMaxTable.setValueAt(Math.round((newState.getJerk().length())*100.)/100., 6, 1);
//           minMaxTable.setValueAt(Math.round((newState.getTime())*100.)/100., 7, 1);
          
          

          //Geschwindigkeit
          if ((newState.getVelocity().length() < ((Double) minMaxTable.getValueAt(0, 2))) || (newMinMax)) {
            minMaxTable.setValueAt(Math.round((newState.getVelocity().length())*100.)/100., 0, 2);
            minMaxTable.setValueAt(Math.round((newState.getTime())*100.)/100., 1, 2);
          }
          if ((newState.getVelocity().length() > ((Double) minMaxTable.getValueAt(0, 3))) || (newMinMax)) {
            minMaxTable.setValueAt(Math.round((newState.getVelocity().length())*100.)/100., 0, 3);
            minMaxTable.setValueAt(Math.round((newState.getTime())*100.)/100., 1, 3);
          }

          //Beschleunigung
          if ((newState.getAcceleration().length() < ((Double) minMaxTable.getValueAt(3, 2))) || (newMinMax)) {
            minMaxTable.setValueAt(Math.round((newState.getAcceleration().length())*100.)/100., 3, 2);
            minMaxTable.setValueAt(Math.round((newState.getTime())*100.)/100., 4, 2);
          }
          if ((newState.getAcceleration().length() > ((Double) minMaxTable.getValueAt(3, 3))) || (newMinMax)) {
            minMaxTable.setValueAt(Math.round((newState.getAcceleration().length())*100.)/100., 3, 3);
            minMaxTable.setValueAt(Math.round((newState.getTime())*100.)/100., 4, 3);
          }
          
          //Winkel
//           if ((newState.getJerk().length() < ((Double)minMaxTable.getValueAt(6, 2))) || (newMinMax)) {
//            minMaxTable.setValueAt(Math.round((newState.getJerk().length())*100.)/100., 6, 2);
//            minMaxTable.setValueAt(Math.round((newState.getTime())*100.)/100., 7, 2);
//           }
//           if ((newState.getJerk().length() > ((Double) minMaxTable.getValueAt(6, 3))) || (newMinMax)) {
//            minMaxTable.setValueAt(Math.round((newState.getJerk().length())*100.)/100., 6, 3);
//            minMaxTable.setValueAt(Math.round((newState.getTime())*100.)/100., 7, 3);
//          }
          newMinMax = false;

          //HUD TODO:sollen wir das wirklich
          //graphics.setHUDData(newState.getVelocity(), newState.getAcceleration());
        }}catch (NullPointerException e){/*log.append("getJerk() "+newState.getJerk());*/}

      } 
    });

    /////////////////////////////////Men\u00dc/////////////////////////////////////
    setJMenuBar(jmb);
    jmb.add(datei);

    datei1.addActionListener(this);
    datei.add(datei1);

    datei2.addActionListener(this);
    datei.add(datei2);
    datei3.addActionListener(this);
    datei.addSeparator();
    datei.add(datei3);
    
    sim1.addActionListener(this);
    simulation.add(sim1);

    sim2.addActionListener(this);
    simulation.add(sim2);

    jmb.add(simulation);

    hilfe1.addActionListener(this);
    hilfe.add(hilfe1);

    ansicht1.addItemListener(this);
    ansicht11.addItemListener(this);
    ansicht12.addItemListener(this);
    ansicht13.addItemListener(this);
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
    ansicht15.addActionListener(this);
    //ansicht5.setSelected(graphics.getShowStateDekoration());
    //ansicht10.setSelected(graphics.getShowStatePoles());
    ButtonGroup bgcam = new ButtonGroup();
    bgcam.add(ansicht3a);
    bgcam.add(ansicht3b);
    ansicht3.add(ansicht3a);
    ansicht3.add(ansicht3b);
    ansicht.add(ansicht1);
    ansicht.add(ansicht11);
    ansicht.add(ansicht12);
    ansicht.add(ansicht13);
    ansicht.addSeparator();
    ansicht.add(ansicht2);
    ansicht.add(ansicht3);
    ansicht.add(ansicht4);
    ansicht.add(ansicht5);
    ansicht.add(ansicht6);
    ansicht.add(ansicht7);
    //ansicht.add(ansicht8);
    ansicht.add(ansicht9);
    ansicht.add(ansicht10);
    ansicht.addSeparator();
    ansicht.add(ansicht15);
    jmb.add(ansicht);

    jmb.add(hilfe);
    // Ende Komponenten

    setResizable(true);
    setVisible(true);

    ansicht.addSeparator();
    ansicht.add(ansicht16);

    try {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        ansicht16.add(new JMenuItem(info.getName()) {
          {
            addActionListener(RollercoasterFrame.this);
            setActionCommand("lookandfeel");
          }
        });
      }
    }
    catch (Exception e) {
    }
  }

  private void reset() {
    graph.clearCurve(0);
    graph.clearCurve(1);
    for (int i = 0; i < 8; i++) {
      if (i == 2 || i == 5) {
        continue;
      }
      for (int j = 1; j < 4; j++) {
//         if ((j == 1) && ((i == 2) || (i == 3) || (i == 7) || i == 8)) {
//           continue;
//         }
        minMaxTable.setValueAt(new Double(0), i, j);
      }
    }
    newMinMax = true;

  }
  
  private void stop() {
        startButton.setLabel("Start");
        sim1.setLabel("Simulation starten");
        sim.stop();
        newMinMax = true;

  }

  public void setCam(CameraMode mode) {
    if (mode == CameraMode.OVERVIEW) {
      ansicht3a.setSelected(true);
      cameraBox.setSelectedIndex(0);
    }
    else {
      ansicht3b.setSelected(true);
      cameraBox.setSelectedIndex(1);
    }
    graphics.setCameraMode(mode);
  }

  public void actionPerformed(ActionEvent e) {
    if ((e.getSource() == startButton) || (e.getSource() == sim1)) { //Simulation starten/pausieren
      if (!sim.isRunning()) {
        if (sim.isStopped()) {
          reset();
        }
        sim.start();
        setCam(graphics.getCameraMode());
        log.append("Simulation gestartet.\n");
        startButton.setLabel("Pause");
        sim1.setLabel("Simulation pausieren");
      }
      else {
        sim.pause();
        log.append("Simulation pausiert.\n");
        startButton.setLabel("Start");
        sim1.setLabel("Simulation starten");
      }
    }
    else if ((e.getSource() == stopButton) || (e.getSource() == sim2)) { //Simulation stoppen 
      if (!sim.isStopped()) {
        stop();
        log.append("Simulation gestoppt.\n");
      }
      else {
        reset();
        log.append("Simulation ist gestoppt. Daten zur\u00dccksetzen.\n");
      }
    }
    else if (e.getSource() == datei1) { //Konstruktion laden
      if (fc.showOpenDialog(RollercoasterFrame.this) == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        Track track = new SerializedTrack(file);
        if (!sim.isStopped()) {
          stop();
          reset();
        }
        sim.setTrack(track);
        log.append("Konstruktion " + file + " geladen.\n");
      }
    }
    else if (e.getSource() == datei2) { //Konstruktion schließen
      sim.setTrack(null);
      log.append("Konstruktion geschlossen.\n");
    }
    else if (e.getSource() == datei3) { //beenden
      System.exit(0);
    }
    else if (e.getSource() == cameraBox) { //Camera
      int cam = cameraBox.getSelectedIndex();
      if (cam == 0) {
        setCam(CameraMode.OVERVIEW);
      }
      else {
        setCam(CameraMode.INTERIOR);
      }
    }
    else if ((e.getSource() == ansicht3a) || (e.getSource() == ansicht3b)) { //Kameraperspektive
      if (ansicht3a.isSelected()) {
        setCam(CameraMode.OVERVIEW);
      }
      else {
        setCam(CameraMode.INTERIOR);
      }
    }
    else if (e.getSource() == ansicht4) { //Deko laden
      if (fc.showOpenDialog(RollercoasterFrame.this) == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        graphics.loadDeko(file.toString());
        log.append("Deko " + file + " geladen.");
      }
    }
    else if (e.getActionCommand().equals("lookandfeel")) { //Look&Feel
      try {
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
          if (((AbstractButton) e.getSource()).getLabel().equals(info.getName())) {
            UIManager.setLookAndFeel(info.getClassName());
            break;
          }
        }
      }
      catch (Exception f) {
      }

      SwingUtilities.updateComponentTreeUI(this);
      pack();
    }
    else if (e.getSource() == ansicht6) { //Pattern laden
      if (fc.showOpenDialog(RollercoasterFrame.this) == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        try {
          graphics.setPattern(file.toString());
        }
        catch (FileNotFoundException f) {
          log.append("Merkw\u00dcrdigerweise ist die Datei nicht da");
        }
        log.append("Pattern " + file + " geladen.");
      }
    }
    else if (e.getSource() == ansicht7) { //Boundingpattern laden
      if (fc.showOpenDialog(RollercoasterFrame.this) == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        try {
          graphics.setBoundingPattern(file.toString());
        }
        catch (FileNotFoundException f) {
          log.append("Merkw\u00dcrdigerweise ist die Datei nicht da");
        }
        log.append("Boundingpattern " + file + " geladen.");
      }
    }
    else if (e.getSource() == ansicht9) {
      /*final JFrame frame = new JFrame("Lade Joint");
      frame.setSize(200,120);
      frame.setLayout(new BorderLayout());
      frame.add(new JLabel("relativen Pfad f\u00dcr Joints"),BorderLayout.NORTH);
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
      String s = (String) JOptionPane.showInputDialog(this, "relativen Pfad fuer Joints:\n", "Pfad Joints",
              JOptionPane.PLAIN_MESSAGE, null, null, "");
      if ((s != null) && (s.length() > 0)) {
        graphics.setJoint(s);
      }

    }
    else if (e.getSource() == ansicht15) {
      //TODO:layout
      grapheinstellungen = new JFrame("Grapheinstellungen");
      grapheinstellungen.setSize(520, 300);
      grapheinstellungen.setLayout(new FlowLayout());
      JPanel kurven = new JPanel(new GridLayout(0, 3, 10, 5));
      kurven.add(new JLabel("Geschwindigkeit"));
      kurven.add(new JLabel("Beschleunigung"));
      kurven.add(new JLabel("Ruck"));
      final JCheckBox anz1 = new JCheckBox("anzeigen", graph.getCurveID("v") >= 0);
      final JCheckBox anz2 = new JCheckBox("anzeigen", graph.getCurveID("a") >= 0);
      final JCheckBox anz3 = new JCheckBox("anzeigen", graph.getCurveID("j") >= 0);
      kurven.add(anz1);
      kurven.add(anz2);
      kurven.add(anz3);
      final JButton farb1 = new JButton("Farbe");
      if (graph.getCurveID("v") >= 0) {
        farb1.setBackground(graph.getColor(graph.getCurveID("v")));
      }
      farb1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Color newColor = JColorChooser.showDialog(RollercoasterFrame.this, "Farbe f\u00dcr Geschwindigkeit", farb1.getBackground());
          if (newColor != null) {
            farb1.setBackground(newColor);
          }
        }
      });
      final JButton farb2 = new JButton("Farbe");
      final JButton farb3 = new JButton("Farbe");
      kurven.add(farb1);
      kurven.add(farb2);
      kurven.add(farb3);
      double val = 0;
      if (graph.getCurveID("v") >= 0) {
        val = graph.getYMin(graph.getCurveID("v"));
      }
      final JSpinner min1 = new JSpinner(new SpinnerNumberModel(val, -10000, 10000, 10));
      if (graph.getCurveID("a") >= 0) {
        val = graph.getYMin(graph.getCurveID("a"));
      }
      final JSpinner min2 = new JSpinner(new SpinnerNumberModel(val, -10000, 10000, 10));
      if (graph.getCurveID("j") >= 0) {
        val = graph.getYMin(graph.getCurveID("j"));
      }
      final JSpinner min3 = new JSpinner(new SpinnerNumberModel(val, -10000, 10000, 10));
      kurven.add(new JPanel(new FlowLayout()) {
        {
          add(new JLabel("y Min."));
          add(min1);
        }
      });
      kurven.add(new JPanel(new FlowLayout()) {
        {
          add(new JLabel("y Min."));
          add(min2);
        }
      });
      kurven.add(new JPanel(new FlowLayout()) {
        {
          add(new JLabel("y Min."));
          add(min3);
        }
      });
      if (graph.getCurveID("v") >= 0) {
        val = graph.getYMax(graph.getCurveID("v"));
      }
      final JSpinner max1 = new JSpinner(new SpinnerNumberModel(val, -10000, 10000, 10));
      if (graph.getCurveID("a") >= 0) {
        val = graph.getYMax(graph.getCurveID("a"));
      }
      final JSpinner max2 = new JSpinner(new SpinnerNumberModel(val, -10000, 10000, 10));
      if (graph.getCurveID("j") >= 0) {
        val = graph.getYMax(graph.getCurveID("j"));
      }
      final JSpinner max3 = new JSpinner(new SpinnerNumberModel(val, -10000, 10000, 10));
      kurven.add(new JPanel(new FlowLayout()) {
        {
          add(new JLabel("y Max."));
          add(max1);
        }
      });
      kurven.add(new JPanel(new FlowLayout()) {
        {
          add(new JLabel("y Max."));
          add(max2);
        }
      });
      kurven.add(new JPanel(new FlowLayout()) {
        {
          add(new JLabel("y Max."));
          add(max3);
        }
      });
      grapheinstellungen.add(kurven);
      final JSpinner breite = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
      //grapheinstellungen.add(breite);
      final JSpinner step = new JSpinner(new SpinnerNumberModel(graph.getStepDistance(), 0.1, 100, 0.1));
      grapheinstellungen.add(new JPanel(new FlowLayout()) {
        {
          add(new JLabel("Schrittweite"));
          add(step);
        }
      });
      final JCheckBox names = new JCheckBox("Namen anzeigen", false);
      //grapheinstellungen.add(names);

      final JButton ok = new JButton("OK");
      ok.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          int id = graph.getCurveID("v");
          if (id >= 0) {
            if (!anz1.isSelected()) {
              graph.removeCurve(id);
            }
            else {
              graph.setYMax(id, (Double) max1.getValue());
              graph.setYMin(id, (Double) min1.getValue());
              graph.setColor(id, farb1.getBackground());
            }
          }
          else {
            if (anz1.isSelected()) {
              graph.addCurve((Double) max1.getValue(), (Double) min1.getValue(), farb1.getBackground(), "v");
            }
          }
          id = graph.getCurveID("a");
          if (id >= 0) {
            if (!anz2.isSelected()) {
              graph.removeCurve(id);
            }
            else {
              graph.setYMax(id, (Double) max2.getValue());
              graph.setYMin(id, (Double) min2.getValue());
              graph.setColor(id, farb2.getBackground());
            }
          }
          else {
            if (anz2.isSelected()) {
              graph.addCurve((Double) max2.getValue(), (Double) min2.getValue(), farb2.getBackground(), "a");
            }
          }
          id = graph.getCurveID("j");
          if (id >= 0) {
            if (!anz3.isSelected()) {
              graph.removeCurve(id);
            }
            else {
              graph.setYMax(id, (Double) max3.getValue());
              graph.setYMin(id, (Double) min3.getValue());
              graph.setColor(id, farb3.getBackground());
            }
          }
          else {
            if (anz3.isSelected()) {
              graph.addCurve((Double) max3.getValue(), (Double) min3.getValue(), farb3.getBackground(), "j");
            }
          }
          graph.setStepDistance((Double) step.getValue());
          grapheinstellungen.dispose();
        }
      });
      grapheinstellungen.add(ok);

      final JButton abb = new JButton("Abbrechen");
      abb.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          grapheinstellungen.dispose();
        }
      });
      grapheinstellungen.add(abb);
      grapheinstellungen.setResizable(false);
      grapheinstellungen.setVisible(true);

    }
  }

  public void itemStateChanged(ItemEvent e) {
    if (e.getSource() == ansicht1) {
      JScrollPane box = (JScrollPane) log.getParent().getParent();
      if (ansicht1.isSelected()) {
        logframe.remove(box);
        ((Container) sp2.getTopComponent()).add(box, BorderLayout.CENTER);
        sp2.repaint();
        logframe.dispose();
      }
      else {
        box.getParent().remove(box);
        logframe = new JFrame("Log");
        logframe.setSize(400, 400);
        logframe.setLayout(new BorderLayout());
        logframe.add(box, BorderLayout.CENTER);
        logframe.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            ansicht1.doClick();
          }
        });
        logframe.setResizable(true);
        logframe.setVisible(true);
        sp2.repaint();
      }
    }
    else if (e.getSource() == ansicht11) { //Graph andocken
      if (ansicht11.isSelected()) {
        graphframe.remove(graph);
        sp1.setTopComponent(graph);
        sp1.repaint();
        graphframe.dispose();
      }
      else {
        sp1.setTopComponent(null);
        graphframe = new JFrame("Graph");
        graphframe.setSize(400, 400);
        graphframe.setLayout(new BorderLayout());
        graphframe.add(graph, BorderLayout.CENTER);
        graphframe.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            ansicht11.doClick();
          }
        });
        graphframe.setResizable(true);
        graphframe.setVisible(true);
        sp2.repaint();
      }
    }
    else if (e.getSource() == ansicht12) { //Tabelle andocken
      JScrollPane box = (JScrollPane) minMaxTable.getParent().getParent();
      if (ansicht12.isSelected()) {
        tabelleframe.remove(minMaxTable);
        ((Container) sp2.getTopComponent()).add(box, BorderLayout.NORTH);
        sp2.repaint();
        tabelleframe.dispose();
      }
      else {
        box.getParent().remove(box);
        tabelleframe = new JFrame("Tabelle");
        tabelleframe.setSize(400, 400);
        tabelleframe.setLayout(new BorderLayout());
        tabelleframe.add(box, BorderLayout.CENTER);
        tabelleframe.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            ansicht12.doClick();
          }
        });
        tabelleframe.setResizable(true);
        tabelleframe.setVisible(true);
        sp2.repaint();
      }
    }
    else if (e.getSource() == ansicht13) {  //Uebersicht andocken
      if (ansicht13.isSelected()) {
        uebersichtframe.remove(overview);
        sp2.setBottomComponent(overview);
        sp2.repaint();
        uebersichtframe.dispose();
      }
      else {
        sp2.setBottomComponent(null);
        uebersichtframe = new JFrame("Uebersicht");
        uebersichtframe.setSize(400, 400);
        uebersichtframe.setLayout(new BorderLayout());
        uebersichtframe.add(overview, BorderLayout.CENTER);
        uebersichtframe.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            ansicht13.doClick();
          }
        });
        uebersichtframe.setResizable(true);
        uebersichtframe.setVisible(true);
        sp2.repaint();
      }
    }
    else if (e.getSource() == ansicht5) {
      graphics.setShowStateDekoration(ansicht5.isSelected());
    }
    else if (e.getSource() == ansicht10) {
      graphics.setShowStatePoles(ansicht10.isSelected());
    }
    else if (e.getSource() == ansicht2) {
      graphics.setEnableHUD(ansicht2.isSelected());
    }
  }
  /* class ResizeListener extends ComponentAdapter {
  public void componentResized(ComponentEvent e) {
  //log.setPreferredSize(new Dimension(rightPanel.getWidth() - 10, rightPanel.getHeight() / 3 - 5));
  // graph.setPreferredSize(new Dimension(rightPanel.getWidth() - 10, rightPanel.getHeight() / 3 - 5));
  }
  }*/
}
