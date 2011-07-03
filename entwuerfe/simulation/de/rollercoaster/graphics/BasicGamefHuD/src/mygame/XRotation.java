/*
 * Eein einfacher Winkel gibt die Steigung an
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Robin
 */
public class XRotation extends Node {
    
    private AssetManager asm;
    
    private Node degree;
    
    private Box b;
    
    private float radius = 50f;
    
    public XRotation(AssetManager asm){
        this.asm = asm;
        init();
    }
    
    private void init(){
        degree = new Node();
        Box b_degree = new Box(radius, radius, 0);
        Geometry geom_degree = new Geometry("needle", b_degree);
        Material mat_needle = new Material(asm, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_needle.setTexture("ColorMap", asm.loadTexture("Textures/Winkel.png"));
        mat_needle.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        geom_degree.setMaterial(mat_needle);
        
        degree.attachChild(geom_degree);
        
        this.attachChild(degree);
    }
    
    /**
     * Ist noch nicht einsatzbereit, Verhältnis zwischen V3erschiebung und Vergrößrung passt noch nciht
     * @param degree 
     */
    public void setDegreeTo(float degree){
        //this.degree.scale(degree);
        this.degree.move(0, this.degree.getLocalTranslation().y*degree/2, 0);
        this.degree.scale(1, degree, 1);
        
    }
    
}
