package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.plugins.BitmapFontLoader;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.io.File;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private ZRotation zrot;
    private YRotation yrot;
    private XRotation xrot;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    


    @Override
    public void simpleInitApp() {
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        geom.updateModelBound();

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/SolidColor.j3md");
        mat.setColor("m_Color", ColorRGBA.Green);
        geom.setMaterial(mat);
        
        guiNode.detachAllChildren();

        rootNode.attachChild(geom);
        
        zrot = new ZRotation(assetManager);
        zrot.move(100, 100, 0);
        
        yrot = new YRotation(assetManager);
        yrot.move(200, 100, 0);
        
        xrot = new XRotation(assetManager);
        xrot.move(300, 100, 0);
        
        guiNode.attachChild(zrot);
        guiNode.attachChild(yrot);
        guiNode.attachChild(xrot);
   
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        //zrot.rotateTo(this.cam.getDirection().getX()*2*(float)Math.PI);
        yrot.rotateBy(tpf);
        zrot.rotateBy(tpf);
        xrot.setDegreeTo(1+tpf/10f);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    
    
}
