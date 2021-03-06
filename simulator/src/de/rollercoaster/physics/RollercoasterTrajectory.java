package de.rollercoaster.physics;

import de.rollercoaster.mathematics.*;
import de.rollercoaster.mathematics.ode.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RollercoasterTrajectory implements Trajectory, DifferentialEquations {

    public final static Vector3d GRAVITATION = new Vector3d(0, 9.81f, 0);
    private final Curve curve;
    private TrajectoryPoint state;
    private double[] positions;
    private double time;
    private Integrator integrator;
    private final double DEFAULT_STEP = 0.01;
    private final List<TrajectoryObserver> observers = Collections.synchronizedList(new LinkedList<TrajectoryObserver>());

    public RollercoasterTrajectory(Curve curve, double s0, double v0) {
        this.curve = curve;
        this.integrator = new RungeKutta(DEFAULT_STEP);

        CurvePoint startPoint = curve.getPoint(s0);

        this.positions = new double[]{s0, v0 / startPoint.getDerivative().length()};
        this.time = 0.0;

        updateState();
    }

    public double getEnergy() {
        return 0.5 * state.getVelocity().lengthSquared() + state.getPosition().dot(GRAVITATION);
    }

    @Override
    public TrajectoryPoint getState() {
        return state;
    }

    @Override
    public void computeTimeStep(double deltaTime) {
        positions = integrator.integrate(this, time, positions, time + deltaTime);
        time = time + deltaTime;

        updateState();
    }

    private void updateState() {
        TrajectoryPoint previousState = state;

        double[] derivatives = getDerivatives(time, positions);

        float s = (float) positions[0];
        float sDot = (float) derivatives[0];
        float sDotDot = (float) derivatives[1];

        CurvePoint point = curve.getPoint(s);
        Vector3d velocity = point.getDerivative().mult(sDot);
        Vector3d acceleration = point.getDerivative().mult(sDotDot).add(point.getSecondDerivative().mult(sDot * sDot));

        Vector3d jerk = Vector3d.ZERO;

        if (null != previousState) {
            double deltaTime = time - previousState.getTime();
            jerk = acceleration.subtract(previousState.getAcceleration()).divide(deltaTime);
        }

        state = new SimpleTrajectoryPoint(point, time, velocity, acceleration, jerk);

        notifyObservers(state);
    }

    @Override
    public double[] getDerivatives(double t, double[] x) {
        CurvePoint point = curve.getPoint(x[0]);

        Vector3d derivative = point.getDerivative();
        Vector3d secondDerivative = point.getSecondDerivative();

        double sDot = x[1];
        double sDotDot = -(derivative.dot(GRAVITATION) + sDot * sDot * derivative.dot(secondDerivative)) / (derivative.lengthSquared());

        return new double[]{sDot, sDotDot};
    }

    @Override
    public boolean addObserver(TrajectoryObserver observer) {
        return this.observers.add(observer);
    }

    @Override
    public boolean removeObserver(TrajectoryObserver observer) {
        return this.observers.remove(observer);
    }

    public void notifyObservers(TrajectoryPoint newState) {
        for (TrajectoryObserver observer : observers) {
            observer.update(newState);
        }
    }
}
