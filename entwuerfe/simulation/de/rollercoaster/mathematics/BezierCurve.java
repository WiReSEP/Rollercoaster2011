package de.rollercoaster.mathematics;

import java.util.List;

public class BezierCurve implements Curve {
  private final List<CurvePoint> controlPoints;
  private final double length;

  public BezierCurve(List<Vector3d> interpolationPoints, List<Vector3d> orientations) {
    this.controlPoints = calculateControlPoints(interpolationPoints, orientations);
    this.length = interpolationPoints.size() - 1;
  }

  @Override
  public double getLength() {
    return length;
  }

  @Override
  public CurvePoint getPoint(double length) {
    CurvePoint p0, p1, p2, p3;

    int idx = (int) length;

    p0 = controlPoints.get(idx);
    p1 = controlPoints.get(idx + 1);
    p2 = controlPoints.get(idx + 2);
    p3 = controlPoints.get(idx + 3);

    double s = length - idx;

    return cubicInterpolation(p0, p1, p2, p3, s);
  }

  public static CurvePoint cubicInterpolation(CurvePoint p0, CurvePoint p1, CurvePoint p2, CurvePoint p3, double s) {
    Vector3d position = getInterpolation(p0.getPosition(), p1.getPosition(), p2.getPosition(), p3.getPosition(), s);
    Vector3d up = getInterpolation(p0.getYawAxis(), p1.getYawAxis(), p2.getYawAxis(), p3.getYawAxis(), s);
    Vector3d derivative = getDerivative(p0.getPosition(), p1.getPosition(), p2.getPosition(), p3.getPosition(), s);
    Vector3d secondDerivative = getSecondDerivative(p0.getPosition(), p1.getPosition(), p2.getPosition(), p3.getPosition(), s);

    return new SimpleCurvePoint(position, derivative, secondDerivative, up);
  }

  public static Vector3d getInterpolation(Vector3d p0, Vector3d p1, Vector3d p2, Vector3d p3, double s) {
    double s0 = (1 - s) * (1 - s) * (1 - s);
    double s1 = 3.0 * (1 - s) * (1 - s) * s;
    double s2 = 3.0 * (1 - s) * s * s;
    double s3 = s * s * s;

    double x = (s0 * p0.x + s1 * p1.x + s2 * p2.x + s3 * p3.x);
    double y = (s0 * p0.y + s1 * p1.y + s2 * p2.y + s3 * p3.y);
    double z = (s0 * p0.z + s1 * p1.z + s2 * p2.z + s3 * p3.z);

    return new Vector3d(x, y, z);
  }

  public static Vector3d getDerivative(Vector3d p0, Vector3d p1, Vector3d p2, Vector3d p3, double s) {
    double t0 = -3.0 * (1 - s) * (1 - s);
    double t1 = 3.0 * (1 - s) * (1 - s) - 6.0 * (1 - s) * s;
    double t2 = 6.0 * (1 - s) * s - 3.0 * s * s;
    double t3 = 3.0 * s * s;

    double x = (t0 * p0.x + t1 * p1.x + t2 * p2.x + t3 * p3.x);
    double y = (t0 * p0.y + t1 * p1.y + t2 * p2.y + t3 * p3.y);
    double z = (t0 * p0.z + t1 * p1.z + t2 * p2.z + t3 * p3.z);

    return new Vector3d(x, y, z);
  }

  public static Vector3d getSecondDerivative(Vector3d p0, Vector3d p1, Vector3d p2, Vector3d p3, double s) {
    double t0 = 6.0 * (1 - s);
    double t1 = -12.0 * (1 - s) + 6.0 * s;
    double t2 = 6.0 * (1 - s) - 12.0 * s;
    double t3 = 6.0 * s;

    double x = (t0 * p0.x + t1 * p1.x + t2 * p2.x + t3 * p3.x);
    double y = (t0 * p0.y + t1 * p1.y + t2 * p2.y + t3 * p3.y);
    double z = (t0 * p0.z + t1 * p1.z + t2 * p2.z + t3 * p3.z);

    return new Vector3d(x, y, z);
  }

  @Override
  public List<CurvePoint> getPointSequence(double maxDistance, double maxAngle) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private List<CurvePoint> calculateControlPoints(List<Vector3d> interpolationPoints, List<Vector3d> orientations) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
