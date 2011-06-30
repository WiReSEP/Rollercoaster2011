package de.rollercoaster.graphics;
//eigene Packete
import de.rollercoaster.mathematics.*;
import de.rollercoaster.graphics.pattern.*;
import de.rollercoaster.data.SerializedTrack;
import de.rollercoaster.data.Track;

//Fremdpackete (JMonkey)
import com.jme3.app.SimpleApplication;
import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.system.JmeContext.Type;

import com.jme3.material.Material;

import com.jme3.math.Vector3f;
import com.jme3.math.Matrix3f;
import com.jme3.math.ColorRGBA;

import com.jme3.scene.Spatial;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import com.jme3.light.DirectionalLight;
import com.jme3.light.AmbientLight;

import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.shadow.BasicShadowRenderer;

import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.LightScatteringFilter;

import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.util.SkyFactory;

//Fremdpackete (java)
import java.util.List;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.File;

public class Graphics3D extends SimpleApplication {



    private int counter = 0;




    private ActionListener actionListener = new ActionListener() {
      private int pos = 0;
      public void onAction(String name, boolean keyPressed, float tpf) {
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
        Material bahn_material = ((Geometry)((Node)joint).getChild(0)).getMaterial();
        
        //Gizzmo laden [DEBUG]
        Spatial gizzmo = assetManager.loadModel("gizzmo.mesh.xml");
        gizzmo.scale (20);
        rootNode.attachChild(gizzmo);

        //Gelände laden
        Spatial terrain = assetManager.loadModel("terrain.mesh.xml");
        terrain.setCullHint(Spatial.CullHint.Never); //nie verstecken
        terrain.scale(10,6,10);
        terrain.move(0,-15,100);
        rootNode.attachChild(terrain);
        terrain.setShadowMode(ShadowMode.Receive);  //Schattenwurf

        //GeländeMaterial laden
        Material mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
         
            /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
            mat_terrain.setTexture("m_Alpha",
                       assetManager.loadTexture("terrain_alpha.png"));
         
            /** 1.2) Add GRASS texture into the red layer (m_Tex1). */
            Texture grass = assetManager.loadTexture("grass.jpg");
            grass.setWrap(WrapMode.Repeat);
            mat_terrain.setTexture("m_Tex1", grass);
            mat_terrain.setFloat("m_Tex1Scale", 64f);
         
            /** 1.3) Add DIRT texture into the green layer (m_Tex2) */
            Texture dirt = assetManager.loadTexture("dirt.jpg");
            dirt.setWrap(WrapMode.Repeat);
            mat_terrain.setTexture("m_Tex2", dirt);
            mat_terrain.setFloat("m_Tex2Scale", 32f);
         
            /** 1.4) Add ROAD texture into the blue layer (m_Tex3) */
            Texture rock = assetManager.loadTexture("dirt.jpg");
            rock.setWrap(WrapMode.Repeat);
            mat_terrain.setTexture("m_Tex3", rock);
            mat_terrain.setFloat("m_Tex3Scale", 128f);

         // terrain.setMaterial(mat_terrain);


      //*********************************************************************************//
      //***                             Licht und Schatten                            ***//
      //*********************************************************************************//
      // Einrichtung der Lichtquellen und des Schattenwurfs                              //
      //*********************************************************************************//
        //Sonne 
        DirectionalLight sun = new DirectionalLight();
        Vector3f lightdirection =new Vector3f(0.7366f,-0.44128f,-0.512525f).normalize();
        sun.setDirection(lightdirection);
        sun.setColor(ColorRGBA.White.mult(1.0f));
        rootNode.addLight(sun);

        //Approcimation indirekter Beleuchtung 
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.9f));
        rootNode.addLight(ambient);
//         rootNode.setShadowMode(ShadowMode.Off);
// 
//         PssmShadowRenderer pssmRenderer = new PssmShadowRenderer(
//         assetManager,1024,4,PssmShadowRenderer.EDGE_FILTERING_PCF);
//         pssmRenderer.setDirection(lightdirection);
//         viewPort.addProcessor(pssmRenderer);

//         rootNode.setShadowMode(ShadowMode.Off);
//         BasicShadowRenderer bsr = new BasicShadowRenderer(assetManager, 256);
//         bsr.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
//         viewPort.addProcessor(bsr);




