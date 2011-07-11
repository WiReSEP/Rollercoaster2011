package de.rollercoaster.mathematics;

/** 
 * Einfache Implementierung eines unveränderlichen <tt>CurvePoint</tt>
 * @author mangelsdorf
 */
public class SimpleCurvePoint implements CurvePoint {
  private final Vector3d position;
  private final Vector3d derivative;
  private final Vector3d secondDerivative;
  private final Vector3d roll;
  private final Vector3d pitch;
  private final Vector3d yaw;
  private final double yawAngle;
  private final static Vector3d GLOBAL_UP = new Vector3d(0.0, 1.0, 0.0);

  public SimpleCurvePoint(Vector3d position, Vector3d derivative, Vector3d secondDerivative, double yawAngle) {
    this(position, derivative, secondDerivative, Vector3d.ZERO);
  }

  public SimpleCurvePoint(Vector3d position, Vector3d derivative, Vector3d secondDerivative, Vector3d yaw) {
    this.position = position;
    this.derivative = derivative;
    this.secondDerivative = secondDerivative;

    this.roll = derivative.normalize();
    this.yaw = yaw.subtract(roll.mult(roll.dot(yaw)));
    this.pitch = roll.cross(yaw).normalize();

    this.yawAngle = Math.acos(Vector3d.cos(GLOBAL_UP, yaw));
  }

  @Override
  public Vector3d getPosition() {
    return position;
  }

  @Override
  public Vector3d getDerivative() {
    return derivative;
  }

  @Override
  public Vector3d getSecondDerivative() {
    return secondDerivative;
  }

  @Override
  public Vector3d getRollAxis() {
    return roll;
  }

  @Override
  public Vector3d getPitchAxis() {
    return pitch;
  }

  @Override
  public Vector3d getYawAxis() {
    return yaw;
  }

  @Override
  public double getYawAngle() {
    return yawAngle;
  }
}
