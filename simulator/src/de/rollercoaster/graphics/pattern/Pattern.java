package de.rollercoaster.graphics.pattern;

/**Kapselt ein Pattern für das Extrudieren entlang einer Bahn.  
Die Eckpunkte des Pattern (Vertices) sind über getVertex durch einen VertexIndex zu erhalten.
Um mehrere Zusammenhangskomponenten zu erlauben werden Trips vorgesehen. 
Ein Trip ist dabei effektiv ein Kreis über den Vertices.*/

public interface Pattern {
  /**Liefert die Anzahl an Trips (Zusammenhangskomponenten) im Pattern*/
  int getTripCount();
  /**Liefert die Anzahl an Vertices (Ecken) des Pattern*/
  int getVertexCount();
  /**Liefert die Länge des pspezifizierten Trips*/
  int getTripLength(int tripnr);
  /**Liefert die vertexnr die an der Position index im geschlossenem Kantenzug (trip)  tripnr steht */
  int getVertexIndex(int tripnr, int index);
  /**Liefert die Daten des Vertex mit der vertexnr.*/
  Vertex3d getVertex(int vertexnr);
} 
