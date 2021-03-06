//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.19 at 08:53:38 PM MESZ 
//


package de.rollercoaster.data.xml;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.rollercoaster.data.xml package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.rollercoaster.data.xml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RollerCoaster }
     * 
     */
    public RollerCoaster createRollerCoaster() {
        return new RollerCoaster();
    }

    /**
     * Create an instance of {@link SimulationParameters }
     * 
     */
    public SimulationParameters createSimulationParameters() {
        return new SimulationParameters();
    }

    /**
     * Create an instance of {@link Track }
     * 
     */
    public Track createTrack() {
        return new Track();
    }

    /**
     * Create an instance of {@link Pillar }
     * 
     */
    public Pillar createPillar() {
        return new Pillar();
    }

    /**
     * Create an instance of {@link PillarList }
     * 
     */
    public PillarList createPillarList() {
        return new PillarList();
    }

    /**
     * Create an instance of {@link General }
     * 
     */
    public General createGeneral() {
        return new General();
    }

}
