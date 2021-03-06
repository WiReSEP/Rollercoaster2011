package de.rollercoaster.mathematics;

import java.util.ArrayList;
import java.util.List;

public class BezierCurve implements Curve {
  private final double MAXIMAL_DISTANCE_LOWER_LIMIT = 1.0;
  private final double DEFAULT_DELTA = 0.02;
  private final double MINIMAL_DELTA = DEFAULT_DELTA / 4;
  private final List<CurvePoint> controlPoints;
  private final double length;
  private double MAXIMAL_ANGLE_LOWER_LIMIT = Math.PI / 72.0;

  public BezierCurve(List<Vector3d> interpolationPoints, List<Vector3d> orientations) {
    this.controlPoints = calculateControlPoints(interpolationPoints, orientations);
    this.length = interpolationPoints.size();
  }

  private BezierCurve(List<CurvePoint> controlPoints) {
    this.controlPoints = controlPoints;
    this.length = (controlPoints.size() - 1) / 3;
  }

  @Override
  public double getLength() {
    return length;
  }

  @Override
  public CurvePoint getPoint(double position) {
    CurvePoint p0, p1, p2, p3;

    position = position % length;
    if (position < 0) {
      position += length;
    }


    int s0 = (int) position;
    int idx = 3 * s0;

    p0 = controlPoints.get(idx);
    p1 = controlPoints.get(idx + 1);
    p2 = controlPoints.get(idx + 2);
    p3 = controlPoints.get(idx + 3);

    double s = position - s0;

    return cubicInterpolation(p0, p1, p2, p3, s);
  }

