package de.rollercoaster.graphics;

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
        // display a cube
        Box boxshape1 = new Box(new Vector3f(-3f, 1.1f, 0f), 1f, 1f, 1f);
        Geometry cube = new Geometry("My Textured Box", boxshape1);
        Material mat_stl = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
        Texture tex_ml = assetManager.loadTexture("Interface/Logo/Monkey.jpg");
        mat_stl.setTexture("m_ColorMap", tex_ml);
        cube.setMaterial(mat_stl);
        rootNode.attachChild(cube);

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
