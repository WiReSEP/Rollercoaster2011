package de.rollercoaster.simulation;

import com.jme3.math.Vector3f;
import de.rollercoaster.graphics.View;
import de.rollercoaster.physics.Trajectory;
import de.rollercoaster.physics.TrajectoryPoint;
import java.awt.Canvas;

public class PrototypeSimulation implements Simulation {

    public final Trajectory physics;
    public final View graphics;
    public long lastTimeStepAt;

    public PrototypeSimulation(Trajectory physics, View graphics) {
        this.physics = physics;
        this.graphics = graphics;
    }

    public void start() {
        this.lastTimeStepAt = System.currentTimeMillis();
    }

    public void stop() {
    }

    public void update() {
        double timeStep = increaseTime();

        updatePhysics(timeStep);
        updateCameraPosition();
    }

    private double increaseTime() {
        long currentTime = System.currentTimeMillis();
        double timeStep = (currentTime - this.lastTimeStepAt) / 1000.0;
        this.lastTimeStepAt = currentTime;

        return timeStep;
    }

    private void updatePhysics(double timeStep) {
        physics.computeTimeStep(timeStep);
    }

    private void updateCameraPosition() {
        TrajectoryPoint state = physics.getState();

        Vector3f location = state.getPosition();
        Vector3f left = state.getPitchAxis();
        Vector3f up = state.getYawAxis();
        Vector3f direction = state.getRollAxis();

        graphics.setCamera(location, left, up, direction);
    }
}
