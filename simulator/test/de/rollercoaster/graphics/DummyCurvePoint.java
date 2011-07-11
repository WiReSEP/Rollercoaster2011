package de.rollercoaster.graphics;

import de.rollercoaster.mathematics.*;

import com.jme3.math.Vector3f;

/**Implementierung des CurvePoint. Zunächst als Dummy gedacht. Mitunter ist dies ein Kandidat der direkt weitergenutzt werden kann. 
<br> <br>
Dev: Umbenennung in einen richtigen CurvePoint?!
 */
public class DummyCurvePoint implements CurvePoint {
  private Vector3f pos = null;
  private Vector3f roll = null;
  private Vector3f pitch = null;
  private Vector3f yaw = null;

  public DummyCurvePoint(Vector3f pos, Vector3f roll, Vector3f pitch, Vector3f yaw) {
    this.pos = pos.clone();
    this.roll = roll.clone();
    this.pitch = pitch.clone();
    this.yaw = yaw.clone();
  }

  @Override
  public Vector3d getPosition() {
    return new Vector3d(pos.x, pos.y, pos.z);
  }

  @Override
  public Vector3d getRollAxis() {
    return new Vector3d(roll.x, roll.y, roll.z);
  }

  @Override
  public Vector3d getPitchAxis() {
    return new Vector3d(pitch.x, pitch.y, pitch.z);
  }

  @Override
  public Vector3d getYawAxis() {
    return new Vector3d(yaw.x, yaw.y, yaw.z);
  }

  //stubs
  @Override
  public double getYawAngle() {
    return 0;
  }

  public Vector3d getDerivative() {
    return null;
  }

  @Override
  public Vector3d getSecondDerivative() {
    return null;
  }
}
