package de.rollercoaster.graphics.pattern;

import com.jme3.math.Vector3f;

/**Kapselt einen Eckpunkt eines 3D-Konstrukts. Es werden positon und normale gespeichert. 
<Dev>:Dies ist als offenes Struct gedacht für die erleichterte Behandlung von Daten. ggf muss das angepasst werden mit get und set </dev>
*/
public class Vertex3d {
  public Vector3f position;
  public Vector3f normal;

  public Vertex3d () {
    position = new Vector3f(0f,0f,0f);
    normal = new Vector3f(0f,0f,0f);
  }

  public Vertex3d (Vector3f pos) {
    position = pos.clone();
    normal = new Vector3f(0f,0f,0f);
  }

  public Vertex3d (Vector3f pos, Vector3f normal) {
    position = pos.clone();
    this.normal = normal.clone();
  }

} 