      //*********************************************************************************//
      //***           Kurvendaten erhalten und Bah erzeugen                           ***//
      //*********************************************************************************//
      // In dieser Sektion werden die Kurvendaten geholt und das Achterbahnobjekt        //
      // dynamisch erzeugt                                                               //
      //*********************************************************************************//
        
        //Kurve erzeugen, Bahn erzeugen, Geometrieknote erzeugen
        Curve curve = readCurve();
        Achterbahn bahn = new Achterbahn(curve,bahn_material,joint);
        rootNode.attachChild(bahn); 

        
        bahn.setShadowMode(ShadowMode.CastAndReceive);  //Schattenwurf
 

      //*********************************************************************************//
      //***                   Userinteraktion                                         ***//
      //*********************************************************************************//
      // Tastaturevents abfangen und umleiten                                            //
      //*********************************************************************************//

        inputManager.addMapping("up",  new KeyTrigger(KeyInput.KEY_ADD));
        inputManager.addMapping("down",  new KeyTrigger(KeyInput.KEY_SUBTRACT));

        inputManager.addListener(actionListener, new String[]{"up","down"});

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

        //ImplementMe: Simon+Robin   (das reinlinken des Observerpattern auf die Physik etc und Kameraupdates sind hier zu machen!)





//        if (!pause) { time+=tpf;}
//         
//         if (time > 1) {
//           geom[counter].removeFromParent();
//           rootNode.attachChild(geom[++counter]);
//           time -=1;
//         }

        //Dies wird aufgerufen bevor ein Frame gerendert wird

        /*Ein bisschen Bewegung: hier wird immer wieder die Bahn entlang gefahren*/

/*        if (points == null) return;

       if (!pause) {time += tpf*12.0;}
        
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
  

    //*********************************************************************************//
    //***                 Interaktionsmethoden zur GUI                              ***//
    //*********************************************************************************//
    // Die nachfolgenden Methoden dienen zum Informationsaustausch zwischen GUi und    //
    //3D-Komponente.                                                                   //
    //*********************************************************************************//


    /** Setzt die HUD-Daten die nicht sowieso intern bekannt sind. Insbesondere müssen hier dinge wie die maximalen Beschleunigungen etc übergeben werden.
    <br> <DEV> @Robin: Bitte definieren welche Daten benötigt werden*/
    public void setHUDData(/*Insert data here*/) {//ImplementMe: Robin
    }

    /**Läd die Dekorationsscene aus einer Datei. Die Datei muss vom Modelloader von jMonkey verarbeitbar sein, also als OgreMesh, gepackte Scene (zip) oder obj-Wavefront vorliegen*/
    public void loadDeko(String filename) {//ImplementMe: Matthias
    }

    /**Setzt den Track und damit die notwendigen Bahndaten. 
    <br><br>

    <DEV> Es ist zu entscheiden ob wir hier dann auch die Physik initialisieren wenn wir sowieso als Pumpe zuständig sind
    */
    public void setTrack(Track track) { //ImplementMe: Matthias
    }

    /**Gibt das Pattern für die Achterbahn, also dessen Querschnitt vor. Das Pattern wird sofern verfügbar aus der Datei gelesen. 
    Gibt es die Datei nicht, wird eine FileNotFoundException geworfen.
    Wenn null anstatt eines String übergeben wird, dann wird ein SimplePattern initialisiert */
    public void setPattern (String filename) throws FileNotFoundException {//ImplementMe: Matthias
    }

    /**Gibt einen Pfad für die Quelle der Joints vor. Der Pfad ist relativ zum Assetsverzeichnis models anzugeben. 
    Ist die Datei nicht auffindbar, wird eine Exception geworfen
    */
    public void setJoint (String filename) throws FileNotFoundException {//ImplementMe: Matthias
    }

    public boolean getShowStateDekoration ()  {return true;//ImplementMe: Matthias
    }
    public void  setShowStateDekoration (boolean state)  {//ImplementMe: Matthias
    }

    public boolean getShowStatePoles ()  { return true;//ImplementMe: Matthias
    }
    public void setShowStatePoles (boolean state)  {//ImplementMe: Matthias
    }

    public void setCameraMode (char mode) {//ImplementMe: Simon
    }
    public char getCameraMode () {return 'i';//ImplementMe: Simon
    }

}
