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


import java.util.List;


//Eigene Pakete
import de.rollercoaster.mathematics.*;



public class Achterbahn extends Node {

  private Geometry geom_bahn;
  private Node joints;
  private Node poles;

  public Achterbahn(Curve curve, Material mat, Spatial joint3d ) {
      super(); //alles was Node kann


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

      //Mesh myobj = ((Geometry)((Node)joint3d).getChild(0)).getMesh();

      for (int i = 0; i < number_of_joints-1; i++) {
           CurvePoint point = curve.getPoint(actual_distance*i);
           Node node = ((Node)joint3d).clone(false);
           node.setLocalTranslation(point.getPosition());
           Matrix3f rot = new Matrix3f();
           rot.fromAxes(point.getPitchAxis().mult(1),point.getYawAxis().mult(1),point.getRollAxis()); //sollte eigentlich so klappen
           node.setLocalRotation(rot);
           joints.attachChild(node);
          
      }

      attachChild(joints);
      
   

        



  }


} 
