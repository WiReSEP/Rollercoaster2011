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
        this.position = position;
        this.derivative = derivative;
        this.secondDerivative = secondDerivative;

        this.yawAngle = yawAngle;

        this.roll = derivative.normalize();

        Vector3d up = GLOBAL_UP.subtract(roll.mult(roll.dot(GLOBAL_UP))).normalize();
        Vector3d left = roll.cross(up).normalize();
  
        this.yaw = up.mult(Math.cos(yawAngle)).add(left.mult(Math.sin(yawAngle))).normalize(); 
        this.pitch = roll.cross(yaw).normalize();

       /* if (Math.abs(roll.dot(roll) - 1.0) > 0.001) {
            throw new RuntimeException("Wrong Roll");
        }

        if (Math.abs(pitch.dot(pitch) - 1.0) > 0.001) {
            throw new RuntimeException("Wrong Roll");
        }

        if (Math.abs(yaw.dot(yaw) - 1.0) > 0.001) {
            throw new RuntimeException("Wrong Roll");
        }

        if (Math.abs(pitch.dot(yaw)) > 0.001) {
            throw new RuntimeException("Pith Yaw!");
        }

        if (Math.abs(pitch.dot(roll)) > 0.001) {
            throw new RuntimeException("Pitch Roll!");
        }

        if (Math.abs(roll.dot(yaw)) > 0.001) {
            throw new RuntimeException("Roll Yaw!");
        }*/

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
