package de.rollercoaster.graphics;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

/**
 *
 * @author Simon
 */
public class CameraControl {

    private CameraMode mode = CameraMode.INTERIOR;//at  the beginning the interiror Cam is activated
    private Camera cam;
    private Spatial car;
    private Matrix3f matrix = new Matrix3f();
    private final SimpleApplication application;
    private Vector3f location;

    /**
     * Kontruktor
     *
     */
    public CameraControl(SimpleApplication application, Camera cam, Spatial car) {
        this.application = application;
        this.cam = cam;
        this.car = car;
				application.getRootNode().attachChild(car);
        car.setCullHint(CullHint.Always);
    }

    /**
     * returns position of car for overview of way
     * @return 2D vector 
     */
    public Vector2f getOverviewPosition() {
        Vector2f position2D = new Vector2f();
        //TODO: Vector3f in Vector 2f umwandeln
        return null;//carPosition;
    }

    /**
     * initials rollercoaster car
     * @param start: startposition of the rollercoaster
     */
    /*public void initCar() {
        Box box = new Box(new Vector3f(1,-1,1), 1, 1, 1);
        Geometry wagon = new Geometry("myBox", box);
        wagon.setMaterial(new Material(application.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"));
        application.getRootNode().attachChild(wagon);
    }*/

    public void updateCamera() {
        switch (mode) {
            case OVERVIEW:
                application.getFlyByCamera().setEnabled(true);
                car.setCullHint(CullHint.Dynamic);
                break;

            case INTERIOR:
                application.getFlyByCamera().setEnabled(false);
                car.setCullHint(CullHint.Always);
                break;
        }
    }

    /**
     * changes the camera Mode: OVERVIEW or INTERIOR VIEW
     * @param mode: camera mode
     */
    public void setCameraMode(CameraMode mode) {
        this.mode = mode;
        updateCamera();

    }

    public CameraMode getCameraMode() {
        return mode;
    }

    void setCarPosition(Vector3f location, Vector3f left, Vector3f up, Vector3f direction) {
			this.location = location;
				if (mode== CameraMode.OVERVIEW){
					matrix.fromAxes(left.mult(-1),up,direction);
					car.setLocalTranslation(location);
					car.setLocalRotation(matrix); 
				}
				else if(mode==CameraMode.INTERIOR){
			 // Fallunterscheidung;
					cam.setFrame(location, left.mult(-1), up, direction);
				}
				
            

            
				
				
    }
}
