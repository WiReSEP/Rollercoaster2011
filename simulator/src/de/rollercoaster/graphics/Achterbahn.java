package de.rollercoaster.graphics;

//Fremdpakete
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.math.Vector3f;
import com.jme3.math.Matrix3f;

import com.jme3.texture.Texture;
import com.jme3.math.ColorRGBA;

import com.jme3.material.Material;

import com.jme3.scene.Spatial;
import com.jme3.scene.Mesh;

import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;

import java.util.List;
import com.jme3.collision.CollisionResults;

import com.jme3.math.Triangle;

import com.jme3.math.Ray;

import com.jme3.collision.CollisionResults;
import com.jme3.bullet.util.CollisionShapeFactory;

//Eigene Pakete
import de.rollercoaster.mathematics.*;
import de.rollercoaster.graphics.pattern.*;



public class Achterbahn extends Node {

  final static float MIN_JOINT_DISTANCE = 1;
  final static float MIN_POLE_DISTANCE = 20;

  //TODO: Anhand des Patterns ermitteln
  final static float POLE_UPPER_DIAMETER = 0.2f;
  final static float POLE_LOWER_DIAMETER = 2.5f;

  private Geometry geom_bahn;
  private Node joints;
  private Node poles;


  public Achterbahn(Curve curve, Material mat, Spatial joint3d ) {
      super(); //alles was Node kann

      List<CurvePoint> points = curve.getPointSequence(MIN_JOINT_DISTANCE/2,0.0);  //Auflösung ausreichend wählen
      

      PatternCurve bahn = null;
      PatternCurve collisiondomain = null;
      //Bahn Extrude erzeugen
      try {
        bahn = new PatternCurve(points, new FilePattern("../models/pattern.obj"));
        collisiondomain = new PatternCurve(points, new FilePattern("../models/bounding_pattern.obj"));
      }
      catch (Exception e) {
        System.out.println (e);
        e.printStackTrace();
        System.exit(-1);
      } 

      geom_bahn = new Geometry("curve_geom", bahn);
      geom_bahn.setMaterial(mat);

      Geometry bounding_bahn = new Geometry("collision_volume", collisiondomain);
      

      attachChild(geom_bahn);
      attachChild(bounding_bahn);  //nur debug kommt das in die anzeige
      bounding_bahn.setMaterial(mat);
      //Joints erzeugen
      joints = new Node("joints");
      this.attachChild(joints);

      //TODO: zu nahe Joints/Poles abfangen

      /*  Algorithmische Idee für die Joints:  
      - Approximation der Kurvenlänge durch aufsummieren der Abstände von zwei aufeinanderfolgenden Punkten
      - maxJoints = (int) approxlength/mmaxDistance;
      - alter algorithmus plus zähler damit der letzte nicht überflüssiger weise gesetzt wird

      Algorithmische Idee für die Poles
      - wie oben nur, dass y Achse nicht beachtet wird (die höhe gilt nicht als Abstand) 


      In beiden Fällen: Wird der Fehler an der Nahtstelle zu groß muss ggf die Konstante lockerer gehandhabt werden
*/

      double curvelength = points.get(0).getPosition().toF().subtract(points.get(points.size()-1).getPosition().toF()).length();
      //double groundcurvelength = getGroundDistance(points.get(0).getPosition().toF(),points.get(points.size()-1).getPosition().toF());
      for (int poscounter = 0; poscounter < points.size()-1; poscounter++) {
        curvelength += points.get(poscounter).getPosition().toF().subtract(points.get(poscounter+1).getPosition().toF()).length();
        //groundcurvelength += getGroundDistance(points.get(poscounter).getPosition().toF(),points.get(poscounter+1).getPosition().toF());
      }

      int jointCount = (int)(curvelength/MIN_JOINT_DISTANCE);
      double realJointDistance = curvelength/(jointCount);
      

         

      //*********************************************************************************//
      //***                         Joints einfügen                                   ***//
      //*********************************************************************************//
      //Die Punkteliste wird durchlaufen wobei der nächste Punkt für das Einfügen        //
      //eines Joints gewählt wird, der zum letzten Joint mindestens MIN_JOINT_DISTANCE   //
      //Abstand hält                                                                     //
      //*********************************************************************************//

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
        
          Geometry geom = new Geometry("Box"+poscounter, b);
          geom.setMaterial(mat);
          geom.setLocalTranslation(pos);
          Matrix3f matrix = new Matrix3f();
          //matrix.fromAxes(x.mult(-1),z,y);
          matrix.fromAxes(x.mult(-1),y,z);

          //System.out.printf ("Determinante (%d) %f [%s,%s,%s]\n",poscounter,matrix.determinant(),x.mult(-1),y,z);
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
          if ((poscounter != 0) && (getGroundDistance(points.get(poscounter).getPosition().toF(),points.get(lastposcounter).getPosition().toF()) < MIN_POLE_DISTANCE)) continue; //Abstandscheck

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

        if getGroundDistance(points.get(0).getPosition().toF(),points.get(lastposcounter).getPosition().toF()) < MIN_POLE_DISTANCE*0.6) {
          poles.getChild("pole"+lastposcounter).removeFromParent();
        }
        

  }


  private static double getGroundDistance(Vector3f v1, Vector3f v2) {
    Vector3f v1clone = v1.clone();
    Vector3f v2clone = v2.clone();
    v1clone.y = 0;
    v2clone.y = 0;
    return v1clone.subtract(v2clone).length();
  }

} 
