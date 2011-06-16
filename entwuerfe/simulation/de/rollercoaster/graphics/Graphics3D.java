package de.rollercoaster.graphics;

import de.rollercoaster.mathematics.*;

import com.jme3.app.SimpleApplication;
import com.jme3.app.Application;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.texture.Texture;
import com.jme3.math.ColorRGBA;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.List;
import com.jme3.system.JmeContext.Type;
import com.jme3.scene.Spatial;
import com.jme3.asset.plugins.FileLocator;


//Ein bisschen Licht damit wir die Normalen auch bewundern können^^
import com.jme3.light.DirectionalLight;
import com.jme3.light.AmbientLight;
import de.rollercoaster.data.SerializedTrack;
import java.io.File;

//den windowlsitener gibt es vorerst damit 
public class Graphics3D extends SimpleApplication {

    public boolean pause = false;

    private double time = 0;
    private List<CurvePoint> points;

    private JmeCanvasContext ctx = null;

    public Graphics3D() {
        super();
        //simpleInitApp();
    }
    private boolean close = false;

    private Curve readCurve() {
         // Lade Probedatei
        SerializedTrack reader = new SerializedTrack(new File("examples/colossos.xml"));
        reader.read();
        
        return reader.getCurve();       
        // return new DummyCurve();
    }
    
    @Override
    public void simpleInitApp() {
        start(Type.Canvas);
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(20);  //mehr speed

        viewPort.setBackgroundColor(ColorRGBA.Blue);
        
        //Kurve erzeugen, Bahn erzeugen, Geometrieknote erzeugen
        Curve curve = readCurve();
        points = curve.getPointSequence(0.0,0.0); //für die spätere benutzung
        Achterbahn bahn = new Achterbahn(curve);
        Geometry geom_bahn = new Geometry("Bahn", bahn);

        //Materials für die Darstellung
        Material wireMaterial = new Material(assetManager, "/Common/MatDefs/Misc/WireColor.j3md");
        Material showNormalsMaterial = new Material(assetManager, "/Common/MatDefs/Misc/ShowNormals.j3md");
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  //ohne Licht
        Material mat2 = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  //mit Licht

        wireMaterial.setColor("Color", ColorRGBA.Blue);
        mat1.setColor("Color", ColorRGBA.Red);
        


        //Schnell zum Umschalten:
        //geom_bahn.setMaterial(wireMaterial);
       // geom_bahn.setMaterial(showNormalsMaterial);
        //geom_bahn.setMaterial(mat1);
        geom_bahn.setMaterial(mat2);


        rootNode.attachChild(geom_bahn);

        //Dummygelände
        assetManager.registerLocator("../models/",FileLocator.class.getName());  //Custom-Path einrichten

        //Spatial terrain = assetManager.loadModel("Terrain.mesh.xml");
        Spatial terrain = assetManager.loadModel("terrain2.mesh.xml");
        //terrain.setMaterial(showNormalsMaterial);

        terrain.scale(100,40,100);
        terrain.move(0,-15,0);
        rootNode.attachChild(terrain);


        
        //Mit dem Normalenshader hat Licht keine Wirkung und mit dem Lighted Shader sieht die Beleuchtung merkwürdig aus
           DirectionalLight sun = new DirectionalLight();
           sun.setDirection(new Vector3f(1,-2,0).normalizeLocal());
           sun.setColor(ColorRGBA.White);
           rootNode.addLight(sun);


          AmbientLight ambient = new AmbientLight();
          ambient.setColor(ColorRGBA.Blue);
          rootNode.addLight(ambient);


        //nichts kann uns aufhalten  (auch nicht der verlust des fokus)
        this.setPauseOnLostFocus(false);

    }

    @Override
    public void simpleUpdate(float tpf) {
        //Dies wird aufgerufen bevor ein Frame gerendert wird

        /*Ein bisschen Bewegung: hier wird immer wieder die Bahn entlang gefahren*/

        if (!pause) {time += tpf*3.0;}
        
        int behind = (int) time;
        int next = behind +1;

        float isnext = (float)time-behind;

        if (behind < 0) {behind = 0;}
        if (next < 0) {next = 0;}

        while (behind> points.size()-1) {behind -= points.size()-1;}
        while (next> points.size()-1) {next -= points.size()-1;}

        //System.out.printf ("<%d,%d>(%d)\n",behind,next,points.size());
        
				//setFrame nur mit  left, deshalb right spiegeln
				
        Vector3d pitch = points.get(behind).getPitchAxis().mult(1-isnext).add(points.get(next).getPitchAxis().mult(isnext)).mult(-1.0);
      				
        Vector3d yaw = points.get(behind).getYawAxis().mult(1-isnext).add(points.get(next).getYawAxis().mult(isnext));
        Vector3d roll =  points.get(behind).getRollAxis().mult(1-isnext).add(points.get(next).getRollAxis().mult(isnext));
        Vector3d loc = points.get(behind).getPosition().mult(1-isnext).add(points.get(next).getPosition().mult(isnext)).add(yaw.normalize().mult(5));


        this.getCamera().setFrame(loc.toF(),pitch.toF() ,yaw.toF() ,roll.toF());
        cam.update();
        
    }

    public JmeCanvasContext getCanvasObject() {
           AppSettings settings = new AppSettings(true);
           settings.setWidth(640);
           settings.setHeight(480);
           settings.setFrameRate(30);
 
           this.setSettings(settings);

        this.createCanvas(); // create canvas!
        ctx = (JmeCanvasContext) this.getContext();
        Dimension dim = new Dimension(640, 480);
        ctx.getCanvas().setPreferredSize(dim);
        // this.startCanvas(); // create canvas!
        
        

        return ctx;
    }

    //Bei closen unbedingt sowas bauen sonst wartet man ewig auf den lwjgl
    public void freeCanvas() {
        close = true;
    }
	
    //Setzt HUD-Daten zur Anzeige
    public void setHUDData(/*Insert data here*/) {
    }

    //Lädt (ggf nur eine Config-) Datei die die Deko enth#ält (oder auf sie verweist)
    public void loadDeko(String filename) {
    }

    //Bekommt eine Bahn und generiert das 3d Bahn object entsprechend
    public void setTrack() {
    }
}
