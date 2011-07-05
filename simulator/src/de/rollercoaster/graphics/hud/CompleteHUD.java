package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Robin
 */
public class CompleteHUD extends com.jme3.scene.Node{
    private ZRotation zRot;
    private RightHuD rHud;
    
    public CompleteHUD(SimpleApplication sa, AssetManager asm, BitmapFont gFont){
        zRot = new ZRotation(sa);
        rHud = new RightHuD(sa, asm, gFont);
        
        this.attachChild(zRot);
        this.attachChild(rHud);
    }
    
    /**
     * sets the speedUp for each direction
     * @param acc 
     */
    public void setAcceleration(Vector3f acc){
        rHud.setAcceleration(acc);
    }
    
    /**
     * Sets the rotation to be shown in the hud
     * @param rot x,y,z rotation
     */
    public void setRotation(Vector3f rot){
        zRot.rotateTo(rot.getZ());
        rHud.setRotation(rot.getX(), rot.getY());
    }
    
}
