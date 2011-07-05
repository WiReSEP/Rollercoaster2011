package de.rollercoaster.gui;

import de.rollercoaster.data.SerializedTrack;
import de.rollercoaster.data.Track;
import de.rollercoaster.graphics.View;

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
    
    String[] columnNames ={"","Minima","Maxima"};
    Object[] [] data = {
    {"Geschwindigkeit", new Integer(1),new Integer(2)},
    {">Zeit", new Integer(1),new Integer(2)},
    {">Beschl.", new Integer(1),new Integer(2)},
    {">Winkel", new Integer(1),new Integer(2)},
    {"-----------------------","",""},
    {"Beschleunigung", new Integer(1),new Integer(2)},
    {">Zeit", new Integer(1),new Integer(2)},
    {">Geschw.", new Integer(1),new Integer(2)},
    {">Winkel", new Integer(1),new Integer(2)},
    };
    String[] cameras = { "Outer", "Inner"};
    private final Simulation sim;
    private final View graphics;
  //private final JPanel panel;
  private JFileChooser fc = new JFileChooser();
  private JPanel rightPanel = new JPanel();
    private JSplitPane sp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT); 
    private Graph graph = new Graph();
    private JTextArea log = new JTextArea("");
    private final JTable minMaxTable = new JTable(data, columnNames);
    //private final View overview;
    
    
    private JPanel bottomPanel = new JPanel(null);
    private JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");
    private JComboBox cameraBox = new JComboBox(cameras);
    
    private JMenuBar jmb = new JMenuBar();
    private JMenu datei = new JMenu("Datei");
    private JMenuItem datei1 = new JMenuItem("Konstruktion öffnen");
    private JMenuItem datei2 = new JMenuItem("Konstruktion schliessen");
    private JMenuItem datei3 = new JMenuItem("beenden");
    private JMenu simulation = new JMenu("Simulation");
    private JMenuItem sim1 = new JMenuItem("Simulation starten");
    private JMenuItem sim2 = new JMenuItem("Simulation stoppen");
    private JMenu ansicht = new JMenu("Ansicht");
    private JMenuItem ansicht1 = new JCheckBoxMenuItem("Log andocken");
		private JMenuItem ansicht2 = new JCheckBoxMenuItem("HUD anzeigen");
    private JMenu hilfe = new JMenu("Hilfe");
    private JMenuItem hilfe1 = new JMenuItem("Info");

		private boolean isRunning = false;

    public RollercoasterFrame(String title, Simulation sim) {
			super(title);
			javax.swing.JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			this.sim = sim;

        View view = sim.getView();
        this.graphics = view;
        //this.panel = new JPanel(new FlowLayout());
        view.init();
        Canvas graphicsCanvas = view.getCanvas();
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                graphics.dispose();
            }
        });
    int frameWidth = 800;
    int frameHeight = 600;
    //setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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

    rightPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
    rightPanel.setLayout(new BorderLayout());
    ResizeListener rl = new ResizeListener();
    rightPanel.addComponentListener(rl);
    cp.add(rightPanel, BorderLayout.CENTER);

    bottomPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
    bottomPanel.setLayout(new FlowLayout());
    cp.add(bottomPanel, BorderLayout.SOUTH);

    //ButtonListener bl = new ButtonListener();
    startButton.setMargin(new Insets(2, 2, 2, 2));
    startButton.addActionListener(this);
    bottomPanel.add(startButton);

    stopButton.setMargin(new Insets(2, 2, 2, 2));
    stopButton.addActionListener(this);
    bottomPanel.add(stopButton);
    
    cameraBox.addActionListener(this);
    bottomPanel.add(cameraBox);
    
    
        graph.setBounds(10, 10, 100, 100);
        graph.addCurve(100, 0, Color.yellow, "v");
        graph.addCurve(300, 0, Color.blue, "a");
        graph.setPreferredSize(new Dimension(100,100));

        //  new Thread(graph).start();
        sim.addObserver(new TrajectoryObserver() {

            private double lastTime = 0.0;

            @Override
            public void update(TrajectoryPoint newState) {
                if (newState.getTime() > lastTime + 1.0) {
                    lastTime = newState.getTime();

                    graph.addPoint(0, newState.getTime(), newState.getVelocity().length());
                    graph.addPoint(1, newState.getTime(), newState.getAcceleration().length());
                }

            }
        });
        
        sp1.setDividerSize(8);
      sp1.setDividerLocation(0.4);
      sp1.setTopComponent(graph);
      sp1.setBottomComponent(sp2);
        rightPanel.add(sp1, BorderLayout.CENTER);
        
        //rightPanel.add(graph, BorderLayout.NORTH);

  


        log.setEditable(false);
        log.setLineWrap(true);
        log.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        //rightPanel.add(log, BorderLayout.SOUTH);

