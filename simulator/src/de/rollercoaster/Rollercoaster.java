package de.rollercoaster;

import de.rollercoaster.data.SerializedTrack;
import de.rollercoaster.graphics.RollercoasterView;
import de.rollercoaster.graphics.View;
import de.rollercoaster.gui.RollercoasterFrame;
import java.io.File;

public class Rollercoaster {

    public static void main(String[] args) {
        
        // TODO: Die Initialisierung muss von der GUI übernommen werden!
        // TODO: Es muss ein Simulator erzeugt werden.
        
        SerializedTrack reader = new SerializedTrack(new File("examples/colossos.xml"));
        reader.read();
        
        View graphics = new RollercoasterView(reader.getCurve());
       
        // Starte Swing
        
        RollercoasterFrame frame = new RollercoasterFrame("Rollercoaster 2011", graphics);

        frame.setSize(1024, 768);
        frame.setLocation(20, 20);
        frame.setVisible(true);
    }
}
