package de.rollercoaster.physics;

public interface Trajectory {
    void computeTimeStep(double timeDiff);
    TrajectoryPoint getState();
}
