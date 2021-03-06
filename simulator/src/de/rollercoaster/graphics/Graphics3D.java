package de.rollercoaster.graphics;
//eigene Packete
import de.rollercoaster.mathematics.*;
import de.rollercoaster.graphics.pattern.*;
import de.rollercoaster.data.SerializedTrack;
import de.rollercoaster.data.Track;
import de.rollercoaster.graphics.hud.CompleteHUD;

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
import com.jme3.scene.Spatial.CullHint;
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
import com.jme3.network.Client;
import com.jme3.util.SkyFactory;
import com.jme3.scene.shape.Box;

import com.jme3.texture.FrameBuffer;
import com.jme3.util.BufferUtils;
import com.jme3.util.Screenshots;
import com.jme3.input.InputManager;
import com.jme3.post.SceneProcessor;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;



//Fremdpackete (java)
import java.util.List;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

import java.util.logging.Logger;
import java.util.logging.Level;

//Fremdpackete (native-java)
import java.nio.ByteBuffer;

public class Graphics3D extends SimpleApplication implements SceneProcessor, ActionListener {
    private CameraControl cameraControl;
    //Achterbahndaten
    private RollercoasterView view;
    private Achterbahn bahn = null;
    private Material bahn_material = null;
    private Spatial joint = null;
    private String pattern_filename;
    private String bounding_pattern_filename;
    private Material showNormalsMaterial;
    private Material redMat;
    private JmeCanvasContext ctx = null;
    private Spatial car;
    private Node deko;
    private CompleteHUD hud;
    private BufferedImage awtImage;
    private ByteBuffer outBuf;
    private Renderer renderer;
    private boolean filter_init = false;
    private Material wire;
    private int shotnumber = 0;

