/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rollercoaster.graphics.hud;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *Zwei Ringe, die sich gegeneinander Verdrehen, bzw. der äußere steht still
 * 
 * 
 * @author Robin
 */
public class ZRotation extends Node{
    private SimpleApplication sa;
    private AssetManager asm;
    
    private Node outerRing;
    private Node innerRing;
    
    private float radius= 50;
    
    private double myRot;
    
    public ZRotation(SimpleApplication sa, float radius){
        this.sa = sa;
        this.asm = sa.getAssetManager();
        this.radius = radius;
        initOuterRing();
        initInnerRing();
        myRot = 0;
    }
    
        public ZRotation(SimpleApplication sa){
        this.sa = sa;
        this.asm = sa.getAssetManager();
        radius = sa.getContext().getSettings().getHeight()/8.5f;
        initOuterRing();
        initInnerRing();
        myRot = 0;
    }
    

    
    private void initInnerRing(){
        innerRing = new Node();
        
      
            /** Translucent/transparent cube. Uses Texture from jme3-test-data library! */
        Box s2 = new Box(radius,radius,0);
        Geometry cube_translucent = new Geometry("s2", s2);
        Material mat_tt = new Material(asm, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_tt.setColor("m_Color", ColorRGBA.Blue);
        mat_tt.setTexture("ColorMap", asm.loadTexture("./nKreis2.png"));
        mat_tt.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        cube_translucent.setMaterial(mat_tt); 
       
        

        innerRing.attachChild(cube_translucent); 
        
        
        this.attachChild(this.innerRing);
        
        //get to the middle
        float xpos = (this.sa.getContext().getSettings().getWidth()-radius*0)/2;
        float ypos = (this.sa.getContext().getSettings().getHeight()+radius)/2;
        this.move(xpos, ypos, 0);

    }
    
    private void initOuterRing(){
        outerRing = new Node();
        Box s2 = new Box(radius*1.1f,radius*1.1f,0);
        Geometry cube_translucent = new Geometry("s2", s2);
        Material mat_tt = new Material(asm, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_tt.setTexture("ColorMap", asm.loadTexture("./nKreis2.png"));
        mat_tt.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        cube_translucent.setMaterial(mat_tt);
        
        
        
        outerRing.attachChild(cube_translucent);
        
        this.attachChild(this.outerRing);
    }
    
    /**
     * rotates the inner ring to a given position
     * @param degree 
     */
    public void rotateTo(float degree){
        //System.out.printf("Rotate to: %f\n.", degree);
        innerRing.rotate(0,0,(float)(degree-myRot));
        myRot=(degree);
        //innerRing.rotate(Quaternion.fromAngleAxis(degree, new Vector3f(0, 0, 1))); 
        //rotateBy(degree*0.001f);
    }
    
    /**
     * rotates the inner ring by 
     * @param degree 
     */
    public void rotateBy(float degree){
        innerRing.rotate(0, 0, degree);
        myRot+=degree;
    }
}
