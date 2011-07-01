package de.rollercoaster.physics;

import de.rollercoaster.mathematics.CurvePoint;
import de.rollercoaster.mathematics.Vector3d;

public interface TrajectoryPoint extends CurvePoint {
    double getTime();
    
    Vector3d getVelocity();
    Vector3d getAcceleration();
    Vector3d getJerk();
}
