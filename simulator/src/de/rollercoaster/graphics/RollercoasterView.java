/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rollercoaster.graphics;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.system.JmeCanvasContext;
import de.rollercoaster.mathematics.Curve;
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
    
    public void setHUDData(Vector3f rot, Vector3f acc) {
      graphics.setHUDData(rot, acc);
    }
    
    public void loadDeko(String filename) throws IllegalArgumentException {
      graphics.loadDeko(filename);
    }
    
    public void setPattern(String pattern_filename, String bounding_pattern_filename) throws FileNotFoundException {
       graphics.setPattern(pattern_filename, bounding_pattern_filename);
    }
    
    public void setJoint(String filename) throws IllegalArgumentException {
       graphics.setJoint(filename);
    }
    
    public boolean getShowStateDekoration() {
      return graphics.getShowStateDekoration();
    }
    
    public void setShowStateDekoration(boolean state) {
      graphics.setShowStateDekoration(state);
    }
    
    public boolean getShowStatePoles() {
      return graphics.getShowStatePoles();
    }
    
    public void setShowStatePoles(boolean state) {
      graphics.setShowStatePoles(state);
    }
}
