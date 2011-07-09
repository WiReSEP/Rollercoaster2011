package de.rollercoaster.physics;

import de.rollercoaster.mathematics.Vector3d;
import de.rollercoaster.mathematics.Curve;
import de.rollercoaster.mathematics.CurvePoint;
import de.rollercoaster.mathematics.SimpleCurvePoint;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RollercoasterTrajectoryTest {

    private final static double EPSILON = 0.00001;

    public static void assertEqualVectors(Vector3d obj1, Vector3d obj2, double delta) {
        if (obj1 == obj2) {
            return;
        } else if (obj1.subtract(obj2).length() <= delta) {
            return;
        } else {
            fail(" expected=" + obj1 + " actual=" + obj2 + " delta=" + delta);
        }
    }

    public static class LinearCurve implements Curve {

        private final Vector3d direction;

        public LinearCurve(Vector3d direction) {
            this.direction = direction.normalize();
        }

        @Override
        public double getLength() {
            return Double.POSITIVE_INFINITY;
        }

        @Override
        public CurvePoint getPoint(double l) {
            Vector3d position = direction.mult(l);
            return new SimpleCurvePoint(position, direction, Vector3d.ZERO, 0.0);
        }

        @Override
        public List<CurvePoint> getPointSequence(double maxDistance, double maxAngle) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Curve translate(Vector3d translation) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    private static Vector3d UP = new Vector3d(0.0, 1.0, 0.0);
    private static Vector3d FORCED = new Vector3d(0.0, 1.0, 1.0).normalize();
    private static Vector3d GRAVITATION = UP.mult(-9.81);
    private RollercoasterTrajectory freeFall;

    @Before
    public void setUp() {
        Curve linear = new LinearCurve(FORCED);
        freeFall = new RollercoasterTrajectory(linear, 0.0, 0.0);
    }

    /**
     * Test of getState method, of class RollercoasterTrajectory.
     */
    @Test
    public void testGetState() {
        System.out.println("getState");
        RollercoasterTrajectory instance = freeFall;
        TrajectoryPoint result = instance.getState();
        assertEqualVectors(Vector3d.ZERO, result.getPosition(), EPSILON);
        assertEqualVectors(FORCED, result.getDerivative(), EPSILON);
        assertEqualVectors(FORCED.mult(FORCED.dot(GRAVITATION)), result.getAcceleration(), EPSILON);
    }

    /**
     * Test of computeTimeStep method, of class RollercoasterTrajectory.
     */
    @Test
    public void testComputeTimeStep() {
        System.out.println("computeTimeStep");
        double deltaTime = 1.0;
        RollercoasterTrajectory instance = freeFall;
        instance.computeTimeStep(deltaTime);

        TrajectoryPoint result = instance.getState();
        assertEqualVectors(FORCED.mult(0.5 * FORCED.dot(GRAVITATION)), result.getPosition(), EPSILON);
    }

    /**
     * Test of getDerivatives method, of class RollercoasterTrajectory.
     */
    @Test
    public void testGetDerivatives() {
        System.out.println("getDerivatives");
        double t = 0.0;
        double[] x = null;
        RollercoasterTrajectory instance = null;
        double[] expResult = null;
        double[] result = instance.getDerivatives(t, x);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addObserver method, of class RollercoasterTrajectory.
     */
    @Test
    public void testAddObserver() {
        System.out.println("addObserver");
        TrajectoryObserver observer = new TrajectoryObserverMock();
        RollercoasterTrajectory instance = freeFall;

        boolean result = instance.addObserver(observer);
        assertEquals(true, result);

        result = instance.addObserver(observer);
        assertEquals("add dublicate", true, result);
    }

    /**
     * Test of removeObserver method, of class RollercoasterTrajectory.
     */
    @Test
    public void testRemoveObserver() {
        System.out.println("removeObserver");
        TrajectoryObserver observer = new TrajectoryObserverMock();
        RollercoasterTrajectory instance = freeFall;

        boolean result = instance.removeObserver(observer);
        assertEquals("remove non existing", false, result);


        instance.addObserver(observer);
        result = instance.removeObserver(observer);
        assertEquals("remove existing", true, result);

        result = instance.removeObserver(observer);
        assertEquals("remove again", false, result);
    }

    /**
     * Test of notifyObservers method, of class RollercoasterTrajectory.
     */
    @Test
    public void testNotifyObservers() {
        System.out.println("notifyObservers");
        TrajectoryObserverMock observer = new TrajectoryObserverMock();
        RollercoasterTrajectory instance = freeFall;
        TrajectoryPoint state = instance.getState();

        instance.addObserver(observer);


        instance.notifyObservers(state);
        boolean notified = observer.isNotified();

        assertEquals(true, notified);
        TrajectoryPoint result = observer.getLastReportedState();
        assertEquals(state, result);
    }
}