//     minMaxTable.setBounds(8, 24, 128, 128);
        minMaxTable.setShowHorizontalLines(false);
        minMaxTable.setShowVerticalLines(false);
//    minMaxTable.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

        int maxWidth = 0;
        for (int i = 0; i < 9; i++) {
            Object cellValue = minMaxTable.getValueAt(i, 0);
            if (cellValue != null) {
                maxWidth = Math.max(minMaxTable.getCellRenderer(i, 0).getTableCellRendererComponent(minMaxTable, cellValue, false, false, i, 0).getPreferredSize().width + minMaxTable.getIntercellSpacing().width, maxWidth);
            }
        }
        minMaxTable.getColumnModel().getColumn(0).setMinWidth(maxWidth + 5);
        minMaxTable.getColumnModel().getColumn(0).setMaxWidth(maxWidth + 5);

			sp2.setDividerSize(8);
      sp2.setTopComponent(minMaxTable);
      sp2.setBottomComponent(log);
        rightPanel.add(sp1, BorderLayout.CENTER);
        //rightPanel.add(new JScrollPane(minMaxTable), BorderLayout.CENTER);


    //Menü
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
    ansicht1.setSelected(true);// .isSelected()
    ansicht2.addItemListener(this);
    ansicht2.setSelected(true);
    ansicht.add(ansicht1);
		ansicht.add(ansicht2);
        jmb.add(ansicht);

        jmb.add(hilfe);
        // Ende Komponenten

        setResizable(true);
        setVisible(true);
    }


        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == startButton) { //Simulation starten
                // JOptionPane.showMessageDialog(null, "Starte Simulation.");
								if (!isRunning) {
									sim.start();
									log.append("Simulation gestartet.\n");
								}
								isRunning = true;
            } else if (e.getSource() == stopButton) { //Simulation stoppen
                // JOptionPane.showMessageDialog(null, "Stoppe Simulation.");
								if (isRunning) {
									sim.stop();
									log.append("Simulation gestoppt.\n");
								}
								isRunning = false;
            } else if (e.getSource() == datei1) { //Konstruktion laden
                if (fc.showOpenDialog(RollercoasterFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //JOptionPane.showMessageDialog(RollercoasterFrame.this, "Lade Datei.");
                    //This is where a real application would open the file.
                    Track track = new SerializedTrack(file);
                    sim.setTrack(track);
                    log.append("Konstruktion "+file+" geladen.");
                }
            } else if (e.getSource() == datei2) { //Konstruktion schließen

                //JOptionPane.showMessageDialog(RollercoasterFrame.this, "Datei schliessen.");
                    sim.setTrack(null);
                    log.append("Konstruktion geschlossen.");
            } else if (e.getSource() == datei3) { //beenden
                System.exit(0);
            } else if (e.getSource() == cameraBox) { //Camera
                int cam = cameraBox.getSelectedIndex();
                if (cam == 0) {
									//setCameraMode('o');
									log.append("Kamera auf Außenansicht gesetzt.");
								} else {
									//setCameraMode('i');
									log.append("Kamera auf Innenansicht gesetzt.");
								}
            } else if (e.getSource() == sim1) { //Simulation starten
                // JOptionPane.showMessageDialog(null, "Starte Simulation.");
								if (!isRunning) {
									sim.start();
									log.append("Simulation gestartet.\n");
								}
								isRunning = true;
            } else if (e.getSource() == sim2) { //Simulation stoppen
                // JOptionPane.showMessageDialog(null, "Stoppe Simulation.");
								if (isRunning) {
									sim.stop();
									log.append("Simulation gestoppt.\n");
								}
								isRunning = false;
						} 
        }

        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() == ansicht1) {
                if (ansicht1.isSelected()) {
                    /*try {
                    rightPanel.remove(log);
                    }
                    catch (Exception ex) {
                    JOptionPane.showMessageDialog(gui.this, "konnte nicht entfernen. "+ex);
                    }
                    //if (log.getRootPane() != null) log.getRootPane().getContentPane().remove(log);
                    //rightPanel.add(log);     */
                    //JOptionPane.showMessageDialog(gui.this, "selected.");
                } else {
                    //if (log.getRootPane() != null) log.getRootPane().getContentPane().remove(log);
                    //JFrame test = new JFrame();
                    //test.getContentPane().add(log, BorderLayout.CENTER);
                    //JOptionPane.showMessageDialog(gui.this, "not selected.");
                }
            }
        }
    

    class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            //log.setPreferredSize(new Dimension(rightPanel.getWidth() - 10, rightPanel.getHeight() / 3 - 5));
           // graph.setPreferredSize(new Dimension(rightPanel.getWidth() - 10, rightPanel.getHeight() / 3 - 5));
        }
    }

}
