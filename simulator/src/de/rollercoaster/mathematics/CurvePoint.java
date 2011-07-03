package de.rollercoaster.mathematics;

public interface CurvePoint {
    Vector3d getPosition();
    Vector3d getDerivative();
    Vector3d getSecondDerivative();
    
    Vector3d getRollAxis();
    Vector3d getPitchAxis();
    Vector3d getYawAxis(); 
    
    double getYawAngle();
}
