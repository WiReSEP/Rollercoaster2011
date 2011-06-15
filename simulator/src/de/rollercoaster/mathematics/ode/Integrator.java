package de.rollercoaster.mathematics.ode;

public interface Integrator {
  double[] integrate(DifferentialEquations system, double t0, double[] x0, double t);
}
