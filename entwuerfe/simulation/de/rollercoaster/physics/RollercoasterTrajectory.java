package de.rollercoaster.physics;

import de.rollercoaster.mathematics.*;
import de.rollercoaster.mathematics.ode.*;

public class RollercoasterTrajectory implements Trajectory, DifferentialEquations {
  private final Curve curve;
  private TrajectoryPoint state;
  private double[] positions;
  private double time;
  private Vector3d gravitation;
  private Integrator integrator;
  private final double DEFAULT_MINIMAL_STEP = 0.01;

  public RollercoasterTrajectory(Curve curve, double s0, double v0) {
    this.curve = curve;
    this.gravitation = new Vector3d(0, 0, -9.81f);
    this.integrator = new RungeKutta(DEFAULT_MINIMAL_STEP);

    CurvePoint startPoint = curve.getPoint(s0);

    this.positions = new double[]{s0, v0 / startPoint.getPitchAxis().length()};
    this.time = 0.0;
  }

  @Override
  public TrajectoryPoint getState() {
    return state;
  }

  @Override
  public void computeTimeStep(double deltaTime) {
    positions = integrator.integrate(this, time, positions, time + deltaTime);
    time = time + deltaTime;

    updateState(deltaTime);
  }

  private void updateState(double deltaTime) {
    TrajectoryPoint previousState = state;

    double[] derivatives = getDerivatives(time, positions);

    float s = (float) positions[0];
    float sDerivative = (float) derivatives[0];
    float sDotDot = (float) derivatives[1];

    CurvePoint point = curve.getPoint(s);
    Vector3d velocity = point.getDerivative().mult(sDerivative);
    Vector3d acceleration = point.getDerivative().mult(sDotDot).add(point.getSecondDerivative().mult(sDerivative * sDerivative));
    Vector3d jerk = acceleration.subtract(previousState.getAcceleration()).divide(deltaTime);

    state = new SimpleTrajectoryPoint(point, velocity, acceleration, jerk);
  }

  @Override
  public double[] getDerivatives(double t, double[] x) {
    CurvePoint point = curve.getPoint(x[0]);

    Vector3d derivative = point.getDerivative();
    Vector3d secondDerivative = point.getSecondDerivative();

    double sDot = x[1];
    double sDotDot = -(derivative.dot(gravitation) + sDot * sDot * derivative.dot(secondDerivative)) / (derivative.lengthSquared());

    return new double[]{sDot, sDotDot};
  }
}
