//Klasse die an der eine MeshReferenz hängt und deren Konstruktor direkt Translation und Rotation aufnimmt
//Schlanke Klasse im Szenegraphen die immer wieder auf das gleiche Objekt verweisen können und somit speicher sparen.
//Da die Rotation nur einmal lokal angefasst werden muss (zur Erstellungszeit) macht der Konstruktor den Vorgang überschaubarer

public class Joint extends Node {
 private Mesh mesh; 
} 
