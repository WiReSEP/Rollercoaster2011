package de.rollercoaster.physics;

import com.jme3.math.Vector3f;
import de.rollercoaster.mathematics.CurvePoint;
import de.rollercoaster.mathematics.SimpleCurvePoint;

/** 
 * Einfache Implementierung eines unveränderlichen <tt>TrajectoryPoint</tt>
 * @author mangelsdorf
 */
public class SimpleTrajectoryPoint extends SimpleCurvePoint implements TrajectoryPoint {
  private final Vector3f velocity;
  private final Vector3f acceleration;
  private final Vector3f jerk;

  public SimpleTrajectoryPoint(CurvePoint point, Vector3f velocity, Vector3f acceleration, Vector3f jerk) {
    super(point.getPosition(), point.getDerivative(), point.getSecondDerivative(), point.getYawAxis());
    
    this.velocity = velocity;
    this.acceleration = acceleration;
    this.jerk = jerk;
  }

  @Override
  public Vector3f getVelocity() {
    return new Vector3f(velocity);
  }

  @Override
  public Vector3f getAcceleration() {
    return new Vector3f(acceleration);
  }

  @Override
  public Vector3f getJerk() {
    return new Vector3f(jerk);
  }
}
