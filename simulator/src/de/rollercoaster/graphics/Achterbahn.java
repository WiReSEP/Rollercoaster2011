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



//Eigene Pakete
import de.rollercoaster.mathematics.*;
import de.rollercoaster.graphics.pattern.*;



public class Achterbahn extends Node {

  final static  int MIN_JOINT_DISTANCE = 5;
  final static  int MIN_POLE_DISTANCE = 35;

  private Geometry geom_bahn;
  private Node joints;
  private Node poles;

  public Achterbahn(Curve curve, Material mat, Spatial joint3d ) {
      super(); //alles was Node kann

      List<CurvePoint> points = curve.getPointSequence(0.0,0.0); 
      

      PatternCurve bahn = null;
      PatternCurve collisiondomain = null;
      //Bahn Extrude erzeugen
      try {
      bahn = new PatternCurve(curve, new FilePattern("../models/pattern.obj"));
      collisiondomain = new PatternCurve(curve, new FilePattern("../models/bounding_pattern.obj"));
      }
      catch (Exception e) {
        System.out.println (e);
        e.printStackTrace();
        System.exit(-1);
      } 

      geom_bahn = new Geometry("curve_geom", bahn);
      geom_bahn.setMaterial(mat);

      Geometry bounding_bahn = new Geometry("collision_volume", collisiondomain);
      bounding_bahn.setMaterial(mat);

      attachChild(geom_bahn);
      attachChild(bounding_bahn);  //nur debug kommt das in die anzeige

      //Joints erzeugen
      joints = new Node("joints");
      this.attachChild(joints);

      int joint_distance = 20;

      int number_of_joints = (int) (1.0*curve.getLength()/joint_distance);
      double actual_distance = 1.0*curve.getLength()/number_of_joints;


      System.out.printf ("DBGData: CurveLenght %f\n",curve.getLength());

      System.out.printf ("*****\n\n\nSpatial: %s\n*****\n\n\n", joint3d);


        // Joint auswählen 
        Mesh b = new Box(Vector3f.ZERO, 4, 2, 0.1f);                        // Debug joint
        //    Mesh b = ((Geometry)((Node)joint3d).getChild(0)).getMesh();   //designed Joint  
        int lastpostcounter = 0;

        for (int poscounter = 0; poscounter < points.size(); poscounter++) {
          if ((poscounter != 0) && (points.get(poscounter).getPosition().toF().subtract(points.get(lastpostcounter).getPosition().toF()).length() < MIN_JOINT_DISTANCE)) continue;
          lastpostcounter = poscounter;

          Vector3f pos = points.get(poscounter).getPosition().toF();
          Vector3f x = points.get(poscounter).getPitchAxis().normalize().toF();
          Vector3f y = points.get(poscounter).getYawAxis().normalize().toF();
          Vector3f z = points.get(poscounter).getRollAxis().normalize().toF();

          if ((x.cross(y).dot(z)) != 1) {
            System.out.println (""+x+","+y+","+z+" ist merkwürdig: "+(x.cross(y).dot(z)));
          }

          Geometry geom = new Geometry("Box"+poscounter, b);
          geom.setMaterial(mat);
          geom.setLocalTranslation(pos);
          Matrix3f matrix = new Matrix3f();
          matrix.fromAxes(x.mult(-1),y,z);
          geom.setLocalRotation(matrix);
          joints.attachChild(geom);
        }

        //Poles  (erste Versuche)

        Mesh p = new Cylinder(20,8,1.0f,6.0f,500.0f, true, false);
        
        lastpostcounter = 0;
        for (int poscounter = 0; poscounter < points.size(); poscounter++) {
          if ((poscounter != 0) && (points.get(poscounter).getPosition().toF().subtract(points.get(lastpostcounter).getPosition().toF()).length() < MIN_POLE_DISTANCE)) continue;
          

          Vector3f pos = points.get(poscounter).getPosition().toF();
          pos.y = pos.y-250-0.5f; //0.5f damit es nicht oben rausragt
          Geometry geom = new Geometry("pole"+poscounter, p);
          geom.setMaterial(mat);
          geom.setLocalTranslation(pos);
          Matrix3f matrix = new Matrix3f();
          matrix.fromAxes(Vector3f.UNIT_Y,Vector3f.UNIT_X,Vector3f.UNIT_Y); //?!
          geom.setLocalRotation(matrix);

          CollisionResults results= new CollisionResults();
//           int colls = bounding_bahn.collideWith(geom.getWorldBound(),results);
          int colls = bounding_bahn.collideWith(new Ray(points.get(poscounter).getPosition().toF(),Vector3f.UNIT_Z.mult(-1)),results);
          Triangle nonsense = new Triangle();
          boolean collision = false;

          
           for (int i = 0; i < results.size(); i++) {
             //System.out.printf ("[Collision %d] Pole %d collides with Triangle %d with Distance %f (concerns Triangle %s)\n",i,poscounter,results.getCollision(i).getTriangleIndex(),results.getCollision(i).getDistance(), results.getCollision(i).getTriangle(nonsense).getCenter());
             System.out.printf ("Colls-Hint %s \n",results.getCollision(i).getTriangle(nonsense).getCenter().subtract(points.get(poscounter).getPosition().toF()));
             if (results.getCollision(i).getTriangle(nonsense).getCenter().subtract(points.get(poscounter).getPosition().toF()).y<=0) {
                
//                 collision = true;
                break;
             }
           }
          System.out.printf ("[Pole %d] Collisions: %d \n", poscounter, colls);
          if (colls<=1) {
            this.attachChild(geom);
            lastpostcounter = poscounter;
          }
          else {
            System.out.printf ("Pole %d skipped due to Collision!\n",poscounter);
          }
         // break;
        }


  }


} 
