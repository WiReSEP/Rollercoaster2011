package de.rollercoaster.physics;

import de.rollercoaster.mathematics.CurvePoint;
import de.rollercoaster.mathematics.SimpleCurvePoint;
import de.rollercoaster.mathematics.Vector3d;

public class TrivialPhysics implements Trajectory {
 private final static CurvePoint ZERO = new SimpleCurvePoint(Vector3d.ZERO,
            new Vector3d(1.0, 0.0, 0.0), Vector3d.ZERO, new Vector3d(0.0, 1.0, 0.0));
  
 private final static TrajectoryPoint TRIVIAL_STATE = new SimpleTrajectoryPoint(ZERO, 0.0, Vector3d.ZERO, Vector3d.ZERO, Vector3d.ZERO);
 
    @Override
    public void computeTimeStep(double timeDiff) {
    }

    @Override
    public TrajectoryPoint getState() {
        return TRIVIAL_STATE;
    }

    @Override
    public boolean addObserver(TrajectoryObserver observer) {
        return false;
    }

    @Override
    public boolean removeObserver(TrajectoryObserver observer) {
         return false;
    }
}
