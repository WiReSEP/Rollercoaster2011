package de.rollercoaster.simulation;

import com.jme3.math.Vector3f;
import de.rollercoaster.data.Track;
import de.rollercoaster.graphics.RollercoasterView;
import de.rollercoaster.graphics.View;
import de.rollercoaster.graphics.ViewObserver;
import de.rollercoaster.mathematics.Curve;
import de.rollercoaster.mathematics.CurvePoint;
import de.rollercoaster.mathematics.Vector3d;
import de.rollercoaster.physics.RollercoasterTrajectory;
import de.rollercoaster.physics.Trajectory;
import de.rollercoaster.physics.TrajectoryObserver;
import de.rollercoaster.physics.TrajectoryPoint;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RollercoasterSimulation implements Simulation, ViewObserver, TrajectoryObserver {

    private final List<TrajectoryObserver> observers = new LinkedList<TrajectoryObserver>();
    private Track track;
    private Trajectory physics;
    private View graphics;
    private double timeScale;
    private SimulationState state;
    private TrajectoryPoint physicsState;

    public RollercoasterSimulation(Track track) {
        Curve curve = track.getCurve();
        
        Vector3d displacement = calculateDisplacement(curve);
        
       // if (!displacement.equals(Vector3d.ZERO)) {
       //     curve = curve.translate(displacement);
       // }
        
        double s0 = 0.0; // calculateStartPosition(curve);
        double v0 = track.getInitialSpeed();
        
        this.track = track;
        this.graphics = new RollercoasterView(curve);
        this.physics = new RollercoasterTrajectory(curve, s0, v0);
        this.timeScale = 1.0;
        this.state = SimulationState.STOPPED;

        bind();
    }

    private Vector3d calculateDisplacement(Curve curve) {
        double length = curve.getLength();
        double position = 0.0;

        double heightOverZero = Double.POSITIVE_INFINITY;;

        while (position < length) {
            CurvePoint point = curve.getPoint(position);
            double height = point.getPosition().y;


            if (height < heightOverZero) {
                heightOverZero = height;
            }

            position += 0.1;
        }

        if (heightOverZero < 50.0) {
            return new Vector3d(0.0, 50.0 - heightOverZero, 0.0);
        } else {
            return Vector3d.ZERO;
        }
    }

    private double calculateStartPosition(Curve curve) {
        double length = curve.getLength();
        double position = 0.0;

        double s0 = 0.0;
        double maximalEnergy = Double.NEGATIVE_INFINITY;

        while (position < length) {
            CurvePoint point = curve.getPoint(position);
            double energy = point.getPosition().dot(RollercoasterTrajectory.GRAVITATION);


            if (energy > maximalEnergy) {
                maximalEnergy = energy;
                s0 = position;
            }

            position += 0.01;
        }

        return s0;
    }

    @Override
    public SimulationState getState() {
        return this.state;
    }

    @Override
    public synchronized void start() {
        if (this.state != SimulationState.RUNNING) {
            this.state = SimulationState.RUNNING;
        }
    }

    @Override
    public synchronized void stop() {
        if (this.state != SimulationState.STOPPED) {
            this.state = SimulationState.STOPPED;
            reset();
        }
    }

    @Override
    public synchronized void pause() {
        if (this.state != SimulationState.PAUSED) {
            this.state = SimulationState.PAUSED;
        }
    }

    @Override
    public void reset() {
        setTrack(this.track);
    }

    @Override
    public boolean isRunning() {
        return SimulationState.RUNNING.equals(this.state);
    }

    @Override
    public boolean isStopped() {
        return SimulationState.STOPPED.equals(this.state);
    }

    @Override
    public boolean isPaused() {
        return SimulationState.PAUSED.equals(this.state);
    }

    @Override
    public double getTimeScale() {
        return this.timeScale;
    }

    @Override
    public void setTimeScale(double timeScale) {
        this.timeScale = timeScale;
    }

    @Override
    public void update(double timeStep) {
        if (isRunning()) {
            double scaledTimeStep = this.timeScale * timeStep;
            updatePhysics(scaledTimeStep);
        }

        updateCameraPosition();
    }

    private void updatePhysics(double timeStep) {
        physics.computeTimeStep(timeStep);
    }

    private void updateCameraPosition() {
        TrajectoryPoint previousState = physicsState;
        physicsState = physics.getState();

        if (physicsState != previousState) {
            Vector3f location = physicsState.getPosition().toF();
            Vector3f left = physicsState.getPitchAxis().toF();
            Vector3f up = physicsState.getYawAxis().toF();
            Vector3f direction = physicsState.getRollAxis().toF();
            Vector3f yaw = physicsState.getYawAxis().toF();
            Vector3f acceleration = physicsState.getAcceleration().toF();

            graphics.setCamera(location, left, up, direction);
            graphics.setHUDData(direction, yaw, acceleration);
        }
    }

    @Override
    public boolean addObserver(TrajectoryObserver observer) {
        synchronized (observers) {
            return this.observers.add(observer);

        }
    }

    @Override
    public boolean removeObserver(TrajectoryObserver observer) {
        synchronized (observers) {
            return this.observers.remove(observer);
        }
    }

    @Override
    public void update(TrajectoryPoint newState) {
        synchronized (observers) {
            for (TrajectoryObserver observer : observers) {
                observer.update(newState);
            }
        }
    }

    @Override
    public Trajectory getTrajectory() {
        return physics;
    }

    @Override
    public View getView() {
        return graphics;
    }

    @Override
    public void setTrack(Track track) {
        unbind();

        this.state = null;
        this.track = track;
        Curve curve = track.getCurve();
        double s0 = 0; // calculateStartPosition(curve);
        double v0 = track.getInitialSpeed();

        graphics.setCurve(curve);
        physics = new RollercoasterTrajectory(curve, s0, v0);

        bind();

        updateCameraPosition();
    }

    private void bind() {
        this.graphics.addObserver(this);
        this.physics.addObserver(this);
    }

    private void unbind() {
        this.physics.removeObserver(this);
        this.graphics.removeObserver(this);
    }
}
