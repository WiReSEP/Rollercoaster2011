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
import java.util.LinkedList;
import java.util.List;

public class RollercoasterSimulation implements Simulation, ViewObserver, TrajectoryObserver {
  private final List<TrajectoryObserver> observers = new LinkedList<TrajectoryObserver>();
  private Track track;
  private Trajectory physics;
  private View graphics;
  private double timeScale;
  private SimulationState state;

  public RollercoasterSimulation(Track track) {
    Curve curve = track.getCurve();
    double s0 = 0;
    double v0 = 20.0;

    this.track = track;
    this.graphics = new RollercoasterView(curve);
    this.physics = new RollercoasterTrajectory(curve, s0, v0);
    this.timeScale = 1.0;
    this.state = SimulationState.STOPPED;

    bind();
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
    if (!isRunning()) {
      return;
    }

    double scaledTimeStep = this.timeScale * timeStep;

    updatePhysics(scaledTimeStep);
    updateCameraPosition();
  }
    
  private void updatePhysics(double timeStep) {
    physics.computeTimeStep(timeStep);
  }

  private void updateCameraPosition() {
    TrajectoryPoint physicsState = physics.getState();

    Vector3f location = physicsState.getPosition().toF();
    Vector3f left = physicsState.getPitchAxis().toF();
    Vector3f up = physicsState.getYawAxis().toF();
    Vector3f direction = physicsState.getRollAxis().toF();

    graphics.setCamera(location, left, up, direction);
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

    this.track = track;
    Curve curve = track.getCurve();
    double s0 = 0;
    double v0 = 0.01; // track.getInitialVelocity();

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
