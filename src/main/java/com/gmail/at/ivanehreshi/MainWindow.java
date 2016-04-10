package com.gmail.at.ivanehreshi;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public static final String FRAME_TITLE = "Mind map";
    public static final Dimension WINDOW_SIZE = new Dimension(500, 600);

    public MainWindow() {
        super(FRAME_TITLE);
        // TODO: replace setSize with pack()
        this.setSize(WINDOW_SIZE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // center window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);
            }
        });
    }

}
