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

  public SimpleCurvePoint(Vector3d position, Vector3d derivative, Vector3d secondDerivative, Vector3d up) {
    this.position = position;
    this.derivative = derivative;
    this.secondDerivative = secondDerivative;

    this.roll = derivative.normalize();
    this.pitch = derivative.cross(up).normalize();
    this.yaw = up.normalize();
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
}
