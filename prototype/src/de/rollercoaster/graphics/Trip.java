/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;

/**
 *
 * @author Robin
 */
public class Trip {
    private int[] f;
    
    public Trip(int[] f){
        this.f = f.clone();
    }
    
    public int getPos(int count){
        return count==0?f[0]:(count==1?f[6]:-1);
    }
    
    public int getNormal(int count){
        return count==0?f[1]:(count==1?f[7]:-1);
    }
}
