package de.rollercoaster.mathematics;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class BezierCurveTest {
    private BezierCurve testCurve;
    private Vector3d zeroPoint = new Vector3d(0.0, 0.0, 0.0);
    private CurvePoint first = new SimpleCurvePoint(
            new Vector3d(1.0, -2.0, 3.0), zeroPoint, zeroPoint, -1.0);
    
    private CurvePoint second = new SimpleCurvePoint(
            new Vector3d(2.0, 1.5, -1.0), zeroPoint, zeroPoint, -2.0);
    
    private CurvePoint third = new SimpleCurvePoint(
            new Vector3d(-1.0, +3.5, +2.0), zeroPoint, zeroPoint, 1.0);
    
    private CurvePoint fourth = new SimpleCurvePoint(
            new Vector3d(3.0, -0.5, -4.0), zeroPoint, zeroPoint, -3.0);
    
 
    public BezierCurveTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        List<Vector3d> points = new ArrayList<Vector3d>();
        
        points.add(zeroPoint);
        points.add(new Vector3d(1.0, 0.0, 0.0));
        points.add(new Vector3d(2.0, 0.0, 0.0));
        points.add(new Vector3d(3.0, 0.0, 0.0));
        
        List<Double> orientations = new ArrayList<Double>();
        
        orientations.add(0.0);
        orientations.add(1.0);
        orientations.add(2.0);
        orientations.add(3.0);
        
        this.testCurve = new BezierCurve(points, orientations);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetLength() {
        System.out.println("getLength");
        BezierCurve instance = testCurve;
        double expResult = 4.0;
        double result = instance.getLength();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getPoint method, of class BezierCurve.
     */
    @Test
    public void testGetPoint() {
        System.out.println("getPoint");
        BezierCurve instance = testCurve;
        
        CurvePoint result = instance.getPoint(0.0);
   
        assertEquals(zeroPoint, result.getPosition());
    }

    /**
     * Test of cubicInterpolation method, of class BezierCurve.
     */
    @Test
    public void testCubicInterpolation() {
        System.out.println("cubicInterpolation");
        double s = 0.25;
        CurvePoint expResult = new SimpleCurvePoint(
            new Vector3d(1.171875, 0.2734375, 1.0625), 
                new Vector3d(-0.9375,7.40625, -4.5),
                new Vector3d(-7.5, -15.75, 18.0), -1.171875);
        CurvePoint result = BezierCurve.cubicInterpolation(first, second, third, fourth, s);
        
        
        assertEquals(expResult.getPosition(), result.getPosition());
        assertEquals(expResult.getDerivative(), result.getDerivative());
        assertEquals(expResult.getSecondDerivative(), result.getSecondDerivative());
        assertEquals(expResult.getYawAngle(), result.getYawAngle(), 0.0);
    }

    /**
     * Test of getInterpolation method, of class BezierCurve.
     */
    @Test
    public void testGetInterpolation_5args_1() {
        System.out.println("getInterpolation");
       
        Vector3d p0 = new Vector3d(1.0, -2.0, 3.0);
        Vector3d p1 = new Vector3d(2.0, 1.5, -1.0);
        Vector3d p2 = new Vector3d(-1.0, +3.5, +2.0);
        Vector3d p3 = new Vector3d(3.0, -0.5, -4.0);
        double s = 0.75;
        Vector3d expResult = new Vector3d(1.140625, 1.4453125, -0.9375);
        Vector3d result = BezierCurve.getInterpolation(p0, p1, p2, p3, s);
        assertEquals(expResult, result);
    }

    /**
     * Test of getInterpolation method, of class BezierCurve.
     */
    @Test
    public void testGetInterpolation_5args_2() {
        System.out.println("getInterpolation");
        double p0 = -1.0;
        double p1 = -2.0;
        double p2 = 1.0;
        double p3 = 3.0;
        double s = 0.7;
        double expResult = 1.065;
        double result = BezierCurve.getInterpolation(p0, p1, p2, p3, s);
        assertEquals(expResult, result, 0.00001);
    }

    /**
     * Test of getDerivative method, of class BezierCurve.
     */
    @Test
    public void testGetDerivative() {
        System.out.println("getDerivative");
        Vector3d p0 = new Vector3d(1.0, -2.0, 3.0);
        Vector3d p1 = new Vector3d(2.0, 1.5, -1.0);
        Vector3d p2 = new Vector3d(-1.0, +3.5, +2.0);
        Vector3d p3 = new Vector3d(3.0, -0.5, -4.0);
        double s = 0.75;
        Vector3d expResult = new Vector3d(3.5625,-3.84375, -7.5);
        Vector3d result = BezierCurve.getDerivative(p0, p1, p2, p3, s);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSecondDerivative method, of class BezierCurve.
     */
    @Test
    public void testGetSecondDerivative() {
        System.out.println("getSecondDerivative");
        Vector3d p0 = new Vector3d(1.0, -2.0, 3.0);
        Vector3d p1 = new Vector3d(2.0, 1.5, -1.0);
        Vector3d p2 = new Vector3d(-1.0, +3.5, +2.0);
        Vector3d p3 = new Vector3d(3.0, -0.5, -4.0);
        double s = 0.75;
        Vector3d expResult = new Vector3d(25.5,-29.25, -30.0);;
        Vector3d result = BezierCurve.getSecondDerivative(p0, p1, p2, p3, s);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPointSequence method, of class BezierCurve.
     */
    @Test
    public void testGetPointSequence() {
        System.out.println("getPointSequence");
        double maxDistance = 0.2;
        double maxAngle = Math.PI/16;
        BezierCurve instance = testCurve;
        List<CurvePoint> result = instance.getPointSequence(maxDistance, maxAngle);
        
        for (int i=0;i<result.size() - 1; i++) {
            CurvePoint current = result.get(i);
            CurvePoint next = result.get(i+1);
            
            double angle = Math.acos(Math.abs(Vector3d.cos(current.getDerivative(), next.getDerivative())));
             
            assertTrue(angle < maxAngle);
            
            double distance = current.getPosition().subtract(next.getPosition()).length();
            
            assertTrue(distance < maxDistance);
            
        }
       
    }
}
