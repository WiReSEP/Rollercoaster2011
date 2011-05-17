/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rollercoaster.graphics;

import com.jme3.math.Vector3f;
import com.jme3.system.JmeCanvasContext;
import de.rollercoaster.mathematics.Curve;
import java.awt.Canvas;

/**
 *
 * @author mangelsdorf
 */
public class RollercoasterView implements View {
    private final Graphics3D graphics;
    private JmeCanvasContext context;
    
    public RollercoasterView() {
        this.graphics = new Graphics3D();
    }
    
    @Override
    public void setCamera(Vector3f position, Vector3f direction) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCurve(Curve curve) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    
}