  public static CurvePoint cubicInterpolation(CurvePoint p0, CurvePoint p1, CurvePoint p2, CurvePoint p3, double s) {
    Vector3d position = getInterpolation(p0.getPosition(), p1.getPosition(), p2.getPosition(), p3.getPosition(), s);
    Vector3d derivative = getDerivative(p0.getPosition(), p1.getPosition(), p2.getPosition(), p3.getPosition(), s);
    Vector3d secondDerivative = getSecondDerivative(p0.getPosition(), p1.getPosition(), p2.getPosition(), p3.getPosition(), s);

    Vector3d yawAxis = getInterpolation(p0.getYawAxis(), p1.getYawAxis(), p2.getYawAxis(), p3.getYawAxis(), s);

    return new SimpleCurvePoint(position, derivative, secondDerivative, yawAxis);
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

  public static double getInterpolation(double p0, double p1, double p2, double p3, double s) {
    double s0 = (1 - s) * (1 - s) * (1 - s);
    double s1 = 3.0 * (1 - s) * (1 - s) * s;
    double s2 = 3.0 * (1 - s) * s * s;
    double s3 = s * s * s;

    double p = (s0 * p0 + s1 * p1 + s2 * p2 + s3 * p3);

    return p;
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
    double position = 0.0;
    double delta = DEFAULT_DELTA;

    if (maxDistance < MAXIMAL_DISTANCE_LOWER_LIMIT) {
      maxDistance = MAXIMAL_DISTANCE_LOWER_LIMIT;
    }

    if (maxAngle < MAXIMAL_ANGLE_LOWER_LIMIT) {
      maxAngle = MAXIMAL_ANGLE_LOWER_LIMIT;
    }

    List<CurvePoint> points = new ArrayList<CurvePoint>();

    CurvePoint previous = getPoint(0.0);

    while (position < length) {
      CurvePoint current = getPoint(position + delta);
      double distance = current.getPosition().subtract(previous.getPosition()).length();
      double angle = Math.acos(Math.abs(Vector3d.cos(current.getDerivative(), previous.getDerivative())));

      if ((distance <= maxDistance && angle <= maxAngle) || delta < MINIMAL_DELTA) {
        if (position + delta < length) {
          points.add(current);
          //System.out.println(position + ";" + previous.getPosition().x+";"+ previous.getPosition().y + ";" + previous.getPosition().z);          
        }

        previous = current;
        position += delta;
        delta = DEFAULT_DELTA;
      }
      else {
        delta /= 2.0;
      }

    }

    return points;
  }

  private static List<CurvePoint> calculateControlPoints(List<Vector3d> positions, List<Vector3d> orientations) {
    final int n = positions.size();

    List<CurvePoint> controlPoints = new ArrayList<CurvePoint>(n);

    LUDecomposition decomposition = prepareDecomposition(n);

    List<Vector3d> cp = calculateControlPoints(decomposition, positions);
    List<Vector3d> op = calculateControlPoints(decomposition, orientations);

    for (int i = 0; i < cp.size(); i++) {
      controlPoints.add(new SimpleCurvePoint(cp.get(i), Vector3d.ZERO, Vector3d.ZERO, op.get(i)));
    }

    return controlPoints;
  }

  private static List<Double> calculateControlPointsOneDimension(LUDecomposition decomposition, List<Double> points) {
    int n = points.size();
    List<Double> controlPoints = new ArrayList<Double>();

    double[] b = new double[n * 2];
    double[] by = new double[n * 2];
    double[] bz = new double[n * 2];

    for (int i = 0; i < n; i++) {
      b[2 * i] = 2.0 * points.get((i + 1) % n);
      b[2 * i + 1] = 0.0;
    }

    double[] x = decomposition.solve(b);


    for (int i = 0; i < n; i++) {
      controlPoints.add(points.get(i));
      controlPoints.add(x[2 * i]);
      controlPoints.add(x[2 * i + 1]);
    }

    controlPoints.add(points.get(0));

    return controlPoints;
  }

  private static List<Vector3d> calculateControlPoints(LUDecomposition decomposition, List<Vector3d> points) {
    int n = points.size();
    List<Vector3d> controlPoints = new ArrayList<Vector3d>();

    double[] bx = new double[n * 2];
    double[] by = new double[n * 2];
    double[] bz = new double[n * 2];

    for (int i = 0; i < n; i++) {
      bx[2 * i] = 2.0 * points.get((i + 1) % n).x;
      bx[2 * i + 1] = 0.0;
      by[2 * i] = 2.0 * points.get((i + 1) % n).y;
      by[2 * i + 1] = 0.0;
      bz[2 * i] = 2.0 * points.get((i + 1) % n).z;
      bz[2 * i + 1] = 0.0;
    }

    double[] xx = decomposition.solve(bx);
    double[] xy = decomposition.solve(by);
    double[] xz = decomposition.solve(bz);


    for (int i = 0; i < n; i++) {
      controlPoints.add(points.get(i));
      controlPoints.add(new Vector3d(xx[2 * i], xy[2 * i], xz[2 * i]));
      controlPoints.add(new Vector3d(xx[2 * i + 1], xy[2 * i + 1], xz[2 * i + 1]));
    }

    controlPoints.add(points.get(0));

    return controlPoints;
  }

  private static LUDecomposition prepareDecomposition(int n) {
    double[][] A = new double[2 * n][2 * n];

    for (int i = 0; i < n; i++) {

      int left = 2 * i;
      int right = (2 * i + 2) % (2 * n);

      A[2 * i][left + 1] = 1.0;
      A[2 * i][right] = 1.0;

      A[2 * i + 1][left] = 1.0;
      A[2 * i + 1][left + 1] = -2.0;
      A[2 * i + 1][right] = 2.0;
      A[2 * i + 1][right + 1] = -1.0;
    }

    return new LUDecomposition(A);
  }

  private CurvePoint translate(CurvePoint point, Vector3d translation) {
    return new SimpleCurvePoint(point.getPosition().add(translation), point.getDerivative(),
            point.getSecondDerivative(), point.getYawAxis());
  }

  @Override
  public Curve translate(Vector3d translation) {
    List<CurvePoint> translatedPoints = new ArrayList<CurvePoint>(controlPoints.size());

    for (CurvePoint point : controlPoints) {
      translatedPoints.add(translate(point, translation));
    }

    return new BezierCurve(translatedPoints);
  }
}
