package de.rollercoaster.graphics;

import com.jme3.math.Vector3f;
import de.rollercoaster.mathematics.Curve;
import java.awt.Canvas;

public interface View {
  void setCamera(Vector3f location, Vector3f left, Vector3f up, Vector3f direction);
  void setCurve(Curve curve);
  void init();
  void dispose();
  Canvas getCanvas();
  boolean addObserver(ViewObserver observer);
  boolean removeObserver(ViewObserver observer);
}
