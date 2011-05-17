package de.rollercoaster.gui;

import de.rollercoaster.graphics.View;
import java.awt.Canvas;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class RollercoasterFrame extends JFrame {
    private final View graphics;
    private final JPanel panel;

    public RollercoasterFrame(String title, final View view) {
        super(title);

        this.graphics = view;
        this.panel = new JPanel(new FlowLayout());

        view.init();
        Canvas graphicsCanvas = view.getCanvas();

        panel.add(graphicsCanvas);
        panel.add(new JButton("Swing Component"));      // add some Swing   

        add(panel);
        pack();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                graphics.dispose();
            }
        });
    }
}
