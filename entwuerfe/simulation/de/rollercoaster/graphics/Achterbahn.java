package de.rollercoaster.graphics;

//eigene
import de.rollercoaster.mathematics.*;
import de.rollercoaster.graphics.pattern.*;
//Jmonkey
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.math.Matrix3f;

import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;

import com.jme3.util.BufferUtils;
import static com.jme3.util.BufferUtils.*;

//Standartjava
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.List;

/**Achterbahn stellt ein MeshObjekt dar, welches nach den Daten einer Curve erzeugt wird. In jedem Punkt wird in die Ebene die durch Pitch und Yaw definiert ist ein vorgegebenes Pattern projiziert und mit dem gleichen 
 Pattern an der vorangegangenenen Stützstelle verbunden. Dadurch wird das Pattern entlang der Curve extrudiert. 
 <br> <br>
 Dev:  Aktuell wird das Pattern noch fest vorgegeben.

@author Matthias
  
*/
public class Achterbahn extends Mesh {

     /**Die Indexliste unseres Pattern. Da es sich um einen Querschnitt handelt, muss es sich um geschlossene Kantenzüge handeln. In der Folge werden hier im ersten Index die Kantenzüge und im zweiten die Ecken unterschieden.
      Die Indizes verweisen auf auf die pattern_vertexdata bzw die zugehörigen Normalen.
      <br> <br>
      Dev: Die gesamte Patternstruktur sollte eigentlich durch ein Objekt gekapselt werden*/

    /**Lokaler >>Verweis<< auf die Kurve. */
    private Curve curve;

    /**Vertexdaten (Position und normalen). */
    private Pattern pattern;    

    /**
     * Default constructor for serialization only. Do not use.
     */
    public Achterbahn() {
    }

    /**Speichert das Bahnobjekt für die Erzeugung zwischen und erzeugt das Objekt mittels Aufruf von updateGeometry. 
    Es wird ein SimplePattern erzeugt.*/
    public Achterbahn(Curve curve) {
       this(curve,new SimplePattern());
    }

    /**Speichert das Bahnobjekt für die Erzeugung zwischen und erzeugt das Objekt mittels Aufruf von updateGeometry. 
    Ein Pattern für das Bahnprofil ist frei wählbar*/
    public Achterbahn(Curve curve, Pattern pattern) {
      super();
      //Referenz speichern
      this.curve = curve;  
      this.pattern = pattern;
      updateGeometry();      
    }

