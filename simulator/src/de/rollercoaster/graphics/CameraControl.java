import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Simon
 */
public class CameraControl extends SimpleApplication{
    private Vector3f carPosition;
    private char mode = 'i';//at  the beginning the interiror Cam is activated
    private Camera cam;
    private Node car;
    private Matrix3f matrix = new Matrix3f();
		
    /**
    * Kontruktor
    *
    */
		 
    public CameraControl(Camera cam, Node car){
        this.cam = cam;
        this.car = car;
    }
		
		
    /**
     * returns position of car for overview of way
     * @return 2D vector 
     */
    public Vector2f getOverviewPosition(){
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
    }
    
    /**
     * initials rollercoaster car
     * @param start: startposition of the rollercoaster
     */
    public void initCar(Vector3f start){
        Box box = new Box(start, 1,1,1);
        Geometry wagon = new Geometry("myBox", box) ;
        wagon.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));
        rootNode.attachChild(wagon); 
    }
    

    @Override
    public void simpleInitApp() {
        switch(mode){
            case 'o':
                flyCam.setEnabled(true);
                car.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));  									
                break;  
                
            case 'i': 
                flyCam.setEnabled(false);
                car.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Invisible.j3md"));
                break;
                
            default:
        }
    }
    
}
