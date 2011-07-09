package de.rollercoaster.graphics;

import de.rollercoaster.mathematics.*;

import java.util.List;
import java.util.LinkedList;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

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
     right[0] = new Vector3f(1f,0f,1f);
     right[1] = new Vector3f(-1f,0f,1f);
     right[2] = new Vector3f(-1f,0f,-1f);
     right[3] = new Vector3f(1f,0f,-1f);  

      pointlist = new LinkedList<CurvePoint>();
      //    public DummyCurvePoint(Vector3f pos, Vector3f roll, Vector3f pitch, Vector3f yaw ) {
      pointlist.add(new DummyCurvePoint (positions[0],front[0],right[0],up[0]));
      pointlist.add(new DummyCurvePoint (positions[1],front[1],right[1],up[1]));
      pointlist.add(new DummyCurvePoint (positions[2],front[2],right[2],up[2]));
      pointlist.add(new DummyCurvePoint (positions[3],front[3],right[3],up[3]));
    }

    public double getLength() {
      return 0.0;
    }
    public CurvePoint getPoint(double length) {
      return null;
    }
    public List<CurvePoint> getPointSequence(double maxDistance, double maxAngle) {
      return pointlist; 
    }

    @Override
    public Curve translate(Vector3d translation) {
        return null;
    }
}
 
