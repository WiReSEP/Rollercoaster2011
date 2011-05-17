package de.rollercoaster.graphics;

import com.jme3.math.Vector3f;
import de.rollercoaster.mathematics.Curve;
import java.awt.Canvas;

public interface View {
    void setCamera(Vector3f position, Vector3f direction);
    void setCurve(Curve curve);
    
    void init();
    void dispose();
    
    Canvas getCanvas();
}
