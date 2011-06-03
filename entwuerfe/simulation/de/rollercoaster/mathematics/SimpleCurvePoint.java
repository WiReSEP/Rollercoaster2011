package de.rollercoaster.mathematics;

import com.jme3.math.Vector3f;

/** 
 * Einfache Implementierung eines unveränderlichen <tt>CurvePoint</tt>
 * @author mangelsdorf
 */
public class SimpleCurvePoint implements CurvePoint {
  private final Vector3f position;
  private final Vector3f derivative;
  private final Vector3f secondDerivative;
  private final Vector3f roll;
  private final Vector3f pitch;
  private final Vector3f yaw;

  public SimpleCurvePoint(Vector3f position, Vector3f derivative, Vector3f secondDerivative, Vector3f up) {
    this.position = new Vector3f(position);
    this.derivative = new Vector3f(derivative);
    this.secondDerivative = new Vector3f(secondDerivative);

    this.roll = derivative.normalize();
    this.pitch = new Vector3f(derivative.cross(up).normalize());
    this.yaw = new Vector3f(up);
  }

  @Override
  public Vector3f getPosition() {
    return new Vector3f(position);
  }

  @Override
  public Vector3f getDerivative() {
    return new Vector3f(derivative);
  }

  @Override
  public Vector3f getSecondDerivative() {
    return new Vector3f(secondDerivative);
  }

  @Override
  public Vector3f getRollAxis() {
    return new Vector3f(roll);
  }

  @Override
  public Vector3f getPitchAxis() {
    return new Vector3f(pitch);
  }

  @Override
  public Vector3f getYawAxis() {
    return new Vector3f(yaw);
  }
}
