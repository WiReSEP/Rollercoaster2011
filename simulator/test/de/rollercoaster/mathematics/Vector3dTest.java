/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rollercoaster.mathematics;

import com.jme3.math.Vector3f;
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
public class Vector3dTest {
    
    public Vector3dTest() {
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
     * Test of mult method, of class Vector3d.
     */
    @Test
    public void testMult() {
        System.out.println("mult");
        double scalar = -1.5;
        Vector3d instance = new Vector3d(1.0, -2.0, 3.5);
        Vector3d expResult = new Vector3d(-1.5, 3.0, -5.25);
        Vector3d result = instance.mult(scalar);
        assertEquals(expResult, result);
    }

    /**
     * Test of divide method, of class Vector3d.
     */
    @Test
    public void testDivide() {
        System.out.println("divide");
        double scalar = -2.0;
        Vector3d instance = new Vector3d(1.0, -2.0, 3.5);
        Vector3d expResult = new Vector3d(-0.5, 1.0, -1.75);
        Vector3d result = instance.divide(scalar);
        assertEquals(expResult, result);
    }

    /**
     * Test of length method, of class Vector3d.
     */
    @Test
    public void testLength() {
        System.out.println("length");
        Vector3d instance = new Vector3d(-2.0, 3.0, -6.0);
        double expResult = 7.0;
        double result = instance.length();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of lengthSquared method, of class Vector3d.
     */
    @Test
    public void testLengthSquared() {
        System.out.println("lengthSquared");
        Vector3d instance = new Vector3d(6.0, -3.0, 2.0);
        double expResult = 49.0;
        double result = instance.lengthSquared();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of add method, of class Vector3d.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        Vector3d other = new Vector3d(1.0, -2.0, 3.0);
        Vector3d instance = new Vector3d(0.0, 1.0, 2.0);
        Vector3d expResult = new Vector3d(1.0, -1.0, 5.0);
        Vector3d result = instance.add(other);
    }

    /**
     * Test of subtract method, of class Vector3d.
     */
    @Test
    public void testSubtract() {
        System.out.println("subtract");
        Vector3d other = new Vector3d(1.0, -2.0, 3.0);
        Vector3d instance = new Vector3d(0.0, 1.0, 2.0);
        Vector3d expResult = new Vector3d(-1.0, 3.0, -1.0);
        Vector3d result = instance.subtract(other);
        assertEquals(expResult, result);
    }

    /**
     * Test of dot method, of class Vector3d.
     */
    @Test
    public void testDot() {
        System.out.println("dot");
        Vector3d other = new Vector3d(1.0, -2.0, 3.0);
        Vector3d instance = new Vector3d(-1.0, 1.0, 2.0);
        double expResult = 3.0;
        double result = instance.dot(other);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of cross method, of class Vector3d.
     */
    @Test
    public void testCross() {
        System.out.println("cross");
        Vector3d other = new Vector3d(1.0, -2.0, 3.0);
        Vector3d instance = new Vector3d(-1.5, 0.5, 2.5);
        Vector3d expResult = new Vector3d(6.5, 7.0, 2.5);
        Vector3d result = instance.cross(other);
        assertEquals(expResult, result);
    }

    /**
     * Test of normalize method, of class Vector3d.
     */
    @Test
    public void testNormalize() {
        System.out.println("normalize");
        Vector3d instance = new Vector3d(0.0, -3.0, 4.0);
        Vector3d expResult = new Vector3d(0.0, -0.6, 0.8);
        Vector3d result = instance.normalize();
        assertEquals(expResult, result);
    }

    /**
     * Test of toF method, of class Vector3d.
     */
    @Test
    public void testToF() {
        System.out.println("toF");
        Vector3d instance = new Vector3d(1.0,-2.0, 3.0);
        Vector3f expResult = new Vector3f(1.0f, -2.0f, 3.0f);
        Vector3f result = instance.toF();
        assertEquals(expResult, result);
    }

    /**
     * Test of cos method, of class Vector3d.
     */
    @Test
    public void testCos() {
        System.out.println("cos");
        Vector3d x = new Vector3d(1.0, -1.0, 2.0);
        Vector3d y = new Vector3d(-1.0, 1.0, 2.0);
        double expResult = 1.0/3.0;
        double result = Vector3d.cos(x, y);
        assertEquals(expResult, result, 0.001);
    }

    /**
     * Test of equals method, of class Vector3d.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = new Vector3d(1.0, 2.0, -3.0);
        Vector3d instance = new Vector3d(1.0, 2.0, -3.0);
        
        assertEquals(instance, obj);
       
        Object obj2 = new Vector3d(1.0, -2.0, 3.0);
        assertNotSame(instance, obj2);
    }

    /**
     * Test of hashCode method, of class Vector3d.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Vector3d other = new Vector3d(1.0, 2.0, -3.0);
        Vector3d instance = new Vector3d(1.0, 2.0, -3.0);
       
        assertEquals(instance.hashCode(), other.hashCode());
    }
}
