/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hud;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Robin
 */
public class RightHuD extends Node{
    private XRotation xRot;
    private YRotation yRot;
    private Acceleration acc;
    
    private AssetManager asm;
    private SimpleApplication sa;
    
    private float radius;
    private float pWidth, pHeight;
    
    public RightHuD(SimpleApplication sa, AssetManager asm, BitmapFont gFont){
        xRot = new XRotation(asm, gFont);
        yRot = new YRotation(asm);
        acc = new Acceleration(asm);
        
        this.asm = asm;
        this.sa = sa;
        
        this.attachChild(xRot);
        this.attachChild(yRot);
        this.attachChild(acc);
        
        pWidth = this.sa.getContext().getSettings().getWidth();
        pHeight = this.sa.getContext().getSettings().getHeight();
        
        radius = this.sa.getContext().getSettings().getWidth()/8.5f;
        
        
        //set the positions
        this.move(pWidth-radius, pHeight-radius, 0);
        this.xRot.move(0, -radius, 0); //Steigungsdreieck
        this.yRot.move(0, 0, 0);   //Kompass
        this.acc.move(-radius*0.333f, -radius*2.5f, 0);//Diagramm
        
    }
    
    /**
     * sets the rotation for the x and y component
     * @param x
     * @param y 
     */
    public void setRotation(float x, float y){
        this.xRot.setDegreeTo(x);
        this.yRot.rotateBy(y);
    }
    
    /**
     * sets the speedUp for each direction
     * @param acc 
     */
    public void setAcceleration(Vector3f acc){
        this.acc.setXAcc(acc.getX());
        this.acc.setYAcc(acc.getY());
        this.acc.setZAcc(acc.getZ());
    }
}
