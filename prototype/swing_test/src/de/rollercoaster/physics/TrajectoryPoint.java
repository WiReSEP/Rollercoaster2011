package de.rollercoaster.physics;

import com.jme3.math.Vector3f;

public interface TrajectoryPoint {
    Vector3f getPosition();
    Vector3f getVelocity();
    Vector3f getAcceleration();
    Vector3f getJerk();
}
