/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hud;

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
        yAccG.move((float)Math.sqrt(1)*radius, (float)Math.sqrt(2) * radius, 0) ;
        
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

        float oldFloat = this.xAccG.getLocalScale().y*radius;
        if(oldFloat > 1.5*xMax*vsrX && acc > 0){
            return;
        }
        this.xAccG.scale(1, 1+acc, 1);
        float newFloat = this.xAccG.getLocalScale().y*radius; 
        this.xAccG.move((newFloat-oldFloat)*1f ,0, 0);
        System.out.printf("xAcc: %f\n", newFloat);
        if(newFloat > vsrX*xMax){
            mat_x.setColor("Color", ColorRGBA.Red);
        }
        else{
            mat_x.setColor("Color", ColorRGBA.Green);
        }
    }
    
    public void setYAcc(float acc){
        float oldFloat = this.yAccG.getLocalScale().y*radius;
        if(oldFloat > 1.5*yMax*vsrY && acc > 0){
            return;
        }      
        this.yAccG.scale(1, 1+acc, 1);
        float newFloat = this.yAccG.getLocalScale().y*radius; 
        this.yAccG.move((float)Math.sqrt(2)*acc*oldFloat/2 ,(float)Math.sqrt(2)*acc*oldFloat/2, 0);
        System.out.printf("yAcc: %f\n", newFloat);
        if(newFloat > vsrY*yMax){
            mat_y.setColor("Color", ColorRGBA.Red);
        }
        else{
            mat_y.setColor("Color", ColorRGBA.Blue);
        };
        
    }
    
    public void setZAcc(float acc){
        float oldFloat = this.zAccG.getLocalScale().y*radius;
        if(oldFloat > 1.5*zMax*vsrZ && acc > 0){
            return;
        }
        this.zAccG.scale(1, 1+acc, 1);
        float newFloat = this.zAccG.getLocalScale().y*radius; 
        this.zAccG.move(0,(newFloat-oldFloat)*1f ,0);
        System.out.printf("zAcc: %f\n", newFloat);
        if(newFloat > vsrZ*zMax){
            mat_z.setColor("Color", ColorRGBA.Red);
        }
        else{
            mat_z.setColor("Color", ColorRGBA.Yellow);
        }
    }
    
}
