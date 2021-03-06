package de.rollercoaster.graphics.pattern;

import com.jme3.math.Vector3f;
import java.util.LinkedList;
import java.io.*;

/**Lädt ein Pattern aus einer Datei. Das Pattern muss aus Blender als OBJ-Wavefront exportiert werden. Zur Trennung von Objekten müssen Groups eingestellt werden. 
Jede Gruppe wird als eigner Trip aufgefasst der im Pattern dann als eigene Extrudeform betrachtet wird.  */ 

public class FilePattern implements Pattern {

  /**Hilfsklasse Vertex. Vertex ist eine Struktur einer doppeltverketteten mitunter zirkulären Liste im Aufbau. Es können die diversen Fragmente eingefügt und ggf umgedreht werden.
  Die Umdrehmethode auf eine fertige zyklische Referenzierung wird Fehlerhafte resultate liefern (außer max= |V|/2) und V ungerade. Die Klasse ist ansonsten ein einfacher Container und hat daher keine Getter/Setter*/
  class Vertex {
    public Vertex pre;
    public Vertex post;
    public int value;

    /**Einfacher Konstruktor zum Verpacken der Daten.*/
    public Vertex (int value, Vertex pre, Vertex post) {
      this.value = value;
      this.pre = pre;
      this.post = post;

    }

    @Override
    public boolean equals (Object o) {
      if ((o == null) || (!(o instanceof Vertex))) return false;
      Vertex v = (Vertex)o;
      return (this.value==v.value);
    }


    /**Dreht alle an die aufrufende Instanz angehängten Objekte bzgl ihrer Vorgänger und Nachfolgereingenschaft um. Bildlicher bedeutet das, dass jedes Objekt in dem Segment sich umdreht*/
    public void turn_links (int max) {
      if (max <= 0) return;
      if (post != null) post.turn_links_post(max-1);
      if (pre != null) pre.turn_links_pre(max-1);
      Vertex tmp = pre;
      pre= post;
      post = tmp;
    } 

    /**Rekursive Methode die das Umdrehen in Richtung der Vorgänger weiterreicht.*/
    private void turn_links_pre (int max) {
      if (max <= 0) return;
      if (pre != null) pre.turn_links_pre(max-1);
      Vertex tmp = pre;
      pre= post;
      post = tmp;
   } 
    /**Rekursive Methode die das Umdrehen in Richtung der Nachfolger weiterreicht.*/
    private void turn_links_post (int max) {
      if (max <= 0) return;
      if (post != null) post.turn_links_post(max-1);
      Vertex tmp = pre;
      pre= post;
      post = tmp;
      
    } 

    @Override 
    public String toString() {
      return String.format("<-- %s --- ( %s ) --- %s -->\n",(pre!= null)?pre.value:"null" ,value,(post!= null)?post.value:"null");
    }

  }


  private int [][] pattern_index_trips;
  private Vertex3d [] vertexdata;

