package com.gmail.at.ivanehreshi.panels.workspace;

import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.NodeView;
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
        NodeModel model = new NodeModel("Root", NodeModel.NodeSide.RIGHT);

        mindMapDrawer = new MindMapDrawer(model);

        this.add(mindMapDrawer, BorderLayout.CENTER);
        mindMapDrawer.setVisible(true);
        mindMapDrawer.doLayout();
        NodeView root = mindMapDrawer.getRootNodeView();
        NodeView first = root.insertNode(" 1 ");
        NodeView second = root.insertNode(" 2 ");
        NodeView a = first.insertNode(" A ");
        a.insertNode("dummy");
        NodeView b = first.insertNode(" B ");
        NodeView c = first.insertNode(" C ");
        NodeView d = first.insertNode(" D ");
//
        NodeView target1 = b.insertNode(" target 1");
        NodeView target2 = b.insertNode("target 2");
        NodeView target3 = b.insertNode("target 3");

        NodeView o = c.insertNode("OOO");

        NodeView x = second.insertNode(" X ");
        NodeView y = second.insertNode(" Y ");
        NodeView z = second.insertNode(" Z ");

        trView = d;
//
        mindMapDrawer.doLayout();
        a.getModel().swapDown();
        time = System.currentTimeMillis();
        Timer timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                test();
            }
        });
//        timer.start();

    }

    public void test() {
        long delta = System.currentTimeMillis()-time;
        trView.translate(0, (int) 2);
        trView.getModel().swapUp();
        time = System.currentTimeMillis();
    }

    private void initGui() {
    }
}
