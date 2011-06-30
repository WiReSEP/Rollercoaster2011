package de.rollercoaster.mathematics.ode;

public class RungeKutta implements Integrator {
  private final double maximalStep;

  public RungeKutta(double maximalStep) {
    this.maximalStep = maximalStep;
  }

  @Override
  public double[] integrate(DifferentialEquations system, double t0, double[] x0, double t1) {
    double t = t0;
    double[] x = x0;

    while (t < t1) {
      double dt = t1 - t;

      if (dt > maximalStep) {
        dt = maximalStep;
      }

      double[] k1 = system.getDerivatives(t, x);
      double[] k2 = system.getDerivatives(t + 0.5 * dt, linearCombination(x, 0.5 * dt, k1));
      double[] k3 = system.getDerivatives(t + 0.5 * dt, linearCombination(x, 0.5 * dt, k2));
      double[] k4 = system.getDerivatives(t + dt, linearCombination(x, dt, k3));

      for (int i = 0; i < x0.length; i++) {
        x[i] += dt / 6.0 * (k1[i] + 2 * k2[i] + 2 * k3[i] + k4[i]);
      }
      
      t += dt;
    }

    return x;
  }

  private static double[] linearCombination(double[] x, double s, double[] k) {
    double[] y = new double[x.length];

    for (int i = 0; i < x.length; i++) {
      y[i] = x[i] + s * k[i];
    }

    return y;
  }
}
