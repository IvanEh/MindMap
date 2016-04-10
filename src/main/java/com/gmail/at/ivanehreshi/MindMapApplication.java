package com.gmail.at.ivanehreshi;

import com.gmail.at.ivanehreshi.menu.MenuBarBuilder;

import javax.swing.*;
import java.awt.*;

public class MindMapApplication extends JFrame {

    private static MindMapApplication instance;

    public static final String FRAME_TITLE = "Mind map";
    public static final Dimension WINDOW_SIZE = new Dimension(500, 600);

    public JMenuBar menuBar;

    private MindMapApplication() {
        super(FRAME_TITLE);
        // TODO: replace setSize with pack()
        this.setSize(WINDOW_SIZE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // center window

        initGui();
    }

    private void initGui() {
        menuBar = new MenuBarBuilder().build();
        this.setJMenuBar(menuBar);
        menuBar.setVisible(true); // for clarification

    }

    public static MindMapApplication getInstance() {
        if(instance == null) {
            instance = new MindMapApplication();
        }
        
        return instance;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MindMapApplication.getInstance().setVisible(true);
            }
        });
    }

}
