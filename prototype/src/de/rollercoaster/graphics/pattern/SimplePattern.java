package de.rollercoaster.graphics.pattern;

import com.jme3.math.Vector3f;

public class SimplePattern implements Pattern {
  
  private int [][] pattern_index_trips = {{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31},
                                          {32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63},
                                          {64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95}}; 
  private Vertex3d [] vertexdata;
  
  public SimplePattern() {
 //Patterndaten vereinbaren
        Vector3f [] pattern_vertexdata = new Vector3f[96];
        Vector3f [] pattern_vertexdata_normal = new Vector3f[96];
        
        //eckpunkte ...sinnigerweise sollten x und y belegt sein; da ich die Daten aber von Hand in 
        //dieses Format gebracht habe hatte ich auf nachträgliche Änderung keine Lust
        pattern_vertexdata[0] = new Vector3f(-4.300409f, -0.000000f, -2.723467f);
        pattern_vertexdata[1] = new Vector3f(-4.176046f, -0.000000f, -2.571930f);
        pattern_vertexdata[2] = new Vector3f(-4.083636f, -0.000000f, -2.399043f);
        pattern_vertexdata[3] = new Vector3f(-4.026731f, -0.000000f, -2.211450f);
        pattern_vertexdata[4] = new Vector3f(-4.007516f, -0.000000f, -2.016360f);
        pattern_vertexdata[5] = new Vector3f(-4.026731f, -0.000000f, -1.821270f);
        pattern_vertexdata[6] = new Vector3f(-4.083636f, -0.000000f, -1.633677f);
        pattern_vertexdata[7] = new Vector3f(-4.176046f, -0.000000f, -1.460790f);
        pattern_vertexdata[8] = new Vector3f(-4.300409f, -0.000000f, -1.309253f);
        pattern_vertexdata[9] = new Vector3f(-4.451946f, -0.000000f, -1.184890f);
        pattern_vertexdata[10] = new Vector3f(-4.624833f, -0.000000f, -1.092480f);
        pattern_vertexdata[11] = new Vector3f(-4.812426f, -0.000000f, -1.035574f);
        pattern_vertexdata[12] = new Vector3f(-5.007516f, -0.000000f, -1.016360f);
        pattern_vertexdata[13] = new Vector3f(-5.202607f, -0.000000f, -1.035575f);
        pattern_vertexdata[14] = new Vector3f(-5.390200f, -0.000000f, -1.092480f);
        pattern_vertexdata[15] = new Vector3f(-5.563087f, -0.000000f, -1.184891f);
        pattern_vertexdata[16] = new Vector3f(-5.714623f, -0.000000f, -1.309253f);
        pattern_vertexdata[17] = new Vector3f(-5.838986f, -0.000000f, -1.460790f);
        pattern_vertexdata[18] = new Vector3f(-5.931396f, -0.000000f, -1.633677f);
        pattern_vertexdata[19] = new Vector3f(-5.988301f, -0.000000f, -1.821270f);
        pattern_vertexdata[20] = new Vector3f(-6.007516f, -0.000000f, -2.016361f);
        pattern_vertexdata[21] = new Vector3f(-5.988301f, -0.000000f, -2.211451f);
        pattern_vertexdata[22] = new Vector3f(-5.931395f, -0.000000f, -2.399044f);
        pattern_vertexdata[23] = new Vector3f(-5.838985f, -0.000000f, -2.571931f);
        pattern_vertexdata[24] = new Vector3f(-5.714622f, -0.000000f, -2.723467f);
        pattern_vertexdata[25] = new Vector3f(-5.563085f, -0.000000f, -2.847830f);
        pattern_vertexdata[26] = new Vector3f(-5.390198f, -0.000000f, -2.940240f);
        pattern_vertexdata[27] = new Vector3f(-5.202605f, -0.000000f, -2.997145f);
        pattern_vertexdata[28] = new Vector3f(-5.007514f, -0.000000f, -3.016360f);
        pattern_vertexdata[29] = new Vector3f(-4.812424f, -0.000000f, -2.997145f);
        pattern_vertexdata[30] = new Vector3f(-4.624831f, -0.000000f, -2.940239f);
        pattern_vertexdata[31] = new Vector3f(-4.451944f, -0.000000f, -2.847828f);

        pattern_vertexdata[32] = new Vector3f(5.727976f, -0.000000f, -2.710113f);
        pattern_vertexdata[33] = new Vector3f(5.852339f, -0.000000f, -2.558577f);
        pattern_vertexdata[34] = new Vector3f(5.944749f, -0.000000f, -2.385690f);
        pattern_vertexdata[35] = new Vector3f(6.001655f, -0.000000f, -2.198097f);
        pattern_vertexdata[36] = new Vector3f(6.020869f, -0.000000f, -2.003006f);
        pattern_vertexdata[37] = new Vector3f(6.001655f, -0.000000f, -1.807916f);
        pattern_vertexdata[38] = new Vector3f(5.944749f, -0.000000f, -1.620323f);
        pattern_vertexdata[39] = new Vector3f(5.852339f, -0.000000f, -1.447436f);
        pattern_vertexdata[40] = new Vector3f(5.727976f, -0.000000f, -1.295900f);
        pattern_vertexdata[41] = new Vector3f(5.576439f, -0.000000f, -1.171537f);
        pattern_vertexdata[42] = new Vector3f(5.403553f, -0.000000f, -1.079127f);
        pattern_vertexdata[43] = new Vector3f(5.215960f, -0.000000f, -1.022221f);
        pattern_vertexdata[44] = new Vector3f(5.020869f, -0.000000f, -1.003006f);
        pattern_vertexdata[45] = new Vector3f(4.825778f, -0.000000f, -1.022221f);
        pattern_vertexdata[46] = new Vector3f(4.638186f, -0.000000f, -1.079127f);
        pattern_vertexdata[47] = new Vector3f(4.465299f, -0.000000f, -1.171537f);
        pattern_vertexdata[48] = new Vector3f(4.313762f, -0.000000f, -1.295900f);
        pattern_vertexdata[49] = new Vector3f(4.189399f, -0.000000f, -1.447437f);
        pattern_vertexdata[50] = new Vector3f(4.096990f, -0.000000f, -1.620324f);
        pattern_vertexdata[51] = new Vector3f(4.040084f, -0.000000f, -1.807917f);
        pattern_vertexdata[52] = new Vector3f(4.020869f, -0.000000f, -2.003007f);
        pattern_vertexdata[53] = new Vector3f(4.040084f, -0.000000f, -2.198098f);
        pattern_vertexdata[54] = new Vector3f(4.096990f, -0.000000f, -2.385691f);
        pattern_vertexdata[55] = new Vector3f(4.189400f, -0.000000f, -2.558578f);
        pattern_vertexdata[56] = new Vector3f(4.313763f, -0.000000f, -2.710114f);
        pattern_vertexdata[57] = new Vector3f(4.465300f, -0.000000f, -2.834477f);
        pattern_vertexdata[58] = new Vector3f(4.638187f, -0.000000f, -2.926887f);
        pattern_vertexdata[59] = new Vector3f(4.825780f, -0.000000f, -2.983792f);
        pattern_vertexdata[60] = new Vector3f(5.020871f, -0.000000f, -3.003006f);
        pattern_vertexdata[61] = new Vector3f(5.215961f, -0.000000f, -2.983791f);
        pattern_vertexdata[62] = new Vector3f(5.403554f, -0.000000f, -2.926885f);
        pattern_vertexdata[63] = new Vector3f(5.576441f, -0.000000f, -2.834475f);

        pattern_vertexdata[64] = new Vector3f( 0.707107f, -0.000000f, -0.707107f);
        pattern_vertexdata[65] = new Vector3f( 0.831470f, -0.000000f, -0.555570f);
        pattern_vertexdata[66] = new Vector3f( 0.923880f, -0.000000f, -0.382683f);
        pattern_vertexdata[67] = new Vector3f( 0.980785f, -0.000000f, -0.195090f);
        pattern_vertexdata[68] = new Vector3f( 1.000000f, -0.000000f, -0.000000f);
        pattern_vertexdata[69] = new Vector3f( 0.980785f,  0.000000f,  0.195090f);
        pattern_vertexdata[70] = new Vector3f( 0.923880f,  0.000000f,  0.382683f);
        pattern_vertexdata[71] = new Vector3f( 0.831470f,  0.000000f,  0.555570f);
        pattern_vertexdata[72] = new Vector3f( 0.707107f,  0.000000f,  0.707107f);
        pattern_vertexdata[73] = new Vector3f( 0.555570f,  0.000000f,  0.831470f);
        pattern_vertexdata[74] = new Vector3f( 0.382683f,  0.000000f,  0.923880f);
        pattern_vertexdata[75] = new Vector3f( 0.195090f,  0.000000f,  0.980785f);
        pattern_vertexdata[76] = new Vector3f(-0.000000f,  0.000000f,  1.000000f);
        pattern_vertexdata[77] = new Vector3f(-0.195091f,  0.000000f,  0.980785f);
        pattern_vertexdata[78] = new Vector3f(-0.382684f,  0.000000f,  0.923879f);
        pattern_vertexdata[79] = new Vector3f(-0.555571f,  0.000000f,  0.831469f);
        pattern_vertexdata[80] = new Vector3f(-0.707107f,  0.000000f,  0.707106f);
        pattern_vertexdata[81] = new Vector3f(-0.831470f,  0.000000f,  0.555570f);
        pattern_vertexdata[82] = new Vector3f(-0.923880f,  0.000000f,  0.382683f);
        pattern_vertexdata[83] = new Vector3f(-0.980785f,  0.000000f,  0.195090f);
        pattern_vertexdata[84] = new Vector3f(-1.000000f, -0.000000f, -0.000001f);
        pattern_vertexdata[85] = new Vector3f(-0.980785f, -0.000000f, -0.195091f);
        pattern_vertexdata[86] = new Vector3f(-0.923879f, -0.000000f, -0.382684f);
        pattern_vertexdata[87] = new Vector3f(-0.831469f, -0.000000f, -0.555571f);
        pattern_vertexdata[88] = new Vector3f(-0.707106f, -0.000000f, -0.707108f);
        pattern_vertexdata[89] = new Vector3f(-0.555569f, -0.000000f, -0.831470f);
        pattern_vertexdata[90] = new Vector3f(-0.382682f, -0.000000f, -0.923880f);
        pattern_vertexdata[91] = new Vector3f(-0.195089f, -0.000000f, -0.980786f);
        pattern_vertexdata[92] = new Vector3f( 0.000002f, -0.000000f, -1.000000f);
        pattern_vertexdata[93] = new Vector3f( 0.195092f, -0.000000f, -0.980785f);
        pattern_vertexdata[94] = new Vector3f( 0.382685f, -0.000000f, -0.923879f);
        pattern_vertexdata[95] = new Vector3f( 0.555572f, -0.000000f, -0.831469f);


        //in die xy ebene packen (nur debug)  (hier wird eine z coord vernichtet die prinzipielle zugelassen wird)
        for (int vertexcounter = 0; vertexcounter < pattern_vertexdata.length; vertexcounter++) {  
          pattern_vertexdata[vertexcounter].y=-pattern_vertexdata[vertexcounter].z;
          pattern_vertexdata[vertexcounter].z = 0f;
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

        vertexdata = new Vertex3d[96];
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
