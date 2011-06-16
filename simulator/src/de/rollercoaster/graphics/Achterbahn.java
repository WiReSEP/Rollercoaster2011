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

import java.util.List;


//Eigene Pakete
import de.rollercoaster.mathematics.*;



public class Achterbahn extends Node {

  private Geometry geom_bahn;
  private Node joints;
  private Node poles;

  public Achterbahn(Curve curve, Material mat, Spatial joint3d ) {
      super(); //alles was Node kann

      List<CurvePoint> points = curve.getPointSequence(0.0,0.0); 

      //Bahn Extrude erzeugen
      PatternCurve bahn = new PatternCurve(curve);

      geom_bahn = new Geometry("curve_geom", bahn);
      geom_bahn.setMaterial(mat);

      attachChild(geom_bahn);


      //Joints erzeugen
      joints = new Node("joints");

      int joint_distance = 20;

      int number_of_joints = (int) (1.0*curve.getLength()/joint_distance);
      double actual_distance = 1.0*curve.getLength()/number_of_joints;


      System.out.printf ("DBGData: CurveLenght %f\n",curve.getLength());

      System.out.printf ("*****\n\n\nSpatial: %s\n*****\n\n\n", joint3d);

        // Joint auswählen 
        Mesh b = new Box(Vector3f.ZERO, 4, 2, 0.1f);                        // Debug joint
        //    Mesh b = ((Geometry)((Node)joint3d).getChild(0)).getMesh();   //designed Joint  
        for (int poscounter = 0; poscounter < points.size(); poscounter++) {
          Vector3f pos = points.get(poscounter).getPosition().toF();
          Vector3f x = points.get(poscounter).getPitchAxis().normalize().toF();
          Vector3f y = points.get(poscounter).getYawAxis().normalize().toF();
          Vector3f z = points.get(poscounter).getRollAxis().normalize().toF();
          Geometry geom = new Geometry("Box"+poscounter, b);
          geom.setMaterial(mat);
          geom.setLocalTranslation(pos);
          Matrix3f matrix = new Matrix3f();
          matrix.fromAxes(x.mult(-1),y,z);
          geom.setLocalRotation(matrix);
          this.attachChild(geom);
        }


  }


} 
