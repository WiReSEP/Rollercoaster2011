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
 
/** Sample 3 - how to load an OBJ model, and OgreXML model,
 * a material/texture, or text. */
public class Kamera extends SimpleApplication {
 
 
    private Geometry geo1, geo2, geo3;
    private Spatial sp1, sp2, sp3;
    private BitmapText helloText;
    private Vector3f target;
    private Spatial player;
    private float speed;
    private float [][] targets;
    int counter =0, maxCounter = 50;
    
    @Override
    public void simpleInitApp() {
 
        Spatial teapot = assetManager.loadModel("Models/Teapot/Teapot.obj");
        
        Material mat_default = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_default.setColor("Color", ColorRGBA.Blue);
         Material mat_default2 = new Material(
            assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        
        teapot.setMaterial(mat_default2);
        rootNode.attachChild(teapot);
        
        sp1 = teapot;
        
        Box b = new Box(new Vector3f(1, 1, 1),1, 1, 1) ;
       Geometry myBox = new Geometry("myBox", b) ;
        myBox.setMaterial(mat_default2);
        rootNode.attachChild(myBox);
        
        geo1 = myBox;
 
        // Create a wall with a simple texture from test_data
        Box box = new Box(Vector3f.ZERO, 2.5f,2.5f,1.0f);
        Spatial wall = new Geometry("Box", box );
        Material mat_brick = new Material(
            assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_brick.setTexture("ColorMap",
            assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        wall.setMaterial(mat_brick);
        wall.setLocalTranslation(2.0f,-2.5f,0.0f);
        rootNode.attachChild(wall);
        
        sp2 = wall;
 
      
 
        // Load a model from test_data (OgreXML + material + texture)
        Spatial ninja = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
        ninja.scale(0.05f, 0.05f, 0.05f);
        ninja.rotate(0.0f, -3.0f, 0.0f);
        ninja.setLocalTranslation(0.0f, -5.0f, -2.0f);
        rootNode.attachChild(ninja);
        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);
        
        this.loadHuD();   //was soll ich damit?
        
        sp3 = ninja;
        
        initKeys();
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
        
        player = ninja;
        target = new Vector3f(40, 40, 40);
        speed = 0.5f;
        
        
        ChaseCamera chasi= new ChaseCamera(cam, player, inputManager);
        
 
    }
    
    private void loadHuD(){
          // Display a line of text with a default font
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setLocalTranslation(300, helloText.getLineHeight(), 2);
        guiNode.attachChild(helloText);
    }
    
    
    private void initKeys(){
        inputManager.addMapping("ClockWise", new KeyTrigger(keyInput.KEY_Y));
        inputManager.addMapping("CClockWise", new KeyTrigger(keyInput.KEY_X));
        inputManager.addMapping("SlowMove", new KeyTrigger(keyInput.KEY_V));
        
        inputManager.addListener(anaList1, new String[] {"ClockWise"});
        inputManager.addListener(anaList2, new String[] {"CClockWise"});
        inputManager.addListener(anaList3, new String[] {"SlowMove}"});
    }
    
    private AnalogListener anaList1 = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {
        geo1.rotate(tpf, tpf, tpf);
        
        sp1.rotate(0, 0, tpf);
        
        sp2.rotate(tpf, 0, tpf);
        
        sp3.rotate(0, 20*tpf, 0);
        }
    };
     private AnalogListener anaList2 = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {
        geo1.rotate(-tpf*0.5f, -tpf*0.5f, -tpf*0.5f);
        
        sp1.rotate(0, 0, -tpf*.5f);
        
        sp2.rotate(-tpf*.5f, 0, -tpf*.5f);
        
        sp3.rotate(0, -20*tpf*.5f, 0);
        }
    };
     
     private AnalogListener anaList3 = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {
            System.out.printf("MoveSpeed\n");
           flyCam.setMoveSpeed(10);
        }
    };
     
     private void makeGround(){
         Box myGround = new Box(new Vector3f(0, -5, 0), 100, 0, 100);
         Geometry myGroundGeo = new Geometry("myGround", myGround);
         Material myGroundMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
         myGroundMat.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
         myGroundGeo.setMaterial(myGroundMat);
         
         this.rootNode.attachChild(myGroundGeo);
     }
     
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
     
     private boolean hasReachedPoint(Spatial player, Vector3f target, float speed){
         return (Math.abs(target.x-player.getLocalTranslation().x)<speed)&&
                    (Math.abs(target.y-player.getLocalTranslation().y)<speed)&&
                    (Math.abs(target.z-player.getLocalTranslation().z)<speed);
     }
    
    
    @Override
    public void simpleUpdate(float tpf){
        
    
        
    if(!hasReachedPoint(player, target, speed)&&counter<maxCounter){
        moveToPoint(player, target, speed);
    }
    else if (counter<maxCounter){
        target = new Vector3f(targets[counter][0], targets[counter][1], targets[counter][2]);
        counter++;
    }
    
        helloText.setText("FTP: " + tpf);
    }
    
}