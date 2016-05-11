package com.gmail.at.ivanehreshi.panels.workspace;

import com.gmail.at.ivanehreshi.MindMapApplication;
import com.gmail.at.ivanehreshi.actions.mindmap.AddNode;
import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.customui.controllers.MindMapMoveController;
import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Workspace extends JPanel {
    private MindMapDrawer mindMapDrawer;
    private NodeView trView;
    private long time;

    public Workspace() {
        this.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255)));
        initGui();
        initDebugGui();
    }

    private void initDebugGui() {
        this.setLayout(new BorderLayout());
        NodeModel model = new NodeModel("Root", NodeModel.NodeSide.ROOT);

        mindMapDrawer = new MindMapDrawer(model);

        this.add(mindMapDrawer, BorderLayout.CENTER);
        mindMapDrawer.setVisible(true);
        mindMapDrawer.doLayout();
        NodeView root = mindMapDrawer.getRootNodeView();
        NodeView first = root.insertNewNode(" 1 ");
        NodeView second = root.insertNewNode(" 2 ");
        NodeView third = root.insertNewNode(" 3 ");
        NodeView a = first.insertNewNode(" A ");
        a.insertNewNode("dummy");
        NodeView b = first.insertNewNode(" B\nHy ");
        NodeView c = first.insertNewNode(" C ");
        NodeView d = first.insertNewNode(" D ");
//
        NodeView target1 = b.insertNewNode(" target 1");
        NodeView target2 = b.insertNewNode("target 2");
        NodeView target3 = b.insertNewNode("target 3");

        NodeView o = c.insertNewNode("OOO");

        NodeView x = second.insertNewNode(" X ");
        NodeView y = second.insertNewNode(" Y ");
        NodeView z = second.insertNewNode(" Z ");
        z.getModel().setImagePath("/home/ivaneh/plus.png");

        trView = d;
//
        mindMapDrawer.doLayout();
//        a.getModel().swapDown();
        time = System.currentTimeMillis();
        Timer timer = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                test();
            }
        });
        timer.start();

    }

    public void test() {
        long delta = System.currentTimeMillis()-time;
//        MindMapApplication.getUndoManagerInstance().undo();
        time = System.currentTimeMillis();
    }

    private void initGui() {
    }
}