    /**Erzeugt die Geometrie unserer Bahn wenn ein Update angefordert wird oder das Objekt erzeugt wird.  */
    public void updateGeometry() {

        //Liste holen ... die Paramter tuen bis jetzt nix da nur die dummyliste erwartet wird
        List<CurvePoint> points = curve.getPointSequence(0.0,0.0);

        /*Wir müssen uns den Speicher für jedes Eckpunkt beschaffen und dessen Koordinaten festlegen. Mittels Indizes können diese Eckpunkte dann zu 
          Dreiecken zusammengebaut werden.*/
        // Vertices (count)   wir brauchen für jede Position in Positions einmal das gesamte Pattern
        int vertCount = pattern.getVertexCount()*points.size();
        setBuffer(Type.Position, 3, createVector3Buffer(getFloatBuffer(Type.Position), vertCount));
        // Normals
        setBuffer(Type.Normal, 3, createVector3Buffer(getFloatBuffer(Type.Normal), vertCount));
        // Texture co-ordinates
        setBuffer(Type.TexCoord, 2, createVector2Buffer(vertCount));

        //Triangles(count) - zwischen je zwei Punkten auf dem pattern kommen 2 Dreiecke hin ... es wird position+1 viele zylinderstrecken geben 
        int triCount =  2*pattern.getVertexCount()*(points.size()+1);
        setBuffer(Type.Index, 3, createShortBuffer(getShortBuffer(Type.Index), 3 * triCount));


        FloatBuffer nb = getFloatBuffer(Type.Normal);
        FloatBuffer pb = getFloatBuffer(Type.Position);
        FloatBuffer tb = getFloatBuffer(Type.TexCoord);
        IndexBuffer ib = getIndexBuffer();

        //Wir speichern uns die errechneten Positionen und Normalen für später zwischen (sonst müssen wir die beim cullcheck aus den buffern rekonstruieren)
        Vertex3d [][] allvertices = new Vertex3d[points.size()][pattern.getVertexCount()];
        

        /*In einem ersten Schritt erzeugen wir die Daten für die Eckpunkte. Dafür müssen wir alle Punkt der Bahn (poscounter) ablaufen und dort ein Pattern erstellen.
          Um Pattern zu erstellen legen wir alle Vertexdaten (Vertexcounter) im Puffer an.*/
        for (int poscounter = 0; poscounter < points.size(); poscounter++) {
          
          // Vorgänger und Nachfolger sicher merken (wir möchten eine geschlossene Bahn)

         /*Einige Vektoren kürzen wir ein wenig ab, da wir sie häufiger bennötigen:
          pos: die Rohposition der Stützstelle aus der Curve. Der Ursprung unseres Pattern wird dorthin projiziert
          x: die X Koordinate des lokalen koordinatensystems in das wir projizieren (aus Weltsicht)
             eine XKoordinate im Pattern kann schnell umgerechnet werden in dem eine Multiplikation mit diesem Vektor vorgenommen wird
          y: die Y Koordinate .... siehe X koordinatensystems
          z: die Z Koordinate .... siehe X koordinatensystems*/
          Vector3f pos = points.get(poscounter).getPosition().toF();
          Vector3f x = points.get(poscounter).getPitchAxis().normalize().toF();
          Vector3f y = points.get(poscounter).getYawAxis().normalize().toF();
          Vector3f z = points.get(poscounter).getRollAxis().normalize().toF();





          //Die Länge wir bei uns nur für Texturkoordinaten benutzt. Da zunächst keine Textur vorgesehen ist, macht es nichts wenn hier zunächst die Stützstellennummer gewählt wird
          int laenge = poscounter; // DEBUG .. this is dummy
       
          /*Erzeugen der Positionen*/
          for (int vertexcounter = 0; vertexcounter < pattern.getVertexCount(); vertexcounter++) {  
                //Position des vertex ermitteln  (Umrechnung erfolgt so dass auch 3D Pattern zulässig sind)
                Vector3f tmpPos = pos.add(x.mult(pattern.getVertex(vertexcounter).position.x));
                tmpPos.addLocal (y.mult(pattern.getVertex(vertexcounter).position.y));
                tmpPos.addLocal (z.mult(pattern.getVertex(vertexcounter).position.z));
                //Normale umrechnen (muss auch projiziert werden) (Umrechnung erfolgt so dass auch 3D Pattern zulässig sind)
                Vector3f tmpNormal = x.mult(pattern.getVertex(vertexcounter).normal.x);
                tmpNormal.addLocal (y.mult(pattern.getVertex(vertexcounter).normal.y));
                tmpNormal.addLocal (z.mult(pattern.getVertex(vertexcounter).normal.z));

                //Position und Normale merken (da die Objekte nicht weiter verwendet werden, brauchen wir nicht clonen!)
                allvertices[poscounter][vertexcounter] = new Vertex3d(tmpPos,tmpNormal);

                //Koordinaten in die Puffer schieben 
                pb.put(tmpPos.x).put(tmpPos.y).put(tmpPos.z);   //Position
                nb.put(tmpNormal.x).put(tmpNormal.y).put(tmpNormal.z);  //Normalen
                tb.put(vertexcounter/pattern.getVertexCount()).put(laenge); //Texturkoordinaten
          }
        }
       


        /*Erzeugung des Index */
        int index = 0; //wir müssen selbst für eine durchnummerierung sorgen
        int segmentlength = pattern.getVertexCount(); //wir merken uns, wieviele ecken wir überspringen müssen um zur "gleichen" Ecke im Pattern an der nächsten Position zu kommen

        
        for (int poscounter = 0; poscounter < points.size(); poscounter++) {   //für jede Psoition
          Vector3f roll = points.get(poscounter).getRollAxis().normalize().toF();
          for (int tripcounter = 0; tripcounter < pattern.getTripCount(); tripcounter++) { //für jeden Trip
            for (int indexcounter = 0; indexcounter < pattern.getTripLength(tripcounter); indexcounter++) { //für jeden Knoten
                    
                    int nextposcounter = ((poscounter+1)%points.size());  //"gleiche" Ecke an nächster Position
                    int nextindexcounter = (indexcounter+1)%pattern.getTripLength(tripcounter); //nächste Ecke an gleicher Position


                    //Wir schauen wo die Normale hinzeigt die man aus dem Kreuzprodukt erhält und vergleichen dann mit der vertexnormale
                    //So können wir entscheiden welche Reihenfolge die richtige ist, sodass Backfaceculling aktiv bleiben kann
                    Vector3f moveInPattern = allvertices[poscounter][pattern.getVertexIndex(tripcounter,nextindexcounter)].position.subtract(allvertices[poscounter][pattern.getVertexIndex(tripcounter,indexcounter)].position);
                    Vector3f crossNormal = roll.cross(moveInPattern).normalize();
                    Vector3f normal = allvertices[poscounter][pattern.getVertexIndex(tripcounter,nextindexcounter)].normal.normalize();

              
                    //Reihenfolge entgegen dem Uhrzeigersinn (Opengl ermittelt aus der Richtung der Definition der Eckpunkte welche Dreiecke sichtbar sind. Diese Reihefolge funktioniert zumindest mit diesem Pattern)
                    if (normal.dot(crossNormal) > 0) {
                      ib.put(index++, poscounter*segmentlength+ pattern.getVertexIndex (tripcounter,indexcounter));
                      ib.put(index++, nextposcounter*segmentlength+ pattern.getVertexIndex (tripcounter,indexcounter));                     
                      ib.put(index++, poscounter*segmentlength+ pattern.getVertexIndex (tripcounter,nextindexcounter));
                     
                      ib.put(index++, poscounter*segmentlength+ pattern.getVertexIndex (tripcounter,nextindexcounter));
                      ib.put(index++, nextposcounter*segmentlength+ pattern.getVertexIndex (tripcounter,indexcounter));  
                      ib.put(index++, nextposcounter*segmentlength+ pattern.getVertexIndex (tripcounter,nextindexcounter));  
                    }
                    else {
                      ib.put(index++, poscounter*segmentlength+ pattern.getVertexIndex (tripcounter,indexcounter));
                      ib.put(index++, poscounter*segmentlength+ pattern.getVertexIndex (tripcounter,nextindexcounter));
                      ib.put(index++, nextposcounter*segmentlength+ pattern.getVertexIndex (tripcounter,indexcounter));                     

                     
                      ib.put(index++, poscounter*segmentlength+ pattern.getVertexIndex (tripcounter,nextindexcounter));
                      ib.put(index++, nextposcounter*segmentlength+ pattern.getVertexIndex (tripcounter,nextindexcounter));  
                      ib.put(index++, nextposcounter*segmentlength+ pattern.getVertexIndex (tripcounter,indexcounter));  
                    }

            }
          }
        } 

        updateBound();
        
    }

    /*Writer und Reader Methode:
      JMonkey sieht vor, dass jedes Objekt geschrieben (in eine Datei) und auch wieder gelesen werden kann. Aktuell sind diese Methode nicht implementiert. 
      TODO: Implementierung dieser Funktionen (soweit als Sinnig erachtet)*/

    public void read(JmeImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
    }

    public void write(JmeExporter e) throws IOException {
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
    }


}
