import com.jme3.app.SimpleApplication;
import com.jme3.app.Application;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.texture.Texture;
import com.jme3.math.ColorRGBA;
import java.awt.Dimension;
import java.awt.event.*;
import com.jme3.system.JmeContext.Type;
import com.jme3.scene.shape.Cylinder;
import com.jme3.light.DirectionalLight;



//den windowlsitener gibt es vorerst damit 
public class Graphics3D extends SimpleApplication {
  private JmeCanvasContext ctx = null;

  public Graphics3D () {
    super();
    //simpleInitApp();
  }

  private boolean close = false;

  @Override
  public void simpleInitApp() {

//         Vector3f [] positions = new Vector3f[26];
// 
//         positions[0] = new Vector3f(0f,0f,0f);
//         positions[0] = new Vector3f( -4.738632f, 1.624878f, 5.879887f);
//         positions[1] = new Vector3f( -13.074545f, 6.143410f ,-4.358834f);
//         positions[2] = new Vector3f( -10.542608f, 8.870111f, -8.329572f);
//         positions[3] = new Vector3f( -2.635177f, 9.454405f, -14.938023f);
//         positions[4] = new Vector3f( 8.115814f, 10.077649f, -22.355103f);
//         positions[5] = new Vector3f( 14.292020f, 9.688121f, -21.441870f);
//         positions[6] = new Vector3f( 16.604593f, 7.896289f ,-17.394842f);
//         positions[7] = new Vector3f( 6.791417f, 6.145103f, -10.642124f);
//         positions[8] = new Vector3f( 0.271175f, 4.871347f ,-8.310635f);
//         positions[9] = new Vector3f( -2.574536f ,4.334851f, -5.912589f);
//         positions[10] = new Vector3f( -5.339746f, 3.474519f, -5.026571f);
//         positions[11] = new Vector3f( -4.271197f, 3.059991f, -3.414528f);
//         positions[12] = new Vector3f( 13.763979f, 4.104042f, -3.69288f);
//         positions[13] = new Vector3f( 20.385965f, 5.948646f, -2.855992f);
//         positions[14] = new Vector3f( 30.812634f, 10.126015f, 0.663381f);
//         positions[15] = new Vector3f( 32.471420f ,18.981981f, 0.527189f);
//         positions[16] = new Vector3f( 26.360268f, 26.268356f, -1.508558f);
//         positions[17] = new Vector3f( 15.548227f, 26.738445f, -3.276520f);
//         positions[18] = new Vector3f( 7.948460f, 20.000507f, -4.358835f);
//         positions[19] = new Vector3f( 11.317429f ,11.715757f, -4.358834f);
//         positions[20] = new Vector3f( 27.613836f, 8.004792f, -4.358834f);
//         positions[21] = new Vector3f( 32.565208f, 7.744209f, -3.388389f);
//         positions[22] = new Vector3f( 34.586823f, 7.314015f, 2.735221f);
//         positions[23] = new Vector3f( 25.811832f, 6.008155f, 4.881735f);
//         positions[24] = new Vector3f( 17.301071f, 4.800794f, 4.910135f);
//         positions[25] = new Vector3f( 7.400023f, 3.234006f, 4.661860f);
//     

Vector3f [] positions = new Vector3f[4];
positions[0] = new Vector3f(-5f,0f,-5f);
positions[1] = new Vector3f(5f,0f,-5f);
positions[2] = new Vector3f(5f,0f,5f);
positions[3] = new Vector3f(-5f,0f,5f);

    start(Type.Canvas);
    flyCam.setDragToRotate(true);
    // display a cube

   /*Cylinder cyl = new Cylinder(5,5,2.0f,5.0f);
   Geometry bahn = new Geometry("Bahn", cyl);
   Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
   mat1.setColor("Color", ColorRGBA.Yellow);
   bahn.setMaterial(mat1);
   rootNode.attachChild(bahn);*/

 
Achterbahn bahn = new Achterbahn(positions);
  Geometry geom_bahn = new Geometry("Bahn", bahn);
  //Material mat1 = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");


  Material wireMaterial = new Material(assetManager, "/Common/MatDefs/Misc/WireColor.j3md");
  wireMaterial.setColor("Color", ColorRGBA.Blue);
  
  geom_bahn.setMaterial(wireMaterial);
  rootNode.attachChild(geom_bahn);


  Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
  Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
  Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
  mat1.setColor("Color", ColorRGBA.Red);
  mat2.setColor("Color", ColorRGBA.Green);
  mat3.setColor("Color", ColorRGBA.Yellow);

  Box box1 = new Box( new Vector3f(-10,0,0), 1,1,1);
  Box box2 = new Box( new Vector3f(10,0,0), 1,1,1);
  Box box3 = new Box( new Vector3f(0,0,10), 1,1,1);

  Geometry marker1 = new Geometry("Box", box1);
  Geometry marker2 = new Geometry("Box", box2);
  Geometry marker3 = new Geometry("Box", box3);
  marker1.setMaterial(mat1);
  marker2.setMaterial(mat2);
  marker3.setMaterial(mat3);
  rootNode.attachChild(marker1);
  rootNode.attachChild(marker2);
  rootNode.attachChild(marker3);

//   blue.rotate(3.14f/2,0,0);

 /*Box box1 = new Box( new Vector3f(1,-1,1), 1,1,1);
  Geometry blue = new Geometry("Box", box1);
  Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
  mat1.setColor("Color", ColorRGBA.Blue);
  blue.setMaterial(mat1);
  rootNode.attachChild(blue);
  */

    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(1,0,-2).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);

    
  }
  @Override
  public void simpleUpdate (float tpf) {
    //Dies wird aufgerufen bevor ein Frame gerendert wird
  }


  public JmeCanvasContext getCanvasObject() {
//           AppSettings settings = new AppSettings(true);
//           settings.setWidth(640);
//           settings.setHeight(480);
//           settings.setFrameRate(30);
// 
//           this.setSettings(settings);

      this.createCanvas(); // create canvas!
      ctx = (JmeCanvasContext) this.getContext();
      Dimension dim = new Dimension(640, 480);
      ctx.getCanvas().setPreferredSize(dim);
     // this.startCanvas(); // create canvas!
    
      return ctx;
  }


  //Bei closen unbedingt sowas bauen sonst wartet man ewig auf den lwjgl
  public void freeCanvas() {
    close = true;
  }


  //Setzt die Kamera auf die entsprechende Position
  public void setCamera (float x,float y,float z, float dx,float dy,float dz) {


  }

  //Setzt HUD-Daten zur Anzeige
  public void setHUDData(/*Insert data here*/) {


  }

  //Lädt (ggf nur eine Config-) Datei die die Deko enth#ält (oder auf sie verweist)
  public void loadDeko (String filename) {

  }

  //Bekommt eine Bahn und generiert das 3d Bahn object entsprechend
  public void setTrack () {

  }

     
} 
