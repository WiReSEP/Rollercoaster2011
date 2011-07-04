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
import com.jme3.scene.shape.Box;

//Fremdpackete (java)
import java.util.List;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.File;


public class Graphics3D extends SimpleApplication {

    private final RollercoasterView view;
    private CameraControl cameraControl;
    private Achterbahn bahn;
    private int counter = 0;
    public boolean pause = false;
    private Material wireMaterial;
    private Material showNormalsMaterial;
    private Material redMat;
    private double time = 0;
    private List<CurvePoint> points;
    private JmeCanvasContext ctx = null;

    private Spatial car;

    public Graphics3D(RollercoasterView view) {
        super();
        this.view = view;
    }
    private boolean close = false;

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
        assetManager.registerLocator("../models/", FileLocator.class.getName());  //Custom-Path einrichten

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
        Material bahn_material = ((Geometry) ((Node) joint).getChild(0)).getMaterial();

        //Gizzmo laden [DEBUG]
        Spatial gizzmo = assetManager.loadModel("gizzmo.mesh.xml");
        gizzmo.scale(20);
        rootNode.attachChild(gizzmo);

        //Gelände laden
        Spatial terrain = assetManager.loadModel("terrain.mesh.xml");
        terrain.setCullHint(Spatial.CullHint.Never); //nie verstecken
        terrain.scale(10, 6, 10);
        terrain.move(0, -15, 100);
        rootNode.attachChild(terrain);
        terrain.setShadowMode(ShadowMode.Receive);  //Schattenwurf

       
        //Wagen bauen
        car = new Geometry("carnode",new Box(1,0.5f,2));
        car.setMaterial(mat1);
        //rootNode.attachChild(car);


      //*********************************************************************************//
      //***                             Licht und Schatten                            ***//
      //*********************************************************************************//
      // Einrichtung der Lichtquellen und des Schattenwurfs                              //
      //*********************************************************************************//

        //Sonne 
        DirectionalLight sun = new DirectionalLight();
        Vector3f lightdirection = new Vector3f(0.7366f, -0.44128f, -0.512525f).normalize();
        sun.setDirection(lightdirection);
        sun.setColor(ColorRGBA.White.mult(1.0f));
        rootNode.addLight(sun);

        //Approcimation indirekter Beleuchtung 
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.9f));
        rootNode.addLight(ambient);


        //*********************************************************************************//
        //***           Kurvendaten erhalten und Bah erzeugen                           ***//
        //*********************************************************************************//
        // In dieser Sektion werden die Kurvendaten geholt und das Achterbahnobjekt        //
        // dynamisch erzeugt                                                               //
        //*********************************************************************************//

        //Kurve erzeugen, Bahn erzeugen, Geometrieknote erzeugen
        bahn = new Achterbahn(view.getCurve(), bahn_material, joint);
        rootNode.attachChild(bahn);


        bahn.setShadowMode(ShadowMode.CastAndReceive);  //Schattenwurf


        //*********************************************************************************//
        //***                   Userinteraktion                                         ***//
        //*********************************************************************************//
        // Tastaturevents abfangen und umleiten                                            //
        //*********************************************************************************//



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

        this.cameraControl = new CameraControl(this, cam);

        //*********************************************************************************//
        //*********************************************************************************//
    }

    @Override
    public void simpleUpdate(float tpf) {
        //*********************************************************************************//
        //*** MainLoop                                                                  ***//
        //*********************************************************************************//
        // Teile allen Observern den nächsten Zeitschritt mit                              //
        //*********************************************************************************//
        view.notifyObservers(tpf);

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

    /**Setzt die Bahnkurve. 
    <br><br>
    
    <DEV> Es ist zu entscheiden ob wir hier dann auch die Physik initialisieren wenn wir sowieso als Pumpe zuständig sind
     */
    public void setCurve(Curve curve) { //ImplementMe: Matthias
    }

    /**Gibt das Pattern für die Achterbahn, also dessen Querschnitt vor. Das Pattern wird sofern verfügbar aus der Datei gelesen. 
    Gibt es die Datei nicht, wird eine FileNotFoundException geworfen.
    Wenn null anstatt eines String übergeben wird, dann wird ein SimplePattern initialisiert */
    public void setPattern(String filename) throws FileNotFoundException {//ImplementMe: Matthias
    }

    /**Gibt einen Pfad für die Quelle der Joints vor. Der Pfad ist relativ zum Assetsverzeichnis models anzugeben. 
    Ist die Datei nicht auffindbar, wird eine Exception geworfen
     */
    public void setJoint(String filename) throws FileNotFoundException {//ImplementMe: Matthias
    }

    public boolean getShowStateDekoration() {
        return true;//ImplementMe: Matthias
    }

    public void setShowStateDekoration(boolean state) {//ImplementMe: Matthias
    }

    public boolean getShowStatePoles() {
        return true;//ImplementMe: Matthias
    }

    public void setShowStatePoles(boolean state) {//ImplementMe: Matthias
    }

    public void setCameraMode(CameraMode mode) {
        cameraControl.setCameraMode(mode);
    }

    public CameraMode getCameraMode() {
        return cameraControl.getCameraMode();
    }

    void setCamera(Vector3f location, Vector3f left, Vector3f up, Vector3f direction) {
        Vector3f pos = location.add(up.normalize().mult(2.0f));
        cameraControl.setCarPosition(pos, left.mult(-1.0f), up, direction);
    }
}
