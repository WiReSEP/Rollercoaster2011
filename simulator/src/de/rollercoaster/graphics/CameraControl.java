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

public class CameraControl {

    private CameraMode mode = CameraMode.INTERIOR;	//at  the beginning the interiror Cam is activated
    private Camera cam;
    private Spatial car;
    private Matrix3f matrix = new Matrix3f();
    private final SimpleApplication application;
    private Vector3f location;
    private Vector3f direction;
		private boolean isOverview = false;
    /**
     * Kontruktor
		* @param application: application of Graphics3D,  cam: Camera,  car: Car 
     *
     */
    public CameraControl(SimpleApplication application, Camera cam, Spatial car) {
        this.application = application;
        this.cam = cam;
        this.car = car;
				application.getRootNode().attachChild(car);
        car.setCullHint(CullHint.Always);	//at the beginning the car is hidden
    }

		/**
			*updates the Camera mode: Interior view or overview
			*
		*/
    public void updateCamera() {
        switch (mode) {
            case OVERVIEW:
							if(!isOverview){	//if Overview is chosen the cameraposition won't change if start is pushed again
                this.cam.getRotation().lookAt(direction, new Vector3f(0,1,0) );	//the camera is straight, not rotated
                this.cam.update();
                application.getFlyByCamera().setEnabled(true);	//control the camera by own
                car.setCullHint(CullHint.Dynamic);	//car is visible
								isOverview= true;
							}
                break;

            case INTERIOR:
                application.getFlyByCamera().setEnabled(false);
                car.setCullHint(CullHint.Always);
								isOverview = false;
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

			/**
			 * returns the camera mode
			 * @return mode: The caera mode
			*/
    public CameraMode getCameraMode() {
        return mode;
    }

		/**
		 * in interior mode the method control the camera and in overview mode the method control the car
		 * @param location: position of camera or car,  left: left vector,  up: up vector, direction: the direction of car or camera
		 *
		*/
    void setCarPosition(Vector3f location, Vector3f left, Vector3f up, Vector3f direction) {
			this.location = location;
      this.direction = direction;
				if (mode== CameraMode.OVERVIEW){
					matrix.fromAxes(left.mult(-1),up,direction);
					car.setLocalTranslation(location);
					car.setLocalRotation(matrix); 
				}
				else if(mode==CameraMode.INTERIOR){
					cam.setFrame(location, left.mult(-1), up, direction);
				}

    }
}
