/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *Eine sich drehende Kompassnadel
 * @author Robin
 */
public class YRotation extends Node{
    private AssetManager asm;
    
    private Node backGround, needle;
    
    private float radius=50;
    
    public YRotation(AssetManager asm){
        this.asm = asm;
        init();
    }
    
    private void init(){
        System.out.println("YRot->Init");
        backGround = new Node();
        Box s2 = new Box(radius*1.1f,radius*1.1f,0);
        Geometry cube_translucent = new Geometry("s2", s2);
        Material mat_tt = new Material(asm, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_tt.setTexture("ColorMap", asm.loadTexture("./Kompass_Hintergrund.png"));
        mat_tt.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        cube_translucent.setMaterial(mat_tt);
    
        backGround.attachChild(cube_translucent);
        
        needle = new Node();
        Box b_needle = new Box(radius, radius, 0);
        Geometry geom_needle = new Geometry("needle", b_needle);
        Material mat_needle = new Material(asm, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_needle.setTexture("ColorMap", asm.loadTexture("./Kompass_Nadel.png"));
        mat_needle.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        geom_needle.setMaterial(mat_needle);
        
        needle.attachChild(geom_needle);
        
        
        this.attachChild(this.backGround);
        this.attachChild(this.needle);
        
    }
    
    /**
     * rotates the needle to a given position
     * @param degree 
     */
    public void rotateTo(float degree){
        
    }
    
    /**
     * rotates the needly by a given value
     * @param degree 
     */
    public void rotateBy(float degree){
        this.needle.rotate(0, 0, degree);
    }
    
}
