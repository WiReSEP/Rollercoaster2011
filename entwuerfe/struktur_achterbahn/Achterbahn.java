/** Containerklasse die die prozedural generierte Bahn mit den eingefügten Objektelementen verbindet.*/



public class Achterbahn extends Node{
  //Diese Knoten werden effektiv angehängt und stehen hier nur als zusatzreferenz zur Verfügung
  private Node curve;
  private Node joints;
  private Node poles;

  private Mesh jointmesh;
  private Mesh polemesh;

  //nur ein neuer Konstruktor
  public Achterbahn  (/*Filename und anderes*/) {
    super();
    curve.addchild (new PatternCurve(Curve,Pattern));
    //So ich die Szenegraphenstruktur richtig verstanden habe, reicht es das meshobjekt einmal zu laden, und es dann unter unterschiedliche Wurzeln zu hängen  um es mehrfach in der szene zu haben
    jointmesh.loadfromfile (filename);
    for (was weiß ich) {
      joints.addChild (new Joint(pos,lage,jointmesh));
    }

    //wiederverwendung auch für die poles
    polemesh.loadfromfile (filename2);
    for (was weiß ich) {
      poles.addChild (new Pole(pos,lage,polemesh));
    }


    this.addChild(curve);
    this.addChild(joints);
    this.addChild(poles);
  }

}
