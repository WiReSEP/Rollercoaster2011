/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rollercoaster.graphics;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.system.JmeCanvasContext;
import de.rollercoaster.mathematics.Curve;
import de.rollercoaster.mathematics.Vector3d;
import java.awt.Canvas;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.io.FileNotFoundException;

/**
 *
 * @author mangelsdorf
 */
public class RollercoasterView implements View {
  private final Graphics3D graphics;
  private JmeCanvasContext context;
  private final List<ViewObserver> observers = new LinkedList<ViewObserver>();
  private Curve curve;

  public RollercoasterView(Curve curve) {
    this.curve = curve;
    this.graphics = new Graphics3D(this);
  }

  @Override
  public void setCamera(Vector3f location, Vector3f left, Vector3f up, Vector3f direction) {
    graphics.setCamera(location, left, up, direction);
  }

  @Override
  public void setCurve(Curve curve) {
    this.curve = curve;
    graphics.setCurve(curve);
  }

  public Curve getCurve() {
    return curve;
  }

  @Override
  public void init() {
    this.context = graphics.getCanvasObject();
  }

  @Override
  public void dispose() {
    synchronized (observers) {
      this.observers.clear();
    }

    graphics.stop();
    context = null;
  }

  @Override
  public Canvas getCanvas() {
    return context.getCanvas();
  }

  @Override
  public boolean addObserver(ViewObserver observer) {
    synchronized (observers) {
      return this.observers.add(observer);
    }
  }

  @Override
  public boolean removeObserver(ViewObserver observer) {
    synchronized (observers) {
      return this.observers.remove(observer);
    }
  }

  public void notifyObservers(double timePerFrame) {
    synchronized (observers) {
      for (ViewObserver observer : observers) {
        observer.update(timePerFrame);
      }
    }
  }

  public void setCameraMode(CameraMode mode) {
    graphics.setCameraMode(mode);
  }

  public CameraMode getCameraMode() {
    return graphics.getCameraMode();
  }

    @Override
  public void setHUDData(Vector3f direction, Vector3f yaw, Vector3f acceleration) {
    double r = Math.sqrt(direction.z * direction.z + direction.x * direction.x);
    double x = 180.0 / Math.PI * Math.atan2(direction.y, r); // Steigung y gegen x-z
    double y = Math.atan2(direction.z, direction.x); // Komposs x gegen z
    
    Vector3f pitch = yaw.cross(direction);
    double z = Math.asin(pitch.y);
    
    Vector3f rotation = new Vector3d(x, y, z).toF();
     
    Vector3f acc = new Vector3f(acceleration.dot(pitch), acceleration.dot(direction), acceleration.dot(yaw)).divide(9.81f);
  
    graphics.setHUDData(rotation, acc);
  }

    @Override
  public void setEnableHUD(boolean arg) {
    graphics.setEnableHUD(arg);
  }

    @Override
  public void loadDeko(String filename) throws IllegalArgumentException {
    graphics.loadDeko(filename);
  }

    @Override
  public void setPattern(String pattern_filename) throws FileNotFoundException {
    graphics.setPattern(pattern_filename);
  }

    @Override
  public void setBoundingPattern(String bounding_pattern_filename) throws FileNotFoundException {
    graphics.setBoundingPattern(bounding_pattern_filename);
  }

    @Override
  public void setJoint(String filename) throws IllegalArgumentException {
    graphics.setJoint(filename);
  }

    @Override
  public boolean getShowStateDekoration() {
    return graphics.getShowStateDekoration();
  }

    @Override
  public void setShowStateDekoration(boolean state) {
    graphics.setShowStateDekoration(state);
  }

    @Override
  public boolean getShowStatePoles() {
    return graphics.getShowStatePoles();
  }

    @Override
  public void setShowStatePoles(boolean state) {
    graphics.setShowStatePoles(state);
  }
}
