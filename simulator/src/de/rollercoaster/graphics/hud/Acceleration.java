/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rollercoaster.graphics.hud;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * Drei Balken für drei Beschleunigungen
 * @author Robin
 */
public class Acceleration extends Node{
    private Box xAccB, yAccB, zAccB;
    private Geometry xAccG, yAccG, zAccG;
    
    private float radius = 5;
    
    //max g-values
    private float   xMax = 10,
                    yMax = 10,
                    zMax = 10;
    
    //value to size ratio
    private float   vsrX = 3,   
                    vsrY = 3,
                    vsrZ = 3;
     
    private Material mat_x, mat_y, mat_z;
    
    private final static float DIAGF = (float)(1.0 /Math.sqrt(2));
    
    private AssetManager asm;
    
    public Acceleration(AssetManager asm){
        this.asm = asm;
        init();
    }
    
    private void init(){
        //x-Box muhahahaaaa
        xAccB = new Box(radius*0.5f, radius, 0);
        xAccG = new Geometry("xBox", xAccB);
        mat_x = new Material(asm, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_x.setColor("m_Color", ColorRGBA.Green);
        xAccG.setMaterial(mat_x);
        xAccG.rotate(0, 0, (float)Math.PI*0.5f) ;
        xAccG.move(radius, 0, 0) ;
        
        //y-Box
        yAccB = new Box(radius*0.5f, radius, 0);
        yAccG = new Geometry("yBox", yAccB);
        mat_y = new Material(asm, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_y.setColor("m_Color", ColorRGBA.Blue);
        yAccG.setMaterial(mat_y);
        yAccG.rotate(0, 0, -(float)Math.PI*0.25f);
        yAccG.move(DIAGF * radius, DIAGF * radius, 0) ;
        
        //z-Box 
        zAccB = new Box(radius*0.5f, radius, 0);
        zAccG = new Geometry("zBox", zAccB);
        mat_z = new Material(asm, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_y.setColor("m_Color", ColorRGBA.Yellow);
        zAccG.setMaterial(mat_z);
        zAccG.move(0, radius, 0) ;
        
        
        this.attachChild(xAccG);
        this.attachChild(yAccG);
        this.attachChild(zAccG);
    }
    
    public void setXAcc(float acc){
 float value = vsrX * acc;
        this.xAccG.setLocalScale(1, value, 1);
        this.xAccG.setLocalTranslation(radius * value, 0, 0);
        
      
        if(Math.abs(acc) > xMax){
            mat_x.setColor("Color", ColorRGBA.Red);
        }
        else{
            mat_x.setColor("Color", ColorRGBA.Green);
        }
    }
    
    public void setYAcc(float acc){
          float value = vsrY * acc;
        this.yAccG.setLocalScale(1, value, 1);
        this.yAccG.setLocalTranslation(DIAGF*radius * value, DIAGF*radius * value, 0);
        
      
        if(Math.abs(acc) > yMax){
            mat_y.setColor("Color", ColorRGBA.Red);
        }
        else{
            mat_y.setColor("Color", ColorRGBA.Blue);
        } 
    }
    
    public void setZAcc(float acc){
        float value = vsrZ * acc;
        this.zAccG.setLocalScale(1, value, 1);
        this.zAccG.setLocalTranslation(0, radius * value, 0);
        
      
        if(Math.abs(acc) > zMax){
            mat_z.setColor("Color", ColorRGBA.Red);
        }
        else{
            mat_z.setColor("Color", ColorRGBA.Yellow);
        }
    }
    
}
