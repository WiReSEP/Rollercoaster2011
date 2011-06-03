package de.rollercoaster.physics;

import com.jme3.math.Vector3f;
import de.rollercoaster.mathematics.CurvePoint;

public interface TrajectoryPoint extends CurvePoint {
    Vector3f getVelocity();
    Vector3f getAcceleration();
    Vector3f getJerk();
}
