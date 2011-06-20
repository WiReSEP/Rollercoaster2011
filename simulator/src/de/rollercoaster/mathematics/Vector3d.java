package de.rollercoaster.mathematics;

import com.jme3.math.Vector3f;

public class Vector3d {

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
        return divide(length());
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
}
