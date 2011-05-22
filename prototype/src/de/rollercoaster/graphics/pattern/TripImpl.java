/*
 * Enthlt die Nummern der Vektoren
 */
package de.rollercoaster.graphics.pattern;

import com.jme3.math.Vector3f;

/**
 *
 * @author Robin
 */
public class TripImpl {
    private int[] f;
    
    public TripImpl(int[] f){
        this.f = f.clone();
    }
    
	/**
	* Liefert die gesuchte Position, Stelle 1(0) oder 2(1)
	* @param die Stelle
	* @return der WErt
	*/
    public int getPos(int count){
        return count==0?f[0]:(count==1?f[6]:-1);
    }
    
	/**
	* Liefert den gesuchten Normalenvektor, Stelle 1(0) oder 2(1)
	* @param die Stelle
	* @return der WErt
	*/
    public int getNormal(int count){
       return count==0?f[1]:(count==1?f[7]:-1);

    }
	
	/**
	* Liefert aus dem Array einen beliebigen gesuchen Wert
	* @param die Stelle des gesuchten Wertes
	* @return der Wert
	*/
	public int getValue(int count){
		//return count<0=-1:count>7?-1:f[count];   //Syntax?!
    return 0;
	}
}
