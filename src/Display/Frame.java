package Display;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    public static final String DEFAULT_TITLE = "MyGame";
    public static final int DEFAULT_WIDTH = 1300;
    public static final int DEFAULT_HEIGHT = 800;

    private final Panel panel;

    //  ================================================================  Constructs  =============
    public Frame() {
        super(DEFAULT_TITLE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setUpFrame();

        add(panel = new Panel(this));
    }

    public Frame(String title) {
        super(title);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setUpFrame();

        add(panel = new Panel(this));
    }

    public Frame(String title, int weight, int height) {
        super(title);
        setSize(weight, height);
        setUpFrame();

        add(panel = new Panel(this));
    }

    //  ===========================================================  Adding public methods  =======
    public void setUpFrame() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);
    }

    //  =======================================================================  Getters ==========
    public Panel getPanel() {
        return panel;
    }

}