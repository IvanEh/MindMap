package com.gmail.at.ivanehreshi;

import com.gmail.at.ivanehreshi.menu.MenuBarBuilder;
import com.gmail.at.ivanehreshi.panels.RootLayoutPanel;
import com.gmail.at.ivanehreshi.utils.Resources;
import com.gmail.at.ivanehreshi.utils.UndoManager;

import javax.swing.*;
import java.awt.*;

public class MindMapApplication extends JFrame {

    private static final int UNDO_HISTORY_SIZE = 10;
    private static MindMapApplication instance;

    public static final String FRAME_TITLE = "Mind map";
    public static final Dimension WINDOW_SIZE = new Dimension(500, 600);
    private static UndoManager undoManagerInstance;

    public JMenuBar menuBar;
    public JPanel rootLayoutPanel;
    private Resources resources;

    private MindMapApplication() {
        super(FRAME_TITLE);

        this.resources = new Resources();

        // TODO: replace setSize with pack()
        this.setSize(WINDOW_SIZE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // center window

        initGui();
    }

    public Resources getResources() {
        return resources;
    }

    public static UndoManager getUndoManagerInstance() {
        if(undoManagerInstance == null) {
            undoManagerInstance = new UndoManager(UNDO_HISTORY_SIZE);
        }
        return undoManagerInstance;
    }

    private void initGui() {
        menuBar = new MenuBarBuilder().build();
        this.setJMenuBar(menuBar);
        menuBar.setVisible(true); // for clarification

        getContentPane().setLayout(new BorderLayout());

        rootLayoutPanel = new RootLayoutPanel();
        getContentPane().add(rootLayoutPanel);

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
