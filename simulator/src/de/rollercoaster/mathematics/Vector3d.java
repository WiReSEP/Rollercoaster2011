package de.rollercoaster.mathematics;

import com.jme3.math.Vector3f;

public class Vector3d {
  public final static Vector3d ZERO = new Vector3d(0.0, 0.0, 0.0);
  public final double x, y, z;

  public Vector3d(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector3d mult(double scalar) {
    return new Vector3d(x * scalar, y * scalar, z * scalar);
  }

  public Vector3d divide(double scalar) {
    return new Vector3d(x / scalar, y / scalar, z / scalar);
  }

  public double length() {
    return Math.sqrt(lengthSquared());
  }

  public double lengthSquared() {
    return x * x + y * y + z * z;
  }

  public Vector3d add(Vector3d other) {
    return new Vector3d(this.x + other.x, this.y + other.y, this.z + other.z);
  }

  public Vector3d subtract(Vector3d other) {
    return new Vector3d(this.x - other.x, this.y - other.y, this.z - other.z);
  }

  public double dot(Vector3d other) {
    return this.x * other.x + this.y * other.y + this.z * other.z;
  }

  public Vector3d cross(Vector3d other) {
    return new Vector3d(this.y * other.z - this.z * other.y, this.z * other.x - this.x * other.z, this.x * other.y - this.y * other.x);
  }

  public Vector3d normalize() {
    double l = length();
    return 0 == l ? ZERO : divide(l);
  }

  public Vector3f toF() {
    return new Vector3f((float) x, (float) y, (float) z);
  }

  @Override
  public String toString() {
    return "<" + x + "," + y + "," + z + ">";
  }

  static double cos(Vector3d x, Vector3d y) {
    if (x.lengthSquared() == 0 || y.lengthSquared() == 0) {
      return 0.0;
    }

    return x.dot(y) / (x.length() * y.length());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Vector3d other = (Vector3d) obj;
    if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
      return false;
    }
    if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
      return false;
    }
    if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 23 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
    hash = 23 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
    hash = 23 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
    return hash;
  }
}
