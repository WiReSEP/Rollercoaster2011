package de.rollercoaster;

import de.rollercoaster.data.SerializedTrack;
import de.rollercoaster.gui.RollercoasterFrame;
import de.rollercoaster.simulation.RollercoasterSimulation;
import de.rollercoaster.simulation.Simulation;
import java.io.File;

import java.util.logging.Logger;
import java.util.logging.Level;

public class Rollercoaster {

    public static void main(String[] args) {

        Logger.getLogger("com.jme3").setLevel(Level.SEVERE); //shut up stupid Monkey

        Simulation simulation = new RollercoasterSimulation(new SerializedTrack(new File("examples/colossos.xml")));
        // Starte Swing

        RollercoasterFrame frame = new RollercoasterFrame("Rollercoaster 2011", simulation);

        frame.setSize(1024, 768);
        frame.setLocation(20, 20);
        frame.setVisible(true);
    }
}
