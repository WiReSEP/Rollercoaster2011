package de.rollercoaster.graphics;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.KeyInput;
import com.jme3.app.SimpleApplication;
import com.jme3.app.Application;
import com.jme3.material.Material;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.light.AmbientLight;



import java.io.File;
import java.util.logging.Logger;
import java.util.logging.Level;

import de.rollercoaster.graphics.Achterbahn;
import de.rollercoaster.mathematics.Curve;
import de.rollercoaster.data.SerializedTrack;


public class AchterbahnMock extends SimpleApplication {

 private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean keyPressed, float tpf) {
      if (name.equals("Continue") && !keyPressed) {
        mode++;
        switch (mode)  {

          case 1: System.out.println("DummyCurve, Normals mapped to material, box as joint, default pattern (includes no poles)");
                  bahn.removeFromParent();
                  bahn = new Achterbahn(new DummyCurve(), showNormalsMaterial,tmp ,null,null);
                  rootNode.attachChild(bahn);
                  break;

          case 2: System.out.println("DummyCurve, Normals mapped to material, box as joint, pattern und bounding_pattern from models directory (should include poles)");
                  bahn.removeFromParent();
                  bahn = new Achterbahn(new DummyCurve(), showNormalsMaterial,tmp ,"../models/pattern.obj","../models/bounding_pattern.obj");
                  rootNode.attachChild(bahn);
                  break;

          case 3: System.out.println("DummyCurve, Normals mapped to material, models/joint.mesh.xml as joint, pattern und bounding_pattern from models directory (should include poles)");
                  bahn.removeFromParent();
                  bahn = new Achterbahn(new DummyCurve(), showNormalsMaterial,joint ,"../models/pattern.obj","../models/bounding_pattern.obj");
                  rootNode.attachChild(bahn);
                  break;

          case 4: System.out.println("DummyCurve, Material as given in joint, models/joint.mesh.xml as joint, pattern und bounding_pattern from models directory (should include poles)");
                  bahn.removeFromParent();
                  DirectionalLight sun = new DirectionalLight();
                  Vector3f lightdirection = new Vector3f(0.7366f, -0.44128f, -0.512525f).normalize();
                  sun.setDirection(lightdirection);
                  sun.setColor(ColorRGBA.White.mult(1.0f));
                  rootNode.addLight(sun);
                  //Approcimation indirekter Beleuchtung 
                  AmbientLight ambient = new AmbientLight();
                  ambient.setColor(ColorRGBA.White.mult(0.9f));
                  rootNode.addLight(ambient);
                  bahn = new Achterbahn(new DummyCurve(), bahn_material,joint ,"../models/pattern.obj","../models/bounding_pattern.obj");
                  rootNode.attachChild(bahn);
                  break;

          case 5: System.out.println("ColossosRollercoaster from examples directory, Material as given in joint, models/joint.mesh.xml as joint, pattern und bounding_pattern from models directory (should include poles)");
                  bahn.removeFromParent();
                  bahn = new Achterbahn(testcurve, bahn_material,joint ,"../models/pattern.obj","../models/bounding_pattern.obj");
                  rootNode.attachChild(bahn);
                  break;

          default: System.exit(0);


        }
      }
    }
  };

private int mode = 0;
private Spatial bahn;
private Material showNormalsMaterial;
private Material bahn_material;
private Spatial joint;
private Curve testcurve;
private Node tmp;

public static void main (String[] args) {
  Logger.getLogger("com.jme3").setLevel(Level.SEVERE); //shut up stupid Monkey
  AchterbahnMock aTest = new AchterbahnMock();
  aTest.start();
}

@Override
public void simpleInitApp() {
  //Einstellungen Verstecken
  this.setShowSettings(false);
  //KameraController setzen
  flyCam.setDragToRotate(true);
  flyCam.setMoveSpeed(50);  
  //Pfad um eigene AssetsOrdner erweitern
  assetManager.registerLocator("../models/", FileLocator.class.getName());  //Custom-Path einrichten
  //Material erzeugen
  showNormalsMaterial = new Material(assetManager, "/Common/MatDefs/Misc/ShowNormals.j3md");

  //Joint laden
  joint = assetManager.loadModel("joint.mesh.xml");
  bahn_material = ((Geometry) ((Node) joint).getChild(0)).getMaterial();

  //zunächst eine Box in der Anzeige
  System.out.println ("DummyBox (just to show something)");
  bahn = new Geometry("test",new Box(Vector3f.ZERO,1,1,1));
  bahn.setMaterial(showNormalsMaterial);
  rootNode.attachChild(bahn);
  
  tmp = new Node();
  tmp.attachChild(new Geometry ("testbox",new Box(Vector3f.ZERO, 4, 2, 0.1f)));
  testcurve = (new SerializedTrack(new File("examples/colossos.xml"))).getCurve();

  //Userinput
  inputManager.addMapping("Continue", new KeyTrigger(KeyInput.KEY_SPACE));
  inputManager.addListener(actionListener, new String[]{"Continue"});


}

}