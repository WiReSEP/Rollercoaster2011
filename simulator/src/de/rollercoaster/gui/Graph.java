package de.rollercoaster.gui;

import java.awt.*;
import java.awt.Color.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.Vector;
import java.util.TreeMap;
import java.util.Map;
import java.util.ConcurrentModificationException;

public class Graph extends JPanel implements Runnable {
  class Curve {
    private double yMax; //größter darstellbarer Y-Wert
    private double yMin; //kleinster darstellbarer Y-Wert
    private Color color;  //Farbe der Kurve
    private String name; //Name der Kurve
    
    public TreeMap<Double,Double> points;
    
    public void addPoint(double x, double y) {
      points.put(x,y);
    }
    
    public void removePoint(double x) {
      points.remove(x);
    }
    
    public void clear() {
      points.clear();
    }
    
    public Curve(double yMax, double yMin, Color color, String name) {
      this.yMax = yMax;
      this.yMin = yMin;
      this.color = color;
      this.name = name;
      this.points = new TreeMap<Double,Double>();
    }
  }
  
  class GraphContent extends JPanel implements MouseListener, MouseMotionListener {
    private int mX;
    public GraphContent() {
      addMouseListener(this);
      addMouseMotionListener(this);
      this.setVisible(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D)g;
      int h = this.getHeight() - 1 - 11;
      int w = this.getWidth();
      g2d.drawLine(0,h,w,h);
      int i = 0;
      while (i < getWidth()) {
        String step = Integer.toString((int)Math.floor(i*stepDistance));
        g2d.drawString(step,i,h+11);
        i += SwingUtilities.computeStringWidth(g2d.getFontMetrics(), step)+10;
      }
      for (Curve c: curves) {
        g2d.setColor(c.color);
        Map.Entry<Double,Double> last = null;
        double scale = ((c.yMax-c.yMin)/h);
        try {
        for (Map.Entry<Double,Double> p: c.points.entrySet()) {
          if (last != null) {
            double fromx = last.getKey() * stepDistance;
            double fromy = h - (last.getValue()-c.yMin) / scale;
            double tox = p.getKey() * stepDistance;
            double toy = h - (p.getValue()-c.yMin) / scale;
            g2d.drawLine((int)Math.round(fromx),(int)Math.round(fromy),(int)Math.round(tox),(int)Math.round(toy));
          }
          last = p;
        }
        }catch (ConcurrentModificationException e) {}
      }
    }
    public void mousePressed(MouseEvent e) {
      mX = (int) e.getX();
    }

    public void mouseClicked(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseMoved(MouseEvent e) { }

    public void mouseDragged(MouseEvent e) {
      //System.out.println(getWidth()+" "+getParent().getWidth());
      if ((getX()-(mX-e.getX()) <= 0)&&(getX()-(mX-e.getX())+getWidth() >= getParent().getWidth()))
      setLocation(getX()-(mX-e.getX()), getY());
    }
  }

  private double stepDistance;   //! x-Wert sind stepDistance Pixel
  private Vector<Curve> curves;
  
  public Graph() {
    super();
    stepDistance = 2;
    curves =  new Vector<Curve>();

    final GraphContent g = new GraphContent();
    setLayout(null);
    add(new JPanel() {
      {
      setLayout(null);
      setBounds(20,0,20,20);
      //setBorder(new BevelBorder(BevelBorder.RAISED));
      add(g, null);
      };
    }, null);
    addComponentListener(new ComponentListener() {
      public void componentResized(ComponentEvent arg0) {
        if (getComponent(0) != null) {
           getComponent(0).setSize(getWidth()-20,getHeight());
        }
        
      }
      public void componentMoved(ComponentEvent arg0) {}
      public void componentShown(ComponentEvent arg0) {}
      public void componentHidden(ComponentEvent arg0) {}
    });
  }
  
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    int h = this.getHeight() - 1;
    int w = this.getWidth();
    int i=1;
    for (Curve c: curves) {
      g2d.setColor(c.color);
      g2d.drawString(Double.toString(c.yMax),0,0+11*i);
      g2d.drawString(Double.toString(c.yMin),0,h-11*i++);
    }
    g2d.setColor(Color.BLACK);
    g2d.drawLine(19,0,19,h-11);
  }
    
  public void addPoint(int curveID, double x, double y) {
    if ((curveID>=0)&&(curveID<curves.size())) {
      curves.elementAt(curveID).addPoint(x,y);
      ((Container)getComponent(0)).getComponent(0).setBounds(-((Container)getComponent(0)).getComponent(0).getWidth()+((Container)getComponent(0)).getWidth(),0,(int)Math.ceil(x*stepDistance),getHeight());
      repaint();
    }
    else throw new RuntimeException("Curve doesn't exist!");
  }
  
  public void addCurve(double yMax, double yMin, Color color, String name) {
    curves.add(new Curve(yMax,yMin,color,name));
  }
  
  public void removePoint(int curveID, double x) {
    if ((curveID>=0)&&(curveID<curves.size())) {
      curves.elementAt(curveID).removePoint(x);
      repaint();
    }
    else throw new RuntimeException("Curve doesn't exist!");
  }
  
  public void clearCurve(int curveID) {
    if ((curveID>=0)&&(curveID<curves.size())) {
      curves.elementAt(curveID).clear();
      repaint();
    }
    else throw new RuntimeException("Curve doesn't exist!");
  }
  
  public void removeCurve(int curveID) {
    if ((curveID>=0)&&(curveID<curves.size())) {
      curves.remove(curveID);
      repaint();
    }
    else throw new RuntimeException("Curve doesn't exist!");
  }
  
  public void print() {
    int i = 0;
    for (Curve c: curves) {
      System.out.println("Kurve " + (i++) + ": " + c.name + ", "+c.color + ", Werte: ");
      System.out.print(" ");
      for (Map.Entry<Double,Double> p: c.points.entrySet()) {
        System.out.print("("+p.getKey()+"; "+p.getValue()+") ");
      }
      System.out.println("");
    }
  }

  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(400,400);
    f.setLocation(200,200);
    f.setLayout(null);
    Graph g = new Graph();
    g.setBounds(10,10,100,100);
    g.addCurve(1,-1,Color.yellow,"test");
    g.addCurve(2,-.8,Color.blue,"test2");
    new Thread(g).start();
    f.getContentPane().add(g);
    f.setVisible(true);
  }
  
  public void run() {
    int x = 0;
    while (true) {
      addPoint(0,x++,Math.sin(x*0.1));
      addPoint(1,x++,Math.cos(x*0.2));
      try {Thread.sleep(300);} catch (Exception e) { }
    }
  }
  
}
