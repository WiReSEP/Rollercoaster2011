package de.rollercoaster.physics;

public class TrajectoryObserverMock implements TrajectoryObserver {

    private TrajectoryPoint state;
    private long counter = 0;

    @Override
    public void update(TrajectoryPoint newState) {
        this.state = newState;
        this.counter++;
    }

    public boolean isNotified() {
        return counter > 0;
    }

    public long getNotificationCount() {
        return counter;
    }

    public TrajectoryPoint getLastReportedState() {
        return state;
    }
}
