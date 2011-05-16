
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jme3.system.JmeCanvasContext;

public class EngineContainer extends JPanel {
    private Graphics3D engine = null;
    private Canvas canvas = null;
    private JmeCanvasContext context = null;

    public EngineContainer(JFrame parent, int resx, int resy) {
        super(new FlowLayout());
        engine = new Graphics3D();
        context = engine.getCanvasObject();
        canvas = context.getCanvas();
        add(canvas);

        parent.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                engine.stop();
            }
        });
    }
}
