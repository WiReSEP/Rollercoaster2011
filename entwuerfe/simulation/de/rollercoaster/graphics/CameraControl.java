import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Simon
 */
public class CameraControl extends SimpleApplication{
    private Vector3f carPosition;
    private char mode = 'i';			//at  the beginning the interiror Cam is activated
    /**
     * returns position of car for overview of way
     * @return 2D vector 
     */
    public Vector2f getPosition(){
        carPosition = cam.getLocation();
        Vector2f position2D = new Vector2f();
        //TODO: Vector3f in Vector 2f umwandeln
        return null;//carPosition;
    }
    
    /**
     * changes the camera Mode: Overview (o) or interior view (i)
     * @param mode: cmaera mode
     */
    public void changeCameraMode(char mode){
			this.mode = mode;
				switch(mode){
            case 'o': flyCam.setEnabled(true);
													//TODO wagen sichtbar machen
                      break;  
                
            case 'i': flyCam.setEnabled(false);
												//TODO Wagen unsichtbar machen
                      break;
                
            default: //TODO: Exception werfen
        }      
    }
    
    /**
     * initials rollercoaster car
     * @param start: startposition of the rollercoaster
     */
    public void initCar(Vector3f start){
        Box box = new Box(start, 1,1,1);
        Geometry car = new Geometry("myBox", box) ;
        car.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));
        rootNode.attachChild(car); 
    }

    @Override
    public void simpleInitApp() {
        
    }
    
		@Override
		public void simpleUpdate(){
		
		}
    
}
