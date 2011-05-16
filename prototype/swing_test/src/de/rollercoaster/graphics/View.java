package de.rollercoaster.graphics;

import com.jme3.math.Vector3f;
import de.rollercoaster.mathematics.Curve;

public interface View {
    void setCamera(Vector3f position, Vector3f direction);
    void setCurve(Curve curve);
}
