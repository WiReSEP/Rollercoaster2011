package de.rollercoaster.graphics;

//Fremdpakete
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.math.Vector3f;
import com.jme3.math.Matrix3f;


import com.jme3.material.Material;

import com.jme3.scene.Spatial;
import com.jme3.scene.Mesh;

import com.jme3.scene.shape.Cylinder;

import java.util.List;


import com.jme3.math.Ray;

import com.jme3.collision.CollisionResults;

//Eigene Pakete
import de.rollercoaster.mathematics.*;
import de.rollercoaster.graphics.pattern.*;



public class Achterbahn extends Node {

  final static float MIN_JOINT_DISTANCE = 1;
  final static float MIN_POLE_DISTANCE = 20;

  final static float POLE_UPPER_DIAMETER = 0.2f;
  final static float POLE_LOWER_DIAMETER = 2.5f;

//   private Geometry geom_bahn;
  private Node joints;
  private Node poles;

  private Node bounding_bahn = new Node ("bounding_vol");

  private Pattern patt =  null;
  private Pattern bounding_patt =  null;


  public Achterbahn(Curve curve, Material mat, Spatial joint3d , String pattern_filename, String bounding_pattern_filename) {
      super("achterbahn"); //alles was Node kann

      //Pattern vorbereiten
      try {
        patt = (pattern_filename!= null)? new FilePattern(pattern_filename): new SimplePattern();
        bounding_patt = (bounding_pattern_filename!= null)? new FilePattern(bounding_pattern_filename): new SimplePattern();
      }
      catch (Exception e) {
        throw new IllegalArgumentException ("At least one part of the initial Rollercoasterdata is invalid");
      }

      //Punkteliste erzeugen
      List<CurvePoint> points = curve.getPointSequence(0.1*MIN_JOINT_DISTANCE,0.0);  //Auflösung ausreichend wählen

      
      int maxpoints = (1<<16)/patt.getVertexCount() -2; //maximale anzahl an Stützstellen in einem FBO

      if (maxpoints < 2)
        throw new IllegalArgumentException ("Unable to Display Pattern. Pattern has too much detail");

      //System.out.printf ("<<<DEBUG>>>MaxPoints calculated as %d, doing %d Displaylists (Points %d)\n",maxpoints, points.size()/maxpoints-1, points.size() );

      boolean at_end  = false; //sind wir am ende der punkteliste angekommen
      int counter = 0;
      List<CurvePoint> points_sublist = null;

      /* Wir erzeugen Patterncurves immer so, dass das FBO fast voll ist. Dann wird das nächste angehängt. Wenn wir am ende der Hauptpunkteliste ankommen darf die Schleife enden.
        Um den Kreis zu schließen muss der Startpunkt in die letzte Liste eingefügt werden. Da nur referenzen kopiert werden muss diese entfernt werden. Da die Schleife nur dann beendet wenn 
        mindestens einmal die bool Variable umgesetzt wurde und somit auch der Punkt extra eingefügt wurde kann einfach immer im Anschluss entfernt werden*/

      while (!at_end) {
        //Bahn Extrude vorbereiten
        PatternCurve bahn = null;
        PatternCurve collisiondomain = null;

        //unterliste erzeugen
        if ((counter+1)*(maxpoints+1) < points.size()) {
           points_sublist = points.subList(counter*(maxpoints+1),(counter+1)*(maxpoints+1)+1); 
        }
        else {
          points_sublist = points.subList(counter*(maxpoints+1),points.size()); 
          points_sublist.add(points.get(0));
          at_end = true;
        }

        //Extrudeobjekt erzeugen
        bahn = new PatternCurve(points_sublist,patt);
        collisiondomain = new PatternCurve(points_sublist,bounding_patt);
        Geometry geom_bahn = new Geometry("curve_geom"+counter, bahn);
        geom_bahn.setMaterial(mat);

        Geometry bounding_bahn_geom = new Geometry("collision_volume", collisiondomain);

        //in den Scenegraphen einhängen
        bounding_bahn.attachChild(bounding_bahn_geom);
        attachChild(geom_bahn);
        counter++;
      }

      points_sublist.remove(points_sublist.size()-1); //das eingefügte wieder entfernen
      

      //*********************************************************************************//
      //***                         Joints einfügen                                   ***//
      //*********************************************************************************//
      //Die Punkteliste wird durchlaufen wobei der nächste Punkt für das Einfügen        //
      //eines Joints gewählt wird, der zum letzten Joint mindestens MIN_JOINT_DISTANCE   //
      //Abstand hält                                                                     //
      //*********************************************************************************//      

     //Joints erzeugen
      joints = new Node("joints");
      this.attachChild(joints);

      double curvelength = points.get(0).getPosition().toF().subtract(points.get(points.size()-1).getPosition().toF()).length();
      for (int poscounter = 0; poscounter < points.size()-1; poscounter++) {
        curvelength += points.get(poscounter).getPosition().toF().subtract(points.get(poscounter+1).getPosition().toF()).length();
      }

      int jointCount = (int)(curvelength/MIN_JOINT_DISTANCE);
      double realJointDistance = curvelength/(jointCount);
      

        //Mesh b = new Box(Vector3f.ZERO, 4, 2, 0.1f);                        // Debug joint
        Mesh b = ((Geometry)((Node)joint3d).getChild(0)).getMesh();   //designed Joint  
        int lastposcounter = 0;

        for (int poscounter = 0; poscounter < points.size(); poscounter++) {
          if ((poscounter != 0) && (points.get(poscounter).getPosition().toF().subtract(points.get(lastposcounter).getPosition().toF()).length() < realJointDistance)) continue;
          lastposcounter = poscounter;

          Vector3f pos = points.get(poscounter).getPosition().toF();
          Vector3f x = points.get(poscounter).getPitchAxis().normalize().toF();
          Vector3f y = points.get(poscounter).getYawAxis().normalize().toF();
          Vector3f z = points.get(poscounter).getRollAxis().normalize().toF();
        
          Geometry geom = new Geometry("joint"+poscounter, b);
          geom.setMaterial(mat);
          geom.setLocalTranslation(pos);
          Matrix3f matrix = new Matrix3f();
          //matrix.fromAxes(x.mult(-1),z,y);
          matrix.fromAxes(x.mult(-1),y,z);

//        System.out.printf ("Determinante (%d) %f [%s,%s,%s]\n",poscounter,matrix.determinant(),x.mult(-1),y,z); 
          geom.setLocalRotation(matrix);
          joints.attachChild(geom);
          
        }


      //*********************************************************************************//
      //***                          Poles einfügen                                   ***//
      //*********************************************************************************//
      //Die Punkteliste wird durchlaufen wobei der nächste Punkt für das Einfügen        //
      //eines Poles gewählt wird, der zum letzten Pole mindestens MIN_POLE_DISTANCE      //
      //Abstand hält und der keine Kollision mit der Bahn aufweist.                      //
      //*********************************************************************************//


        //Poles  (erste Versuche)
        //Joints erzeugen
        poles = new Node("poles");
        this.attachChild(poles);



        //Das ist vorerst unser Pole (ein einfacher Zylinder der unten ein bisschen dicker ist als oben)
        // Der Zylinder ist sehr lang damit man nicht skalieren muss um ihn unter die Bahn zu setzen
        Mesh p = new Cylinder(20,8,POLE_UPPER_DIAMETER,POLE_LOWER_DIAMETER,500.0f, true, false);

        
        lastposcounter = 0;// wir merken uns wann das letzte mal ein Pole gesetzt wurde    
        for (int poscounter = 0; poscounter < points.size(); poscounter++) {
          if ((poscounter == 0) || (getGroundDistance(points.get(poscounter).getPosition().toF(),points.get(lastposcounter).getPosition().toF()) < MIN_POLE_DISTANCE) || //Abstandscheck
            (projection(points.get(poscounter).getYawAxis().normalize().toF(),Vector3f.UNIT_Y) < 0))  //Wenn die Bahn nach unten gedreht ist
            continue; 

          //CollisionCheck: (an 4 Ecken der BoundingBox wird gesampelt)
          CollisionResults results= new CollisionResults();

          int colls = 0;
          //4Samples:
          colls += bounding_bahn.collideWith(new Ray(points.get(poscounter).getPosition().toF().add(Vector3f.UNIT_X.mult( 1.0f*POLE_UPPER_DIAMETER/2.0f)),Vector3f.UNIT_Y.mult(-1)),results);
          colls += bounding_bahn.collideWith(new Ray(points.get(poscounter).getPosition().toF().add(Vector3f.UNIT_X.mult(-1.0f*POLE_UPPER_DIAMETER/2.0f)),Vector3f.UNIT_Y.mult(-1)),results);
          colls += bounding_bahn.collideWith(new Ray(points.get(poscounter).getPosition().toF().add(Vector3f.UNIT_Z.mult( 1.0f*POLE_UPPER_DIAMETER/2.0f)),Vector3f.UNIT_Y.mult(-1)),results);
          colls += bounding_bahn.collideWith(new Ray(points.get(poscounter).getPosition().toF().add(Vector3f.UNIT_Z.mult(-1.0f*POLE_UPPER_DIAMETER/2.0f)),Vector3f.UNIT_Y.mult(-1)),results);
          

          //System.out.printf ("[Pole %d] Collisions: %d \n", poscounter, colls);
          if (colls==4) { //min 4 Kollisionen entstehen, da wir in der aktuellen Position starten
            Vector3f pos = points.get(poscounter).getPosition().toF();   
            pos.y = pos.y-250-0.05f; //0.05f damit es nicht oben rausragt
            Geometry geom = new Geometry("pole"+poscounter, p);
            geom.setMaterial(mat);
            geom.setLocalTranslation(pos);
            Matrix3f matrix = new Matrix3f();
            matrix.fromAxes(Vector3f.UNIT_Z,Vector3f.UNIT_X,Vector3f.UNIT_Y); //?!
            geom.setLocalRotation(matrix);

            poles.attachChild(geom);
            lastposcounter = poscounter;
          }
        }

        //Bis 0.6 Abstandsmaß geht in Ordnung (empirischer Wert)

        if (getGroundDistance(points.get(0).getPosition().toF(),points.get(lastposcounter).getPosition().toF()) < MIN_POLE_DISTANCE*0.6) {
          Spatial tmp = poles.getChild("pole"+lastposcounter);
          if (tmp != null )
            tmp.removeFromParent();
        }
        

  }


  private static double getGroundDistance(Vector3f v1, Vector3f v2) {
    Vector3f v1clone = v1.clone();
    Vector3f v2clone = v2.clone();
    v1clone.y = 0;
    v2clone.y = 0;
    return v1clone.subtract(v2clone).length();
  }

  private static double projection (Vector3f onto, Vector3f vec) {
    return onto.dot(vec)/onto.lengthSquared();
  }

} 
