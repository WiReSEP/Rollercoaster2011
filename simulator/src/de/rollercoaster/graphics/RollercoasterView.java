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

/**
 *
 * @author mangelsdorf
 */
public class RollercoasterView implements View {

    private final Graphics3D graphics;
    private JmeCanvasContext context;
    private final List<ViewObserver> observers = Collections.synchronizedList(new LinkedList<ViewObserver>());
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

    //Temporäre Methode zum starten und stopen des Kameraflugs
    //Diese Methode ist nur zur Präsentation eingefügt!!!
    @Deprecated
    public void pause(boolean state) {
        graphics.pause = state;
    }

    @Override
    public boolean addObserver(ViewObserver observer) {
        return this.observers.add(observer);
    }

    @Override
    public boolean removeObserver(ViewObserver observer) {
        return this.observers.remove(observer);
    }

    public void notifyObservers(double timePerFrame) {
        for (ViewObserver observer : observers) {
            observer.update(timePerFrame);
        }
    }

    public void setCameraMode(CameraMode mode) {
        graphics.setCameraMode(mode);
    }

    public CameraMode getCameraMode() {
        return graphics.getCameraMode();
    }
}
