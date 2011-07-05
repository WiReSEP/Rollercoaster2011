/*
 * Eein einfacher Winkel gibt die Steigung an
 */
package de.rollercoaster.graphics.hud;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *Steigungsdreieck
 * @author Robin
 */
public class XRotation extends Node {
    
    private AssetManager asm;
    private BitmapFont gFont;
    
    private Node degree;
    
    private BitmapText string;
    
    private Box b;
    
    private float radius = 50f;
    
    public XRotation(AssetManager asm, BitmapFont guiFont){
        this.asm = asm;
        this.gFont = guiFont;
        init();
        
    }
    
    private void init(){
        degree = new Node();
        Box b_degree = new Box(radius*0.5f, radius, 0);
        Geometry geom_degree = new Geometry("needle", b_degree);
        Material mat_needle = new Material(asm, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_needle.setTexture("ColorMap", asm.loadTexture("./Winkel.png"));
        mat_needle.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        geom_degree.setMaterial(mat_needle);
        
        degree.attachChild(geom_degree);
        
        string = new BitmapText(gFont, false);
        string.setSize(gFont.getCharSet().getRenderedSize());      // font size
        string.setColor(ColorRGBA.White);                             // font color
        string.setText("45°");             // the text
        string.setLocalTranslation(0, /*string.getLineHeight()*/-radius, 0); // position
   
        
        this.attachChild(string);
        
        
        this.attachChild(degree);
    }
    
    /**
     * sets the acclivity to degree
     * @param degree 
     */
    public void setDegreeTo(float degree){

        //this.degree.scale(degree);
        float oldFloat = this.degree.getLocalScale().y*radius;
        
        if(oldFloat > 5*radius && degree >0){
            return;
        }
                
        this.degree.scale(1, degree, 1);
        float newFloat = this.degree.getLocalScale().y*radius; 
        this.degree.move(0,(newFloat-oldFloat)*1f,0);
        this.string.setText(degree + "°");
    }
    
}
