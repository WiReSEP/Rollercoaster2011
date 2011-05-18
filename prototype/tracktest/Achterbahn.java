/*Entwicklung eines Meshes zur Darstellung der Bahn... basiert auf Cylinder.java aus der JME3 */
//package com.jme3.scene.shape;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import static com.jme3.util.BufferUtils.*;

import java.io.IOException;
import java.nio.FloatBuffer;

import com.jme3.math.Vector3f;
import com.jme3.math.Matrix3f;



public class Achterbahn extends Mesh {
     private int [][] pattern_index_trips = {{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,0}};/*,
                               {32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,32},
                               {64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,64}};*/

///////////////////////////////////////
    private int axisSamples= 10;

    private int radialSamples = 10;

    private float radius = 1.0f;
    private float radius2 =1.0f;

    private float height = 2.0f;
    private boolean closed = true;
    private boolean inverted = false;

//////////////////////////////////////7

    private Vector3f [] positions;

    private Vector3f [] pattern_vertexdata;
    //private int [][] pattern_index_trips;
    /**
     * Default constructor for serialization only. Do not use.
     */
    public Achterbahn() {
    }


    public Achterbahn(Vector3f [] positions) {
        super();
        this.positions = positions; //TODO: clone methode!

        pattern_vertexdata = new Vector3f[96];
        
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

        //in die yz ebene packen (nur debug)
        for (int vertexcounter = 0; vertexcounter < pattern_vertexdata.length; vertexcounter++) {  
          pattern_vertexdata[vertexcounter].y=pattern_vertexdata[vertexcounter].z;
          pattern_vertexdata[vertexcounter].z = 0f;
        }

        //Kantenzüge
       /* pattern_index_trips = {{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,0},
                               {32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,32},
                               {64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,64}}

*/
        updateGeometry();
    }


