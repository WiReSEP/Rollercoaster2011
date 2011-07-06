package de.rollercoaster.simulation;

import de.rollercoaster.data.Track;
import de.rollercoaster.graphics.View;
import de.rollercoaster.physics.Trajectory;
import de.rollercoaster.physics.TrajectoryObserver;

public interface Simulation {

    void start();
    void stop();    
    void pause();    
    void reset(); 
    
    boolean isRunning();
    boolean isPaused();
                
    SimulationState getState();
    
    double getTimeScale();
    void setTimeScale(double timeScale);
  
    boolean addObserver(TrajectoryObserver observer);

    boolean removeObserver(TrajectoryObserver observer);
    
    public Trajectory getTrajectory();
    
    public View getView();
    
    public void setTrack(Track track);
}
