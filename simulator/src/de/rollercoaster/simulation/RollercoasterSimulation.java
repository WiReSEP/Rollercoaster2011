package de.rollercoaster.simulation;

import com.jme3.math.Vector3f;
import de.rollercoaster.data.Track;
import de.rollercoaster.graphics.RollercoasterView;
import de.rollercoaster.graphics.View;
import de.rollercoaster.graphics.ViewObserver;
import de.rollercoaster.mathematics.Curve;
import de.rollercoaster.physics.RollercoasterTrajectory;
import de.rollercoaster.physics.Trajectory;
import de.rollercoaster.physics.TrajectoryObserver;
import de.rollercoaster.physics.TrajectoryPoint;

public class RollercoasterSimulation implements Simulation, ViewObserver {

    public Trajectory physics;
    public View graphics;
    public double timeScale;
     
    public RollercoasterSimulation(Track track) {
        Curve curve = track.getCurve();
        double s0 = 0;
        double v0 = 20.0;

        this.graphics = new RollercoasterView(curve);
        this.physics = new RollercoasterTrajectory(curve, s0, v0);
        this.timeScale = 1.0;
    }

    @Override
    public void start() {
        this.graphics.addObserver(this);
    }

    @Override
    public void stop() {
        this.graphics.removeObserver(this);
    }

    public void setTimeScale(double timeScale) {
        this.timeScale = timeScale;
    }

    @Override
    public void update(double timeStep) {
        double scaledTimeStep = this.timeScale * timeStep;

        updatePhysics(scaledTimeStep);
        updateCameraPosition();
    }

    private void updatePhysics(double timeStep) {
        physics.computeTimeStep(timeStep);
    }

    private void updateCameraPosition() {
        TrajectoryPoint state = physics.getState();

        Vector3f location = state.getPosition().toF();
        Vector3f left = state.getPitchAxis().toF();
        Vector3f up = state.getYawAxis().toF();
        Vector3f direction = state.getRollAxis().toF();

        graphics.setCamera(location, left, up, direction);
    }

    @Override
    public boolean addObserver(TrajectoryObserver observer) {
        return physics.addObserver(observer);
    }

    @Override
    public boolean removeObserver(TrajectoryObserver observer) {
        return physics.removeObserver(observer);
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
        Curve curve = track.getCurve();
        double s0 = 0;
        double v0 = 0.01; // track.getInitialVelocity();

        graphics.setCurve(curve);
        physics = new RollercoasterTrajectory(curve, s0, v0);
    }
}
