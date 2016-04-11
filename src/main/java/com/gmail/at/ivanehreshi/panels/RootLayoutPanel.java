package com.gmail.at.ivanehreshi.panels;

import com.gmail.at.ivanehreshi.panels.toolbar.Toolbar;
import com.gmail.at.ivanehreshi.panels.workspace.Workspace;

import javax.swing.*;
import java.awt.*;

public class RootLayoutPanel extends JPanel {
    private final static int HGAP = 5;
    private final static int VGAP = 5;

    private JPanel toolbar;
    private JPanel workspace;
    private JSplitPane splitPane;

    public RootLayoutPanel() {
        this.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0)));
        initLayout();
        initGui();
    }

    private void initLayout() {
        setLayout(new BorderLayout(HGAP, VGAP));
    }

    private void initGui() {
        toolbar = new Toolbar();
        this.add(toolbar, BorderLayout.NORTH);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new Workspace(), new Workspace());
        this.add(splitPane);

//        workspace = new Workspace();
//        this.add(workspace);
    }
}
