package de.rollercoaster.graphics.pattern;

import com.jme3.math.Vector3f;

public class SimplePattern implements Pattern {
  
  private int [][] pattern_index_trips = {{0,1,2,3,4,5,6,7},
                                          {8,9,10,11,12,13,14,15},
                                          {16,17,18,19,20,21,22,23}}; 
  private Vertex3d [] vertexdata;
  
  public SimplePattern() {
 //Patterndaten vereinbaren
        Vector3f [] pattern_vertexdata = new Vector3f[24];
        Vector3f [] pattern_vertexdata_normal = new Vector3f[24];
        
        //eckpunkte ...sinnigerweise sollten x und y belegt sein; da ich die Daten aber von Hand in 
        //dieses Format gebracht habe hatte ich auf nachträgliche Änderung keine Lust
        pattern_vertexdata[0] = new Vector3f(-4.300409f, -0.000000f, -2.723467f);
        pattern_vertexdata[1] = new Vector3f(-4.007516f, -0.000000f, -2.016360f);
        pattern_vertexdata[2] = new Vector3f(-4.300409f, -0.000000f, -1.309253f);
        pattern_vertexdata[3] = new Vector3f(-5.007516f, -0.000000f, -1.016360f);
        pattern_vertexdata[4] = new Vector3f(-5.714623f, -0.000000f, -1.309253f);
        pattern_vertexdata[5] = new Vector3f(-6.007516f, -0.000000f, -2.016361f);
        pattern_vertexdata[6] = new Vector3f(-5.714622f, -0.000000f, -2.723467f);
        pattern_vertexdata[7] = new Vector3f(-5.007514f, -0.000000f, -3.016360f);

        pattern_vertexdata[8] = new Vector3f(5.727976f, -0.000000f, -2.710113f);        
        pattern_vertexdata[9] = new Vector3f(6.020869f, -0.000000f, -2.003006f);
        pattern_vertexdata[10] = new Vector3f(5.727976f, -0.000000f, -1.295900f);
        pattern_vertexdata[11] = new Vector3f(5.020869f, -0.000000f, -1.003006f);
        pattern_vertexdata[12] = new Vector3f(4.313762f, -0.000000f, -1.295900f);
        pattern_vertexdata[13] = new Vector3f(4.020869f, -0.000000f, -2.003007f);
        pattern_vertexdata[14] = new Vector3f(4.313763f, -0.000000f, -2.710114f);
        pattern_vertexdata[15] = new Vector3f(5.020871f, -0.000000f, -3.003006f);

        pattern_vertexdata[16] = new Vector3f( 0.707107f, -0.000000f, -0.707107f);
        pattern_vertexdata[17] = new Vector3f( 1.000000f, -0.000000f, -0.000000f);
        pattern_vertexdata[18] = new Vector3f( 0.707107f,  0.000000f,  0.707107f);
        pattern_vertexdata[19] = new Vector3f(-0.000000f,  0.000000f,  1.000000f);
        pattern_vertexdata[20] = new Vector3f(-0.707107f,  0.000000f,  0.707106f);
        pattern_vertexdata[21] = new Vector3f(-1.000000f, -0.000000f, -0.000001f);
        pattern_vertexdata[22] = new Vector3f(-0.707106f, -0.000000f, -0.707108f);
        pattern_vertexdata[23] = new Vector3f( 0.000002f, -0.000000f, -1.000000f);


        //in die xy ebene packen (nur debug)  (hier wird eine z coord vernichtet die prinzipielle zugelassen wird)
        for (int vertexcounter = 0; vertexcounter < pattern_vertexdata.length; vertexcounter++) {  
          pattern_vertexdata[vertexcounter].y=-pattern_vertexdata[vertexcounter].z;
          pattern_vertexdata[vertexcounter].z = 0f;
        }


        //scale
         for (int vertexcounter = 0; vertexcounter < pattern_vertexdata.length; vertexcounter++) {  
           pattern_vertexdata[vertexcounter].multLocal(0.8f);          
         }
        

        //Normalen erzeugen (nur debug) (es wird angenommen, dass nur x und y gesetzt sind und nach dem debugcode drüber ist das auch der fall)
        //Für die Normalenerzeugung wird ein Trick angewandt: Suche den Mittelpunkt einer Zusammenhangskomponente (trip) und verlängere den vektor von dort zu einem Vertex auf die Länge 1 
        // Dieses Verfahren funktioniert nur, da alle Trips symmetrisch bzw Kreisförmig sind.
        for (int tripcounter = 0; tripcounter < pattern_index_trips.length; tripcounter++) {
          Vector3f center = new Vector3f(0f,0f,0f);
          //Orte aufsummieren um Mitte zu erhalten
          for (int indexcounter = 0; indexcounter < pattern_index_trips[tripcounter].length; indexcounter++) {
            center.addLocal(pattern_vertexdata[pattern_index_trips[tripcounter][indexcounter]]);
          }
          //mitteln
          center.multLocal((float)1.0/pattern_index_trips[tripcounter].length);  
          
          //Vektoren erzeugen, normalisieren und zuweisen
          for (int indexcounter = 0; indexcounter < pattern_index_trips[tripcounter].length; indexcounter++) {
            pattern_vertexdata_normal[pattern_index_trips[tripcounter][indexcounter]]= pattern_vertexdata[pattern_index_trips[tripcounter][indexcounter]].subtract(center).normalize();//.mult(-1f);            
          }          
        }

        vertexdata = new Vertex3d[24];
        for (int vertexcounter = 0; vertexcounter < pattern_vertexdata.length; vertexcounter++) {  
           vertexdata[vertexcounter] = new Vertex3d(pattern_vertexdata[vertexcounter],pattern_vertexdata_normal[vertexcounter]);
        }
    }

 

  public int getTripCount() {
    return pattern_index_trips.length;
  }
  
  public int getVertexCount() {
    return vertexdata.length;
  }

  public int getTripLength(int tripnr) {
    return pattern_index_trips[tripnr].length;
  }

  public int getVertexIndex(int tripnr, int index) {
    return pattern_index_trips[tripnr][index];
  }
  
  public Vertex3d getVertex(int vertexnr) {
    return vertexdata[vertexnr];
  }

   
} 