    public Graphics3D(RollercoasterView view) {
        super();
        this.view = view;
    }
    private boolean capture_ss = false;

//    @Override
//    public void handleError(java.lang.String errMsg, java.lang.Throwable t) {
//    }
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals("wire") && !keyPressed) {
            rootNode.setMaterial(wire);
        }
        if (name.equals("screenshot") && !keyPressed) {
            capture_ss = true;
        }
        if (name.equals("hide") && !keyPressed) {
            if (bahn.getCullHint() != CullHint.Always) {
                bahn.setCullHint(CullHint.Always);
                car.setCullHint(CullHint.Always);
            } else {
                bahn.setCullHint(CullHint.Dynamic);
                car.setCullHint(CullHint.Dynamic);
            }
        }
    }

    public void initialize(RenderManager rm, ViewPort vp) {
        renderer = rm.getRenderer();
        reshape(vp, vp.getCamera().getWidth(), vp.getCamera().getHeight());
        filter_init = true;
    }

    public void preFrame(float tpf) {
    }

    public void postQueue(RenderQueue rq) {
    }

    public void postFrame(FrameBuffer out) {
        if (capture_ss) {
            capture_ss = false;
            screenshot(out);
        }
    }

    public void cleanup() {
    }

    @Override
    public boolean isInitialized() {
        return filter_init;
    }

    public void reshape(ViewPort vp, int w, int h) {
        outBuf = BufferUtils.createByteBuffer(w * h * 4);
        awtImage = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
    }

    private void screenshot(FrameBuffer fb) {
        renderer.readFrameBuffer(fb, outBuf);
        Screenshots.convertScreenShot(outBuf, awtImage);
        try {
            ImageIO.write(awtImage, "png", new File("screenshot" + shotnumber + ".png"));
        } catch (IOException e) {
            System.err.println("Error creating Screenshot");
        }
        shotnumber++;
    }

    @Override
    public void simpleInitApp() {

        //*********************************************************************************//
        //***                      Einstellungen an der Engine                          ***//
        //*********************************************************************************//
        // Die Einstellungen die für unsere Zwecke angepasst werden müssen werden in dieer //
        // Sektion getätigt                                                                //
        //*********************************************************************************//
        //Debug-FlyByCamera-Controler einrichten
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(50);  //mehr speed

        //Cameraeinstellungen
        cam.setFrustumFar(20000);  //Farclipping ein bisschen erhöhen (damit unsere Landschaft bleibt
        viewPort.setBackgroundColor(ColorRGBA.Black); //Hintergrundfarbe setzen (für Debug auf auffällige Farbe setzen damit Lücken sichtbar werden; Release auf Schwarz oä)

        //nichts kann uns aufhalten  (auch nicht der verlust des fokus)
        this.setPauseOnLostFocus(false);

        this.setDisplayStatView(false);
        this.setDisplayFps(false);

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
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  //ohne Licht
        mat1.setColor("Color", ColorRGBA.Red);

        //Debug-Materialien[DEBUG]
        showNormalsMaterial = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  //ohne Licht
        redMat.setColor("Color", ColorRGBA.Red);

        wire = new Material(assetManager, "WireColor.j3md");
        wire.setColor("Color", ColorRGBA.White);


        //Deko Knoten erzeugen
        deko = new Node("deco_node");
        rootNode.attachChild(deko);

        //Skybox (Beispiel Dekoration)
        deko.attachChild(SkyFactory.createSky(assetManager,
                assetManager.loadTexture("skybox/west.png"), //west
                assetManager.loadTexture("skybox/east.png"), //east
                assetManager.loadTexture("skybox/north.png"), //north
                assetManager.loadTexture("skybox/south.png"), //south
                assetManager.loadTexture("skybox/up.png"), //up
                assetManager.loadTexture("skybox/down.png") //down
                ));

        //Gelände laden (Beispiel Dekoration)
        Spatial terrain = assetManager.loadModel("terrain.mesh.xml");
        terrain.setCullHint(Spatial.CullHint.Never); //nie verstecken
        terrain.scale(10, 6, 10);
        terrain.move(0, -15, 100);
        deko.attachChild(terrain);
        terrain.setShadowMode(ShadowMode.Receive);  //Schattenwurf

        //Joint laden
        joint = assetManager.loadModel("joint.mesh.xml");
        bahn_material = ((Geometry) ((Node) joint).getChild(0)).getMaterial();

        //Wagen bauen
        //car = new Geometry("carnode", new Box(1, 0.5f, 2));
        //car.setMaterial(mat1);
        car = assetManager.loadModel("car.mesh.xml");
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
        //***           Kurvendaten erhalten und Bahn erzeugen                          ***//
        //*********************************************************************************//
        // In dieser Sektion werden die Kurvendaten geholt und das Achterbahnobjekt        //
        // dynamisch erzeugt                                                               //
        //*********************************************************************************//

        //Kurve erzeugen, Bahn erzeugen, Geometrieknote erzeugen
        pattern_filename = "../models/pattern.obj";
        bounding_pattern_filename = "../models/bounding_pattern.obj";
        bahn = new Achterbahn(view.getCurve(), bahn_material, joint, pattern_filename, bounding_pattern_filename);
        rootNode.attachChild(bahn);


        bahn.setShadowMode(ShadowMode.CastAndReceive);  //Schattenwurf


        //*********************************************************************************//
        //***                   Userinteraktion                                         ***//
        //*********************************************************************************// 
        // Tastaturevents abfangen und umleiten                                            //
        //*********************************************************************************//

        inputManager.addMapping("screenshot", new KeyTrigger(KeyInput.KEY_SYSRQ));
        inputManager.addMapping("screenshot", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("wire", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("hide", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addListener(this, new String[]{"screenshot", "wire", "hide"});

//Screenshotfilter einbauen
        List<ViewPort> vps = this.getRenderManager().getPostViews();
        ViewPort last = vps.get(vps.size() - 1);
        last.addProcessor(this);




        //*********************************************************************************//
        //***                             GUI                                           ***//
        //*********************************************************************************//
        // Einrichtung der HUD Anzeigekomponente                                           //
        //*********************************************************************************//
        hud = new CompleteHUD(this, assetManager, guiFont);
        guiNode.attachChild(hud);
        hud.setEnable(false);

        //*********************************************************************************//
        //***                 Cameracontroler                                           ***//
        //*********************************************************************************//
        // Einrichtung des Cameracontroler zur Unterstützung unterschiedlicher Modi        //
        //*********************************************************************************//

        this.cameraControl = new CameraControl(this, cam, car);

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
//     settings.setWidth(640);
//     settings.setHeight(480);
        settings.setWidth(640);
        settings.setHeight(480);
        settings.setFrameRate(30);
        settings.setSamples(4);

        this.setSettings(settings);

        this.createCanvas(); // create canvas!
        ctx = (JmeCanvasContext) this.getContext();
        Dimension dim = new Dimension(640, 480);
        ctx.getCanvas().setPreferredSize(dim);
        return ctx;
    }

    //Bei closen unbedingt sowas bauen sonst wartet man ewig auf den lwjgl
    public void freeCanvas() {
        this.stop(true); //wir warten auf das beenden der game engine
    }

    /**Sorgt für die Neuerzeugung der gesamten Achterbahn. Diese Methode wird intern benutzt wenn eine Veränderung das neu Generieren der Achterbahn notwendig macht*/
    private void reinit() {
        if (null != bahn) {
            bahn.removeFromParent();
        }
        if (null != view.getCurve()) {
            bahn = new Achterbahn(view.getCurve(), bahn_material, joint, pattern_filename, bounding_pattern_filename);
            rootNode.attachChild(bahn);
        }
    }

    //*********************************************************************************//
    //***                 Interaktionsmethoden zur GUI                              ***//
    //*********************************************************************************//
    // Die nachfolgenden Methoden dienen zum Informationsaustausch zwischen GUi und    //
    //3D-Komponente.                                                                   //
    //*********************************************************************************//
    /** Setzt die HUD-Daten die nicht sowieso intern bekannt sind. Insbesondere müssen hier dinge wie die maximalen Beschleunigungen etc übergeben werden.
     *
     * @param rot der Rotationsvektor
     * @param acc der Beschleunigungsvektor
     */
    public void setHUDData(Vector3f rot, Vector3f acc) {//ImplementMe: Robin

        this.hud.setRotation(rot);
        this.hud.setAcceleration(acc);

    }

    public void setEnableHUD(boolean arg) {
        this.hud.setEnable(arg);
    }

    /**Läd die Dekorationsscene aus einer Datei. Die Datei muss vom Modelloader von jMonkey verarbeitbar sein, also als OgreMesh, gepackte Scene (zip) oder obj-Wavefront vorliegen*/
    public void loadDeko(String filename) throws IllegalArgumentException {
        deko.detachAllChildren();
        File tmp_file = new File(filename);
        String parent = tmp_file.getParent();
        String file = tmp_file.getName();
        try {
            assetManager.registerLocator(parent, FileLocator.class.getName());  //Custom-Path einrichten
            Spatial deko_content = assetManager.loadModel(file);
            deko.attachChild(deko_content);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to Load Dekoration");
        }


    }

    /**Ruft ein Update auf. Die Curve wird tatsächlich nicht benutzt, da angenommen wird, dass diese Methode vom View aufgerufen wurde. 
     */
    public void setCurve(Curve curve) {
        //if (curve == null) car.setCullHint(CullHint.Always);
        reinit();
    }

    /**Gibt das Pattern für die Achterbahn, also dessen Querschnitt vor. Das Pattern wird sofern verfügbar aus der Datei gelesen. 
    Gibt es die Datei nicht, wird eine FileNotFoundException geworfen.
    Wenn null anstatt eines String übergeben wird, dann wird ein SimplePattern initialisiert */
    public void setPattern(String pattern_filename) throws FileNotFoundException {
        this.pattern_filename = pattern_filename;
        //this.bounding_pattern_filename = bounding_pattern_filename;
        reinit();
    }

    public void setBoundingPattern(String bounding_pattern_filename) throws FileNotFoundException {
        //this.pattern_filename = pattern_filename;
        this.bounding_pattern_filename = bounding_pattern_filename;
        reinit();
    }

    /**Gibt einen Pfad für die Quelle der Joints vor. Der Pfad ist relativ zum Assetsverzeichnis models anzugeben. 
    Ist die Datei nicht auffindbar, wird eine Exception geworfen
     */
    public void setJoint(String filename) throws IllegalArgumentException {
        File tmp_file = new File(filename);
        String parent = tmp_file.getParent();
        String file = tmp_file.getName();
        try {
            assetManager.registerLocator(parent, FileLocator.class.getName());  //Custom-Path einrichten
            joint = assetManager.loadModel(file);
            bahn_material = ((Geometry) ((Node) joint).getChild(0)).getMaterial();
            reinit();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to Load Joint.");
        }
    }

    /**Liefert genau dann true wenn die Dekoration angezeigt wird*/
    public boolean getShowStateDekoration() {
        return (deko.getCullHint() != CullHint.Always);
    }

    /** Zeigt die Dekoration an oder versteckt sie. Mit true wird die Dekoration angezeigt**/
    public void setShowStateDekoration(boolean state) {
        if (state) {
            deko.setCullHint(CullHint.Dynamic);
        } else {
            deko.setCullHint(CullHint.Always);
        }
    }

    /**Liefert genau dann true wenn die Poles angezeigt wird*/
    public boolean getShowStatePoles() {
        Spatial poles = bahn.getChild("poles");
        if (poles == null) {
            return false;
        }
        return (poles.getCullHint() != CullHint.Always);
    }

    /** Zeigt die Poles an oder versteckt sie. Mit true wird die Dekoration angezeigt**/
    public void setShowStatePoles(boolean state) {
        Spatial poles = bahn.getChild("poles");
        if (poles != null) {
            if (state) {
                poles.setCullHint(CullHint.Dynamic);
            } else {
                poles.setCullHint(CullHint.Always);
            }
        }
    }

    public void setCameraMode(CameraMode mode) {
        if (null != cameraControl) {
            cameraControl.setCameraMode(mode);
        }
    }

    public CameraMode getCameraMode() {
        return null != cameraControl ? cameraControl.getCameraMode() : null;
    }

    void setCamera(Vector3f location, Vector3f left, Vector3f up, Vector3f direction) {
        Vector3f pos = location.add(up.normalize().mult(2.0f));
        cameraControl.setCarPosition(pos, left, up, direction);
    }
}
