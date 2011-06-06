package de.rollercoaster.simulation;

import com.jme3.math.Vector3f;
import de.rollercoaster.graphics.View;
import de.rollercoaster.graphics.ViewObserver;
import de.rollercoaster.physics.Trajectory;
import de.rollercoaster.physics.TrajectoryPoint;

public class RollercoasterSimulation implements Simulation, ViewObserver {
  public final Trajectory physics;
  public final View graphics;
  public double timeScale;
  public double currentTime;

  public RollercoasterSimulation(Trajectory physics, View graphics) {
    this.physics = physics;
    this.graphics = graphics;
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
  public void update(float timeStep) {
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
}
