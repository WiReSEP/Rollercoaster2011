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
import com.jme3.system.JmeContext.Type;

//Ein bisschen Licht damit wir die Normalen auch bewundern können^^
import com.jme3.light.DirectionalLight;
import com.jme3.light.AmbientLight;

//den windowlsitener gibt es vorerst damit 
public class Graphics3D extends SimpleApplication {

    private JmeCanvasContext ctx = null;

    public Graphics3D() {
        super();
        //simpleInitApp();
    }
    private boolean close = false;

    @Override
    public void simpleInitApp() {
        start(Type.Canvas);
        flyCam.setDragToRotate(true);
        
        //Kurve erzeugen, Bahn erzeugen, Geometrieknote erzeugen
        Curve curve = new DummyCurve();
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
        geom_bahn.setMaterial(showNormalsMaterial);
        //geom_bahn.setMaterial(mat1);
        //geom_bahn.setMaterial(mat2);


        rootNode.attachChild(geom_bahn);

        
        //Mit dem Normalenshader hat Licht keine Wirkung und mit dem Lighted Shader sieht die Beleuchtung merkwürdig aus
        /* DirectionalLight sun = new DirectionalLight();
          sun.setDirection(new Vector3f(1,0,-2).normalizeLocal());
          sun.setColor(ColorRGBA.White);
          rootNode.addLight(sun);

          AmbientLight ambient = new AmbientLight();
          ambient.setColor(ColorRGBA.Blue);
          rootNode.addLight(ambient);*/

    }

    @Override
    public void simpleUpdate(float tpf) {
        //Dies wird aufgerufen bevor ein Frame gerendert wird
    }

    public JmeCanvasContext getCanvasObject() {
//           AppSettings settings = new AppSettings(true);
//           settings.setWidth(640);
//           settings.setHeight(480);
//           settings.setFrameRate(30);
// 
//           this.setSettings(settings);

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
