package de.rollercoaster.mathematics.ode;

/**
 *
 * @author mangelsdorf
 */
public interface DifferentialEquations {
  double[] getDerivatives(double t, double[] x);
}
