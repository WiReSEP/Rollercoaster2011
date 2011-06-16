package de.rollercoaster.physics;

import de.rollercoaster.mathematics.CurvePoint;
import de.rollercoaster.mathematics.SimpleCurvePoint;
import de.rollercoaster.mathematics.Vector3d;

/** 
 * Einfache Implementierung eines unveränderlichen <tt>TrajectoryPoint</tt>
 * @author mangelsdorf
 */
public class SimpleTrajectoryPoint extends SimpleCurvePoint implements TrajectoryPoint {
  private final Vector3d velocity;
  private final Vector3d acceleration;
  private final Vector3d jerk;

  public SimpleTrajectoryPoint(CurvePoint point, Vector3d velocity, Vector3d acceleration, Vector3d jerk) {
    super(point.getPosition(), point.getDerivative(), point.getSecondDerivative(), point.getYawAxis());
    
    this.velocity = velocity;
    this.acceleration = acceleration;
    this.jerk = jerk;
  }

  @Override
  public Vector3d getVelocity() {
    return velocity;
  }

  @Override
  public Vector3d getAcceleration() {
    return acceleration;
  }

  @Override
  public Vector3d getJerk() {
    return jerk;
  }
}
