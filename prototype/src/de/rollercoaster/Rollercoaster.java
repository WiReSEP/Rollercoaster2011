package de.rollercoaster;

import de.rollercoaster.data.SerializedTrack;
import de.rollercoaster.graphics.RollercoasterView;
import de.rollercoaster.graphics.View;
import de.rollercoaster.gui.RollercoasterFrame;
import java.io.File;

public class Rollercoaster {

    public static void main(String[] args) {
        // Lade Probedatei
        SerializedTrack reader = new SerializedTrack(new File("examples/colossos.xml"));
        reader.read();

        // Starte Swing
        View graphics = new RollercoasterView();
        RollercoasterFrame frame = new RollercoasterFrame("Rollercoaster 2011", graphics);

        frame.setSize(1024, 768);
        frame.setLocation(20, 20);
        frame.setVisible(true);
    }
}
