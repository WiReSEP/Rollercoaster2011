/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rollercoaster.mathematics;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mangelsdorf
 */
public class LUDecompositionTest {
    /**
     * Test of solve method, of class LUDecomposition.
     */
    @Test
    public void testSolve() {
        System.out.println("solve");
        double[] b = {8.0, -11.0, -3.0};
        
        double[][] A = {
          {2.0, 1.0, -1.0},
          {-3.0, -1.0, 2.0},
          {-2.0, 1.0, 2.0}
        };
        
        LUDecomposition instance = new LUDecomposition(A);
        double[] expResult = {2.0, 3.0, -1.0};
        double[] result = instance.solve(b);
        assertArrayEquals(expResult, result, 0.0000001);
    }
}
