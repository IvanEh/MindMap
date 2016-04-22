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
        NodeModel model = new NodeModel("Root");

        //model1.setPolarCoord(120, Math.toRadians(0));
//        model2.setPolarCoord(120, Math.toRadians(45));
//        model3.setPolarCoord(120, Math.toRadians(45));



        mindMapDrawer = new MindMapDrawer(model);

        this.add(mindMapDrawer, BorderLayout.CENTER);
        mindMapDrawer.setVisible(true);
        mindMapDrawer.doLayout();
        NodeView root = mindMapDrawer.getRootNodeView();
        NodeView first = root.insertNode(" 1 ");
        NodeView second = root.insertNode(" 2 ");
        NodeView a = first.insertNode(" A ");
        NodeView b = first.insertNode(" B ");
        NodeView c = first.insertNode(" C ");

        NodeView o = c.insertNode("OOO");

        NodeView x = second.insertNode(" X ");
        NodeView y = second.insertNode(" Y ");
        NodeView z = second.insertNode(" Z ");

        trView = b;

        mindMapDrawer.doLayout();
        time = System.currentTimeMillis();
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                test();
            }
        });
        timer.start();

    }

    public void test() {
        long delta = System.currentTimeMillis()-time;
        trView.translate(0, (int) -2);
        time = System.currentTimeMillis();
    }

    private void initGui() {
    }
}