  /**Lädt das Pattern aus der Datei. Es werden Exceptions geworfen wenn das Pattern der Datei kein Kreis ist. Völlig unlesbare Zeilen werden ignoriert.*/
  public FilePattern (String filename) throws IllegalArgumentException,FileNotFoundException,IOException {
    //Datei laden und öffnen
    File file_handle = new File(filename);
    FileReader filereader = new FileReader(file_handle);
    BufferedReader bufferedreader = new BufferedReader(filereader);

    //Datei interpretieren
    String line;
    LinkedList<Vector3f> verticies = new LinkedList<Vector3f>();  //Die Eckpunkte speichern
    LinkedList<int[]> trips = new LinkedList<int[]>(); //die trips
    LinkedList<Vertex> vertexcircle = null; //die reihenfolge der Eckpunkte


    while((line=bufferedreader.readLine())!=null){
       switch (line.split(" ")[0].charAt(0)) {
          case '#': continue; //Kommentare
          case 'g': // nächsten trip anfangen 
                    if (vertexcircle != null) {
                      int[] cycle = new int[vertexcircle.size()];
                      Vertex v = vertexcircle.get(0).pre;
                      for (int i = 0 ; i < vertexcircle.size(); i++) {
                        v= v.post;
                        if (v== null) {throw new IllegalArgumentException ("Given Patten is no circle!");}
                        cycle[i] = v.value;
                      }
                      trips.addLast(cycle);
                    }
                    vertexcircle = new LinkedList<Vertex>();
                    break;

          case 'v': //Verticies können direkt in die Liste geladen werden; Indizes werden fortlaufend Vergeben (Dateiformatspezifikation)
                    verticies.addLast(new Vector3f((float)Double.parseDouble(line.split(" ")[1]),(float)Double.parseDouble(line.split(" ")[2]),(float)Double.parseDouble(line.split(" ")[3]))); break;

          case 'f': //Ein Face wird interpretiert
                    //Ein Face (hier also eine Kante) besteht aus 2 Knoten a und b. Es muss unterschieden werden ob a oder b bereits vorhanden sind und somit potenziell schon einen Vorgänger oder Nachfolger haben
                    //entsprechend muss der jeweils andere an die Segmente angefügt werden. Außerdem gibt es den Fall, dass beide bereits vorhanden sind und die Segmente verbunden werden müssen. Dabei muss ggf ein Segment
                    // gedreht werden. Da a und b nur die Indizes von faces sind stellen va und vb die zugehörigen Vertex-Objekte dar
                    int a = Integer.parseInt(line.split(" ")[1]);
                    int b = Integer.parseInt(line.split(" ")[2]);
                    int posa, posb;
                    if ((posa = vertexcircle.indexOf(new Vertex(a,null,null))) != -1) {
                       if ((posb = vertexcircle.indexOf(new Vertex(b,null,null))) != -1) {
                          //a und b vorhanden
                          Vertex va = vertexcircle.get(posa);
                          Vertex vb = vertexcircle.get(posb);
                          //Wir müssen den Link zwischen a und b herstellen. Auch wenn er schon gesetzt ist, dann folgt dass das Dateiformat invalide ist. 
                          //Es reicht also nach einem freien Slot für die Verbindung ausschau zu halten 
                          //Ggf müssen wir noch an einem Knoten umdrehen
                          if (va.pre == null) {
                            if (vb.post == null) {
                               vb.post = va;
                               va.pre = vb;
                            }
                            else if (vb.pre == null) {
                              va.pre = vb;
                              vb.turn_links(vertexcircle.size()); //umkehren dieses Strangs
                              vb.post = va;
                            }
                              else {throw new IllegalArgumentException ("Nodes with degree > 2 not supported [0]"); }
                          }
                          else if (va.post == null) {
                            if (vb.pre == null) {
                               vb.pre = va;
                               va.post = vb;
                            }
                            else if (vb.post == null) {
                              va.post = vb;
                              vb.turn_links(vertexcircle.size()); //umkehren dieses Strangs
                              vb.pre = va;
                            }
                              else {throw new IllegalArgumentException ("Nodes with degree > 2 not supported [1]");}
                          }
                          else {throw new IllegalArgumentException ("Nodes with degree > 2 not supported [2]"); }                    
                                                    
                       }
                       else {
                          //a vorhanden aber nicht b
                          Vertex va = vertexcircle.get(posa);
                          if (va.pre == null) {
                            Vertex vb = new Vertex(b,null,va);
                            va.pre = vb;
                            vertexcircle.addLast(vb);
                          } 
                          else if (va.post == null) {
                            Vertex vb = new Vertex(b,va,null);
                            va.post = vb;
                            vertexcircle.addLast(vb);
                          } 
                          else {throw new IllegalArgumentException ("Nodes with degree > 2 not supported [3]"); }    
                       }
                    }
                    else if ((posb = vertexcircle.indexOf(new Vertex(b,null,null))) != -1) {
                      // a nicht vorhanden aber b
                      Vertex vb = vertexcircle.get(posb);
                      if (vb.pre == null) {
                        Vertex va = new Vertex(a,null,vb);
                        vb.pre = va;
                        vertexcircle.addLast(va);
                      } 
                      else if (vb.post == null) {
                         Vertex va = new Vertex(a,vb,null);
                         vb.post = va;
                         vertexcircle.addLast(va);
                      } 
                        else {throw new IllegalArgumentException ("Nodes with degree > 2 not supported [4]"); }    

                    }
                    else {
                      //Weder a noch b sind vorhanen
                      Vertex va = new Vertex(a,null,null);
                      Vertex vb = new Vertex(b,va,null);
                      va.post = vb; 
                      vertexcircle.addLast(va);
                      vertexcircle.addLast(vb);
                    }
                    break;

          default:  System.err.println ("Skipping Line  - Unknown Token "+line.split(" ")[0].charAt(0)+" found where #/g/v/f was expected");
       }
    }
    
    //Normalerweise wird das Schließen der Trips beim Fund eines neuen Trips vorgenommen. Der letzte Trip braucht damit eine Sonderbehandlung
    if (vertexcircle != null) {
      int[] cycle = new int[vertexcircle.size()];
      Vertex v = vertexcircle.get(0).pre;
      for (int i = 0 ; i < vertexcircle.size(); i++) {
        v= v.post;
        if (v== null) {throw new IllegalArgumentException ("Given Patten is no circle!");}
        cycle[i] = v.value;
      }
      trips.addLast(cycle);
    }

   
   //Trips kopieren - in das Array einfügen 
   pattern_index_trips = new int[trips.size()][] ;
   for (int i = 0; i < trips.size(); i++) {
      pattern_index_trips[i] = new int[trips.get(i).length];
      for (int k = 0; k < trips.get(i).length; k++) {
        pattern_index_trips[i][k] = trips.get(i)[k]-1; //-1 da wir mit index 0 starten
      }
   }

  //Verticies kopieren
   vertexdata = new Vertex3d[verticies.size()];
   for (int i = 0; i < verticies.size(); i++) 
    vertexdata[i] = new Vertex3d(verticies.get(i));

  //Normalen berechnen
  //Für die Normalenerzeugung wird ein Trick angewandt: Suche den Mittelpunkt einer Zusammenhangskomponente (trip) und verlängere den vektor von dort zu einem Vertex auf die Länge 1 
  // Dieses Verfahren ist nicht sehr robust gegenüber rapiden Formabweichungen, jedoch deutlich schneller als alle Varianten die das Problem allgemein lösen (vgl Point in Polygon-problem (PIP))
  Vector3f [] pattern_vertexdata_normal = new Vector3f[vertexdata.length];

  for (int tripcounter = 0; tripcounter < pattern_index_trips.length; tripcounter++) {
    Vector3f center = new Vector3f(0f,0f,0f);
    //Orte aufsummieren um Mitte zu erhalten
    for (int indexcounter = 0; indexcounter < pattern_index_trips[tripcounter].length; indexcounter++) {
      center.addLocal(vertexdata[pattern_index_trips[tripcounter][indexcounter]].position);
    }
    //mitteln
    center.multLocal((float)1.0/pattern_index_trips[tripcounter].length);  
    
    //Vektoren erzeugen, normalisieren und zuweisen
    for (int indexcounter = 0; indexcounter < pattern_index_trips[tripcounter].length; indexcounter++) {
      pattern_vertexdata_normal[pattern_index_trips[tripcounter][indexcounter]]= vertexdata[pattern_index_trips[tripcounter][indexcounter]].position.subtract(center).normalize();
    }          
  }

  for (int vertexcounter = 0; vertexcounter < vertexdata.length; vertexcounter++) {  
      vertexdata[vertexcounter].normal = pattern_vertexdata_normal[vertexcounter];
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
