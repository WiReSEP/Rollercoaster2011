package de.rollercoaster.graphics;

import de.rollercoaster.mathematics.*;
import de.rollercoaster.graphics.pattern.*;

import com.jme3.app.SimpleApplication;
import com.jme3.app.Application;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.math.Matrix3f;

import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.texture.Texture;
import com.jme3.math.ColorRGBA;
import java.awt.Dimension;
//import java.awt.event.*;
import java.util.List;
import com.jme3.system.JmeContext.Type;
import com.jme3.scene.Spatial;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;

import com.jme3.util.SkyFactory;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;



import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.LightScatteringFilter;



//Ein bisschen Licht damit wir die Normalen auch bewundern können^^
import com.jme3.light.DirectionalLight;
import com.jme3.light.AmbientLight;
import de.rollercoaster.data.SerializedTrack;
import java.io.File;


public class Graphics3D extends SimpleApplication {
    private int counter = 0;




    private ActionListener actionListener = new ActionListener() {
      private int pos = 0;
      public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals("wire") && !keyPressed ) {
          rootNode.getChild("collision_volume").setMaterial(wireMaterial);
        }
        if (name.equals("full") && !keyPressed) {
          rootNode.getChild("collision_volume").setMaterial(showNormalsMaterial);
        }

        if (name.equals("up") && !keyPressed ) {
          Spatial node;
          pos++;
          while (((node=rootNode.getChild("pole"+pos))== null) && pos < 20000) {pos++;}
          System.out.printf ("Selected Pole %d ...",pos);
          if (node != null) node.setMaterial(redMat);
          int lastpos = pos-1; 
          while (((node=rootNode.getChild("pole"+lastpos))== null) && lastpos > -10) {lastpos--;}
          if (node != null) node.setMaterial(showNormalsMaterial);
          System.out.printf ("done \n");
        }
     
