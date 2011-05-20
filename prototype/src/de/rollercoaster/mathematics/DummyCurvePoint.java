package de.rollercoaster.mathematics;

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

    public DummyCurvePoint(Vector3f pos, Vector3f roll, Vector3f pitch, Vector3f yaw ) {
      this.pos   = pos.clone();
      this.roll  = roll.clone();
      this.pitch = pitch.clone();
      this.yaw   = yaw.clone();
    }

    public Vector3f getPosition() {
      return pos.clone();
    }

    public Vector3f getRollAxis(){
      return roll.clone();
    }

    public Vector3f getPitchAxis(){
      return pitch.clone();
    }

    public Vector3f getYawAxis(){
      return yaw.clone();
    }

}
 
