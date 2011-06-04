package de.rollercoaster.mathematics;

import java.util.List;
import java.util.LinkedList;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

import java.lang.IllegalArgumentException;

/**Implementiert das Curveinterface mittels Dummydaten. Bis auf die für die 3DBahn notwendigen Anteile ist nichts implemntiert!*/

public class DummyCurve implements Curve{
  
    private List<CurvePoint> pointlist;
 
    public DummyCurve(List<CurvePoint> points) {
        this.pointlist = new ArrayList<CurvePoint>(points);
    }
    
    public DummyCurve () {
     Vector3f [] positions = new Vector3f[4];
     positions[0] = new Vector3f(-50f,0f,-50f);
     positions[1] = new Vector3f(50f,0f,-50f);
     positions[2] = new Vector3f(50f,0f,50f);
     positions[3] = new Vector3f(-50f,0f,50f);  

     Vector3f [] up = new Vector3f[4];
     up[0] = new Vector3f(0f,1f,0f);
     up[1] = new Vector3f(0f,1f,0f);
     up[2] = new Vector3f(0f,1f,0f);
     up[3] = new Vector3f(0f,1f,0f);  

     Vector3f [] front = new Vector3f[4];
     front[0] = new Vector3f(1f,0f,-1f);
     front[1] = new Vector3f(1f,0f,1f);
     front[2] = new Vector3f(-1f,0f,1f);
     front[3] = new Vector3f(-1f,0f,-1f);  

     Vector3f [] right = new Vector3f[4];
     right[0] = new Vector3f(-1f,0f,-1f);
     right[1] = new Vector3f(1f,0f,-1f);
     right[2] = new Vector3f(1f,0f,1f);
     right[3] = new Vector3f(-1f,0f,1f);  

      pointlist = new LinkedList<CurvePoint>();
      //    public DummyCurvePoint(Vector3f pos, Vector3f roll, Vector3f pitch, Vector3f yaw ) {
      pointlist.add(new DummyCurvePoint (positions[0],front[0],right[0],up[0]));
      pointlist.add(new DummyCurvePoint (positions[1],front[1],right[1],up[1]));
      pointlist.add(new DummyCurvePoint (positions[2],front[2],right[2],up[2]));
      pointlist.add(new DummyCurvePoint (positions[3],front[3],right[3],up[3]));
    }

    public double getLength() {
      System.err.println ("WARNING: >>DBBUGCODE<< in use");

      double length = 0;
      for (int i = 1; i < pointlist.size(); i++) {
        length+= pointlist.get(i).getPosition().subtract(pointlist.get(i-1).getPosition()).length();
      }
  
      return length;
    }
    public CurvePoint getPoint(double length) {
      System.err.println ("WARNING: >>DBBUGCODE<< in use");
      
      if (length> getLength()) 
        throw new IllegalArgumentException("Länge zu groß");




      double lengthcounter = 0;
      int i ;
      for (i= 1; i < pointlist.size() && lengthcounter < length; i++) {
//         System.out.printf ("%f von %f <%d>\n",lengthcounter,length,i);

        lengthcounter+= pointlist.get(i).getPosition().subtract(pointlist.get(i-1).getPosition()).length();
      }
      boolean uebertrag = false;

      if (i >= pointlist.size()) {
        uebertrag = true;
        
       }


          
      double lengthdiff_lastpoints = pointlist.get(uebertrag?i-1:i).getPosition().subtract(pointlist.get(uebertrag?i-2:i-1).getPosition()).length();
      lengthcounter-= lengthdiff_lastpoints;

      double lengthdiff_requested_ratio =  (length-lengthcounter)/lengthdiff_lastpoints;

      float isnext = (float) lengthdiff_requested_ratio;

      if (uebertrag)
        i=0;

      Vector3f pitch = pointlist.get(uebertrag?pointlist.size()-1:i-1).getPitchAxis();//.mult(1-isnext).add(pointlist.get(i).getPitchAxis().mult(isnext));

        //Spiegeln der pitch 
        //pitch.multLocal(-1.0f);
        
        Vector3f yaw = pointlist.get(uebertrag?pointlist.size()-1:i-1).getYawAxis().mult(1-isnext).add(pointlist.get(i).getYawAxis().mult(isnext));
        Vector3f roll =  pointlist.get(uebertrag?pointlist.size()-1:i-1).getRollAxis().mult(1-isnext).add(pointlist.get(i).getRollAxis().mult(isnext));
        Vector3f loc = pointlist.get(uebertrag?pointlist.size()-1:i-1).getPosition().add(pointlist.get(i).getPosition().subtract(pointlist.get(uebertrag?pointlist.size()-1:i-1).getPosition()).mult(isnext));

        //return new DummyCurvePoint (loc,new Vector3f(0.5f,0,0.5f).normalize(),new Vector3f(-0.5f,0,0.5f).normalize(),Vector3f.UNIT_Y);
        return new DummyCurvePoint (loc,roll.normalize(),pitch.normalize(),yaw.normalize());

    }
    public List<CurvePoint> getPointSequence(double maxDistance, double maxAngle) {
      return pointlist; 
    }
}
 
