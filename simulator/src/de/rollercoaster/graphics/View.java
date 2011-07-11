package de.rollercoaster.graphics;

import com.jme3.math.Vector3f;
import de.rollercoaster.mathematics.Curve;
import java.awt.Canvas;
import java.io.FileNotFoundException;

public interface View {

    void setCamera(Vector3f location, Vector3f left, Vector3f up, Vector3f direction);

    void setCurve(Curve curve);

    void init();

    void dispose();

    Canvas getCanvas();

    boolean addObserver(ViewObserver observer);

    boolean removeObserver(ViewObserver observer);

    void setHUDData(Vector3f direction, Vector3f yaw, Vector3f acceleration);

    void setEnableHUD(boolean arg);

    void loadDeko(String filename) throws FileNotFoundException;

    void setPattern(String pattern_filename) throws FileNotFoundException;

    void setBoundingPattern(String bounding_pattern_filename) throws FileNotFoundException;

    void setJoint(String filename) throws FileNotFoundException;

    boolean getShowStateDekoration();

    void setShowStateDekoration(boolean state);

    boolean getShowStatePoles();

    void setShowStatePoles(boolean state);
}
