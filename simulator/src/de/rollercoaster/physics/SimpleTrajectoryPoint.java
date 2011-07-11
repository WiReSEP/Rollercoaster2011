package de.rollercoaster.physics;

import de.rollercoaster.mathematics.CurvePoint;
import de.rollercoaster.mathematics.SimpleCurvePoint;
import de.rollercoaster.mathematics.Vector3d;

/** 
 * Einfache Implementierung eines unveränderlichen <tt>TrajectoryPoint</tt>
 * @author mangelsdorf
 */
public class SimpleTrajectoryPoint implements TrajectoryPoint {
  private final double time;
  private final Vector3d velocity;
  private final Vector3d acceleration;
  private final Vector3d jerk;
  private final CurvePoint point;

  public SimpleTrajectoryPoint(CurvePoint point, double time, Vector3d velocity, Vector3d acceleration, Vector3d jerk) {
    this.point = point;
    this.time = time;
    this.velocity = velocity;
    this.acceleration = acceleration;
    this.jerk = jerk;
  }

  @Override
  public double getTime() {
    return time;
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

  @Override
  public Vector3d getPosition() {
    return point.getPosition();
  }

  @Override
  public Vector3d getDerivative() {
    return point.getDerivative();
  }

  @Override
  public Vector3d getSecondDerivative() {
    return point.getSecondDerivative();
  }

  @Override
  public Vector3d getRollAxis() {
    return point.getRollAxis();
  }

  @Override
  public Vector3d getPitchAxis() {
    return point.getPitchAxis();
  }

  @Override
  public Vector3d getYawAxis() {
    return point.getYawAxis();
  }

  @Override
  public double getYawAngle() {
    return point.getYawAngle();
  }
}
