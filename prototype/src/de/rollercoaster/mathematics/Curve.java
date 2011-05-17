package de.rollercoaster.mathematics;

import java.util.List;

public interface Curve {
    double getLength();
    CurvePoint getPoint(double length);
    List<CurvePoint> getPointSequence(double maxDistance, double maxAngle);
}