        if (name.equals("down") && !keyPressed ) {
          Spatial node;
          pos--;
          while (((node=rootNode.getChild("pole"+pos))== null) && pos > -10) {pos--;}
          if (node != null) node.setMaterial(redMat);
          int lastpos = pos+1; 
          while (((node=rootNode.getChild("pole"+lastpos))== null) && lastpos < 20000) {lastpos++;}
          if (node != null) node.setMaterial(showNormalsMaterial);
        }
      }
    };


    public boolean pause = false;

        private Material wireMaterial ;
        private Material showNormalsMaterial;
        private Material redMat;


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

      //*********************************************************************************//
      //***                      Einstellungen an der Engine                          ***//
      //*********************************************************************************//
      // Die Einstellungen die für unsere Zwecke angepasst werden müssen werden in dieer //
      // Sektion getätigt                                                                //
      //*********************************************************************************//

        //lwjgl für ein AWT Canvas Objekt vorbereiten
        start(Type.Canvas);  //TODO:  Prüfen ob diese Zeile hier wirklich so reingehört

        //Debug-FlyByCamera-Controler einrichten
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(50);  //mehr speed

        //Cameraeinstellungen
        cam.setFrustumFar(20000);  //Farclipping ein bisschen erhöhen (damit unsere Landschaft bleibt
        viewPort.setBackgroundColor(ColorRGBA.Red); //Hintergrundfarbe setzen (für Debug auf auffällige Farbe setzen damit Lücken sichtbar werden; Release auf Schwarz oä)

        //nichts kann uns aufhalten  (auch nicht der verlust des fokus)
        this.setPauseOnLostFocus(false);

      //*********************************************************************************//
      //***                         Assets einrichten                                 ***//
      //*********************************************************************************//
      // In dieser Sektion werden notwendige Daten geladen die Inhalt oder Art der       //
      // Darstellung beeinflussen. Im wesentlichen werden die Quellpfade gesetzt und     //
      // Materialien, Objekte und Texturen geladen                                       //
      //*********************************************************************************//

        //Pfad um eigene AssetsOrdner erweitern
        assetManager.registerLocator("../models/",FileLocator.class.getName());  //Custom-Path einrichten

        //Materialien setzen
        wireMaterial = new Material(assetManager, "/Common/MatDefs/Misc/WireColor.j3md");
        wireMaterial.setColor("Color", ColorRGBA.Yellow);        
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  //ohne Licht
        mat1.setColor("Color", ColorRGBA.Red);        

        //Debug-Materialien[DEBUG]
        showNormalsMaterial = new Material(assetManager, "/Common/MatDefs/Misc/ShowNormals.j3md");
        redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  //ohne Licht
        redMat.setColor("Color", ColorRGBA.Red);
        
        //Skybox
        rootNode.attachChild(SkyFactory.createSky(assetManager,
          assetManager.loadTexture("skybox/west.png"), //west
          assetManager.loadTexture("skybox/east.png"), //east
          assetManager.loadTexture("skybox/north.png"), //north
          assetManager.loadTexture("skybox/south.png"), //south
          assetManager.loadTexture("skybox/up.png"), //up
          assetManager.loadTexture("skybox/down.png") //down
        )); 

        //Joint laden
        Spatial joint = assetManager.loadModel("joint.mesh.xml");
        
        //Gizzmo laden [DEBUG]
        Spatial gizzmo = assetManager.loadModel("Cylinder.mesh.xml");
        gizzmo.scale (20);
        rootNode.attachChild(gizzmo);

        //Gelände laden
        Spatial terrain = assetManager.loadModel("Grid.001.mesh.xml");
        terrain.setCullHint(Spatial.CullHint.Never); //nie verstecken
        terrain.scale(10,6,10);
        terrain.move(0,-15,100);
        rootNode.attachChild(terrain);

      //*********************************************************************************//
      //***                             Licht und Schatten                            ***//
      //*********************************************************************************//
      // Einrichtung der Lichtquellen und des Schattenwurfs                              //
      //*********************************************************************************//
        //Sonne 
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1,-2,0).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        //Approcimation indirekter Beleuchtung 
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);

        //Schattenwurf
        terrain.setShadowMode(ShadowMode.CastAndReceive);
        bahn.setShadowMode(ShadowMode.CastAndReceive);

      //*********************************************************************************//
      //***           Kurvendaten erhalten und Bah erzeugen                           ***//
      //*********************************************************************************//
      // In dieser Sektion werden die Kurvendaten geholt und das Achterbahnobjekt        //
      // dynamisch erzeugt                                                               //
      //*********************************************************************************//
        
        //Kurve erzeugen, Bahn erzeugen, Geometrieknote erzeugen
        Curve curve = readCurve();
        points = curve.getPointSequence(0.0,0.0); //für die spätere benutzung
        Achterbahn bahn = new Achterbahn(curve,showNormalsMaterial,joint);
        rootNode.attachChild(bahn); 

        

 

      //*********************************************************************************//
      //***                   Userinteraktion                                         ***//
      //*********************************************************************************//
      // Tastaturevents abfangen und umleiten                                            //
      //*********************************************************************************//

        inputManager.addMapping("up",  new KeyTrigger(KeyInput.KEY_ADD));
        inputManager.addMapping("down",  new KeyTrigger(KeyInput.KEY_SUBTRACT));
        inputManager.addMapping("wire",  new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping("full",  new KeyTrigger(KeyInput.KEY_N));

        inputManager.addListener(actionListener, new String[]{"up","down","wire","full"});

      //*********************************************************************************//
      //***                             GUI                                           ***//
      //*********************************************************************************//
      // Einrichtung der HUD Anzeigekomponente                                           //
      //*********************************************************************************//

        //TODO: HUD einfügen 


      //*********************************************************************************//
      //***                 Cameracontroler                                           ***//
      //*********************************************************************************//
      // Einrichtung des Cameracontroler zur Unterstützung unterschiedlicher Modi        //
      //*********************************************************************************//

        //TODO: Cameracontroler einfügen

      //*********************************************************************************//
      //*********************************************************************************//
    }

    @Override
    public void simpleUpdate(float tpf) {

//        if (!pause) { time+=tpf;}
//         
//         if (time > 1) {
//           geom[counter].removeFromParent();
//           rootNode.attachChild(geom[++counter]);
//           time -=1;
//         }

        //Dies wird aufgerufen bevor ein Frame gerendert wird

        /*Ein bisschen Bewegung: hier wird immer wieder die Bahn entlang gefahren*/

   /*    if (!pause) {time += tpf*12.0;}
        
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
        cam.update();//*/
        
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
