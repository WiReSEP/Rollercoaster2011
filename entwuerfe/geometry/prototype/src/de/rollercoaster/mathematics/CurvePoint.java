package de.rollercoaster.mathematics;

import com.jme3.math.Vector3f;

public interface CurvePoint {
    Vector3f getPosition();
    Vector3f getRollAxis();
    Vector3f getPitchAxis();
    Vector3f getYawAxis();    
}
