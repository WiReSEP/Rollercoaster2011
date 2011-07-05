/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rollercoaster.simulation;

import de.rollercoaster.data.Track;
import de.rollercoaster.graphics.View;
import de.rollercoaster.physics.Trajectory;
import de.rollercoaster.physics.TrajectoryObserver;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mangelsdorf
 */
public class RollercoasterSimulationTest {
    
    public RollercoasterSimulationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of start method, of class RollercoasterSimulation.
     */
    @Test
    public void testStart() {
        System.out.println("start");
        RollercoasterSimulation instance = null;
        instance.start();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stop method, of class RollercoasterSimulation.
     */
    @Test
    public void testStop() {
        System.out.println("stop");
        RollercoasterSimulation instance = null;
        instance.stop();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTimeScale method, of class RollercoasterSimulation.
     */
    @Test
    public void testSetTimeScale() {
        System.out.println("setTimeScale");
        double timeScale = 0.0;
        RollercoasterSimulation instance = null;
        instance.setTimeScale(timeScale);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class RollercoasterSimulation.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        double timeStep = 0.0;
        RollercoasterSimulation instance = null;
        instance.update(timeStep);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addObserver method, of class RollercoasterSimulation.
     */
    @Test
    public void testAddObserver() {
        System.out.println("addObserver");
        TrajectoryObserver observer = null;
        RollercoasterSimulation instance = null;
        boolean expResult = false;
        boolean result = instance.addObserver(observer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeObserver method, of class RollercoasterSimulation.
     */
    @Test
    public void testRemoveObserver() {
        System.out.println("removeObserver");
        TrajectoryObserver observer = null;
        RollercoasterSimulation instance = null;
        boolean expResult = false;
        boolean result = instance.removeObserver(observer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTrajectory method, of class RollercoasterSimulation.
     */
    @Test
    public void testGetTrajectory() {
        System.out.println("getTrajectory");
        RollercoasterSimulation instance = null;
        Trajectory expResult = null;
        Trajectory result = instance.getTrajectory();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getView method, of class RollercoasterSimulation.
     */
    @Test
    public void testGetView() {
        System.out.println("getView");
        RollercoasterSimulation instance = null;
        View expResult = null;
        View result = instance.getView();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTrack method, of class RollercoasterSimulation.
     */
    @Test
    public void testSetTrack() {
        System.out.println("setTrack");
        Track track = null;
        RollercoasterSimulation instance = null;
        instance.setTrack(track);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