    public void updateGeometry() {



        // Vertices (count)   wir brauchen für jede Position in Positions einmal das gesamte Pattern
        int vertCount = pattern_vertexdata.length*positions.length;
        setBuffer(Type.Position, 3, createVector3Buffer(getFloatBuffer(Type.Position), vertCount));
        // Normals
        setBuffer(Type.Normal, 3, createVector3Buffer(getFloatBuffer(Type.Normal), vertCount));
        // Texture co-ordinates
        setBuffer(Type.TexCoord, 2, createVector2Buffer(vertCount));

        //Triangles(count) - zwischen je zwei Punkten auf dem pattern kommen 2 Dreiecke hin ... es wird position+1 viele zylinderstrecken geben 
        int triCount =  2*pattern_vertexdata.length*(positions.length+1);
        setBuffer(Type.Index, 3, createShortBuffer(getShortBuffer(Type.Index), 3 * triCount));


        FloatBuffer nb = getFloatBuffer(Type.Normal);
        FloatBuffer pb = getFloatBuffer(Type.Position);
        FloatBuffer tb = getFloatBuffer(Type.TexCoord);
        IndexBuffer ib = getIndexBuffer();

        /*Poscounter durchläuft alle Positionen
          tripcounter durchläuft alle trips (zusammenhangskomponenten) des Pattern 
          indexcounter durchläuft alle indizies eines trips (bis auf den letzten; da der zum schließen den ersten index enthalt)*/
        for (int poscounter = 0; poscounter < positions.length; poscounter++) {
          System.out.print("=");
          // Vorgänger und Nachfolger sicher merken
          int vorgaengerpos = ((poscounter != 0) ? (poscounter-1): (positions.length-1));
          int nachfolgerpos = ((poscounter != positions.length-1) ? (poscounter+1): (0));
        
          //System.out.printf ("posc = %d vorgänger:%d; nachfolger:%d Bound(%d)\n",poscounter, vorgaengerpos, nachfolgerpos, positions.length);

          // Eingangsrichtung und Ausgangsrichtung in der Position
          //beide vektoren zeigen von der aktuellen position weg!
          Vector3f indir = positions[vorgaengerpos].subtract(positions[poscounter]);
          Vector3f outdir= positions[nachfolgerpos].subtract(positions[poscounter]);

          
          Vector3f pos = new Vector3f(positions[poscounter].getX(),positions[poscounter].getY(),positions[poscounter].getZ());


          //normalisieren
          indir.normalizeLocal();
          outdir.normalizeLocal();


          //Projektionsmatrix um das Pattern in die Bahnkurve (zunächst normal zu indir zu legen)

          float proj_angle = indir.angleBetween(new Vector3f(0,0,-1)); //pattern wurde in 2d gebaut und steht damit normal zu z ... vorzeichen folgt aus der richtung von indir
          
          Vector3f proj_axis = indir.cross(new Vector3f(0,0,-1));
          
          Matrix3f proj_matrix = new Matrix3f();
          proj_matrix.fromAngleAxis(proj_angle,proj_axis);



          //lokale Rotationswinkel und achse erzeugen
          float angle = indir.angleBetween(outdir);

          //if (poscounter == positions.length-1) angle *= -1;

          //Todo: das kreuzprodukt schlägt fehl wenn in und out linear abhängig! --> sonderfall abfangen
          Vector3f rot_axis = indir.cross(outdir); //hier ist es relativ egal wie rum das kreuzprodukt gebildet wird (denke ich)
          rot_axis.normalizeLocal();
          //Rotationsmatrix erzuegen (auf die winkelhalbierende)
          Matrix3f rot_matrix = new Matrix3f();
          rot_matrix.fromAngleAxis(-angle/2,rot_axis); // DEBUG Ggf negativer winkel (wegen rotationsrichtung)
     
           System.out.printf ("In %s  Out %s\n",indir,outdir);
           System.out.printf ("%f  |- %s --> \n",angle,rot_axis);
           System.out.printf ("%s\n",rot_matrix);

          //rot_matrix.multLocal(proj_matrix); //zusammenfassen (convinience damit nicht hinterher immer zwei matrizen dastehen)
          rot_matrix= proj_matrix.mult(rot_matrix);

          int laenge = poscounter; // DEBUG .. this is dummy
          
          for (int vertexcounter = 0; vertexcounter < pattern_vertexdata.length; vertexcounter++) {  
                //System.out.printf ("Counter: %d Bound(%d) Matrix <%s> Vector <%s>\n",vertexcounter,pattern_vertexdata.length,rot_matrix,pattern_vertexdata[vertexcounter]);
                Vector3f tmpPos = pos.add(rot_matrix.mult(pattern_vertexdata[vertexcounter]));
                
              ////////////////////////Normalen berechnung ; nicht sehr schön (ggf mitübergeben beim pattern!)
                //wir brauchen den vorgänger und nachfolger im pattern
                int v=0,n=0; //vorgänger und nachfolger
            
                for (int tripcounter = 0; tripcounter < pattern_index_trips.length; tripcounter++) {
                  for (int indexcounter = 0; indexcounter < pattern_index_trips[tripcounter].length-1; indexcounter++) {
                    if (pattern_index_trips[tripcounter][indexcounter] == vertexcounter) {
                      v = (indexcounter > 0)? pattern_index_trips[tripcounter][indexcounter-1]: pattern_index_trips[tripcounter][pattern_index_trips[tripcounter].length-2];
                      n = pattern_index_trips[tripcounter][indexcounter+1]; //durch den doppelten index ist dieser sicher
                    }
                  }
                }
                
                Vector3f tmpNormal1 = pattern_vertexdata[v].subtract(tmpPos.subtract(pos)).cross(outdir.subtract(indir));
                Vector3f tmpNormal2 = pattern_vertexdata[n].subtract(tmpPos.subtract(pos)).cross(outdir.subtract(indir));
                tmpNormal1.normalizeLocal();
                tmpNormal2.normalizeLocal();
                Vector3f tmpNormal = tmpNormal1.add(tmpNormal2);
                tmpNormal.normalizeLocal();

              ///////////////////////////////////////////////////////7


//                 System.out.printf ("%s\n",tmpPos);
                
                pb.put(tmpPos.x).put(tmpPos.y).put(tmpPos.z);
                nb.put(tmpNormal.x).put(tmpNormal.y).put(tmpNormal.z);
                tb.put(vertexcounter/pattern_vertexdata.length).put(laenge);
          }
        }
       //2 schleifen für mehr kontrolle ... so können alle punkte erzeugt werden ohne, dass die Dreiecke gebaut werden müssen
        int index = 0;
        int triplength = pattern_vertexdata.length; //wieviele indizes benötigt ein trip (also einmal das zu extrudierende pattern)
        int segmentlength = pattern_vertexdata.length; //check

                                                      //Das letzte Segment muss schließen und  kann damit nicht automatisch benutzt werden
                                                            // |
                                                            // v
        for (int poscounter = 0; poscounter < positions.length-1; poscounter++) {
          //pattern_vertexdata.length*positions.length;




//ACHTUNG Vertexreihenfolge bis jetzt nicht geprüft

          for (int tripcounter = 0; tripcounter < pattern_index_trips.length; tripcounter++) {
            for (int indexcounter = 0; indexcounter < pattern_index_trips[tripcounter].length-2; indexcounter++) {

                    ib.put(index++, poscounter*segmentlength+tripcounter*triplength+indexcounter);
                    ib.put(index++, poscounter*segmentlength+tripcounter*triplength+indexcounter+1);
                    ib.put(index++, poscounter*segmentlength+(tripcounter+1)*triplength+indexcounter);
                    ib.put(index++, poscounter*segmentlength+tripcounter*triplength+indexcounter+1);
                    ib.put(index++, poscounter*segmentlength+(tripcounter+1)*triplength+indexcounter+1);
                    ib.put(index++, poscounter*segmentlength+(tripcounter+1)*triplength+indexcounter);

            }
                    ib.put(index++, poscounter*segmentlength+tripcounter*triplength+pattern_index_trips[tripcounter].length-2);
                    ib.put(index++, poscounter*segmentlength+tripcounter*triplength+0);
                    ib.put(index++, poscounter*segmentlength+(tripcounter+1)*triplength+pattern_index_trips[tripcounter].length-2);
                    ib.put(index++, poscounter*segmentlength+tripcounter*triplength+0);
                    ib.put(index++, poscounter*segmentlength+(tripcounter+1)*triplength+0);
                    ib.put(index++, poscounter*segmentlength+(tripcounter+1)*triplength+pattern_index_trips[tripcounter].length-2);
          }
//break;
        } //end fpr pos
        //letzes Segment manuel
          for (int tripcounter = 0; tripcounter < pattern_index_trips.length; tripcounter++) {
            for (int indexcounter = 0; indexcounter < pattern_index_trips[tripcounter].length-2; indexcounter++) {

                    ib.put(index++, (positions.length-1)*segmentlength+tripcounter*triplength+indexcounter);
                    ib.put(index++, (positions.length-1)*segmentlength+tripcounter*triplength+indexcounter+1);
                    ib.put(index++, indexcounter);
                    ib.put(index++, (positions.length-1)*segmentlength+tripcounter*triplength+indexcounter+1);
                    ib.put(index++, indexcounter+1);
                    ib.put(index++, indexcounter);

            }
                    ib.put(index++, (positions.length-1)*segmentlength+tripcounter*triplength+pattern_index_trips[tripcounter].length-2);
                    ib.put(index++, (positions.length-1)*segmentlength+tripcounter*triplength+0);
                    ib.put(index++, pattern_index_trips[tripcounter].length-2);
                    ib.put(index++, (positions.length-1)*segmentlength+tripcounter*triplength+0);
                    ib.put(index++, 0);
                    ib.put(index++, pattern_index_trips[tripcounter].length-2);
          }




        updateBound();
        
    }

    public void read(JmeImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
        axisSamples = capsule.readInt("axisSamples", 0);
        radialSamples = capsule.readInt("radialSamples", 0);
        radius = capsule.readFloat("radius", 0);
        radius2 = capsule.readFloat("radius2", 0);
        height = capsule.readFloat("height", 0);
        closed = capsule.readBoolean("closed", false);
        inverted = capsule.readBoolean("inverted", false);
    }

    public void write(JmeExporter e) throws IOException {
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(axisSamples, "axisSamples", 0);
        capsule.write(radialSamples, "radialSamples", 0);
        capsule.write(radius, "radius", 0);
        capsule.write(radius2, "radius2", 0);
        capsule.write(height, "height", 0);
        capsule.write(closed, "closed", false);
        capsule.write(inverted, "inverted", false);
    }


}
