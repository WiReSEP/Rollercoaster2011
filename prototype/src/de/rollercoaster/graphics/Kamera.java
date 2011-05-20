package mygame;


import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.input.*;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Node;
 
/** Sample 3 - how to load an OBJ model, and OgreXML model,
 * a material/texture, or text. */
public class Kamera extends SimpleApplication {
 
 

    private BitmapText helloText;
    
    private Geometry boxOfView;
    private Node nodeOfView;
    private Vector3f target;
    private Spatial player;
    //private float speed;
    private float [][] targets;
    private int counter =0, maxCounter = 10;
    private ChaseCamera chasi;
    
    /**
     * Intialmethode,wer hätts gedacht...
     */
    @Override
    public void simpleInitApp() {

 
        // Eine Mauer zur Orientierung
        Box box = new Box(Vector3f.ZERO, 2.5f,2.5f,1.0f);
        Spatial wall = new Geometry("Box", box );
        Material mat_brick = new Material(
            assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_brick.setTexture("ColorMap",
            assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        wall.setMaterial(mat_brick);
        wall.setLocalTranslation(2.0f,-2.5f,0.0f);
        rootNode.attachChild(wall);
         
        
        this.loadHuD();   //was soll ich damit?

        makeGround();
        flyCam.setMoveSpeed(30);
        
        
        
        targets = new float[maxCounter][3];
        for (int i = 0; i< maxCounter;i++){
            targets[i] = new float[3];
            for (int j = 0;j<3;j++){
                targets[i][j] =(float) Math.random()*100;
            }
        }
        
        targets[maxCounter-1] = new float[]{0,0,0};
        

        initBoxOfView(null);
        chasi= new ChaseCamera(cam, boxOfView, inputManager);
        chasi.setDefaultDistance(0.0f);
        chasi.setDragToRotate(true);
        player = boxOfView;
        target = new Vector3f(40, 40, 40);
        speed = 0.2f;
 
    }
    
    /**
     * Erzegut das Objekt, dem die Kamera folgt.
     * @param positionOfBox Offset der Kameraposition
     */
    private void initBoxOfView(Vector3f positionOfBox){
        nodeOfView = new Node("nodeOfView");
        
        
        Box theBoxOfView = new Box(positionOfBox, 0,0,0);
        boxOfView = new Geometry("myBox", theBoxOfView) ;
        boxOfView.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));
        nodeOfView.attachChild(boxOfView);
        
        rootNode.attachChild(nodeOfView);
    }
    

    
    /**
     * eine unütze methode...
     */
    private void loadHuD(){
          // Display a line of text with a default font
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setLocalTranslation(300, helloText.getLineHeight(), 2);
        guiNode.attachChild(helloText);
    }
    
    
   
     /**
     * Erzeugt eine graue Grundfläche zur Orientierung bei z=0
     */
     private void makeGround(){
         Box myGround = new Box(new Vector3f(0, -5, 0), 100, 0, 100);
         Geometry myGroundGeo = new Geometry("myGround", myGround);
         Material myGroundMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
         myGroundMat.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
         myGroundGeo.setMaterial(myGroundMat);
         
         this.rootNode.attachChild(myGroundGeo);
     }
     
     /**
      * Errechnet den Zielvektor aus aktueller Position und Ziel, bewegt
      * das Objekt in Schrittgrößen auf diesem Vektor Richtung Ziel
      * @param player das zu bewegende Objekt
      * @param target das angestrebte Ziel
      * @param speed Schrittgröße
      */
     private void moveToPoint(Spatial player, Vector3f target, float speed){
         
             Vector3f nextPoint = new Vector3f();
             Vector3f distance = new Vector3f();
             distance.x = target.x - player.getLocalTranslation().x;
             distance.y = target.y - player.getLocalTranslation().y;
             distance.z = target.z - player.getLocalTranslation().z;
             float scalarDistance = distance.length();
             
             
             float new_x = (distance.x)/scalarDistance*speed;
             float new_y = (distance.y)/scalarDistance*speed;
             float new_z = (distance.z)/scalarDistance*speed;
             
             if(Math.abs(distance.x) < speed){
                 new_x = 0;
             }
             if (Math.abs(distance.y) < speed){
                 new_y = 0;
             }
             if (Math.abs(distance.z) < speed){
                 new_z = 0;
             }
                 
             nextPoint.x = player.getLocalTranslation().x + new_x;
             nextPoint.y = player.getLocalTranslation().y + new_y;
             nextPoint.z = player.getLocalTranslation().z + new_z;
             
             player.setLocalTranslation(nextPoint);
         
         
     }
     /**
      * Prüft ob das Ziel erreicht wurde. DAbei wird von einer Ungenauigkeit in der 
      * Größe eines Schrittes ausgegangen
      * @param player das Objekt
      * @param target der Zielpunkt
      * @param speed Schrittgröße
      * @return Ziel erreicht, oder Grenzen erreicht?
      */
     private boolean hasReachedPoint(Spatial player, Vector3f target, float speed){
         return (Math.abs(target.x-player.getLocalTranslation().x)<speed)&&
                    (Math.abs(target.y-player.getLocalTranslation().y)<speed)&&
                    (Math.abs(target.z-player.getLocalTranslation().z)<speed);
     }
    
    
    @Override
    public void simpleUpdate(float tpf){
    //chasi= new ChaseCamera(cam, boxOfView, inputManager);
    /*
     * Wenn der Zielpunkt noch nciht erreicht ist, bewegt sich die Kamera
     * weiter darauf zu
     */
    if(!hasReachedPoint(player, target, speed)&&counter<=maxCounter){
        moveToPoint(player, target, speed);
        
        //chasi.setDefaultDistance(0.01f);
    }
    /**
     * Zielpunkt erreicht, nächster wird aus dem Array geladen
     */
    else if (counter<maxCounter){
        target = new Vector3f(targets[counter][0], targets[counter][1], targets[counter][2]);
        counter++;
    }
    /**
     * Alle Ziele abgeflogen -> Fahrt zu Ende
     */
    else{
        
    }
        //was für ein tpf haben wir?
        helloText.setText("FTP: " + tpf);
    }
    
}