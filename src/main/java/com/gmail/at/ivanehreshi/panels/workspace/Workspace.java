package com.gmail.at.ivanehreshi.panels.workspace;

import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class Workspace extends JPanel {
    public Workspace() {
        this.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255)));
        initGui();
        initDebugGui();
    }

    private void initDebugGui() {
        this.setLayout(new BorderLayout());
        NodeModel model = new NodeModel("Root");
        NodeModel model1 = new NodeModel("Node1", model);
        NodeModel model2 = new NodeModel("Node2", model);
        NodeModel model3 = new NodeModel("Node3", model);
        NodeModel model3_1 = new NodeModel("Node3_1", model3);
        model3_1.setPolarCoord(30, Math.toRadians(30));

        model1.setPolarCoord(120, Math.toRadians(0));
        model2.setPolarCoord(120, Math.toRadians(45));
        model3.setPolarCoord(120, Math.toRadians(45));


        model.addNode(model1);
        model.addNode(model2);
        model.addNode(model3);
        model3.addNode(model3_1);
        MindMapDrawer mindMapDrawer = new MindMapDrawer(model);

        mindMapDrawer.setVisible(true);
        this.add(mindMapDrawer, BorderLayout.CENTER);

    }

    private void initGui() {
    }
}
