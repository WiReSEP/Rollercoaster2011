package de.rollercoaster.simulation;

import de.rollercoaster.physics.TrajectoryObserver;

public interface Simulation {
  void start();
  void stop();

  boolean addObserver(TrajectoryObserver observer);
  boolean removeObserver(TrajectoryObserver observer);
}
