package com.gmail.at.ivanehreshi.panels.workspace;

import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.customui.TableNodeView;
import com.gmail.at.ivanehreshi.models.NodeModel;
import com.gmail.at.ivanehreshi.models.TableNodeModel;

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

    public MindMapDrawer getMindMapDrawer() {
        return mindMapDrawer;
    }

    private void initDebugGui() {
        this.setLayout(new BorderLayout());
        NodeModel model = new NodeModel("Root", NodeModel.NodeSide.ROOT);

        mindMapDrawer = new MindMapDrawer(model);

        this.add(mindMapDrawer, BorderLayout.CENTER);
        mindMapDrawer.setVisible(true);
        mindMapDrawer.doLayout();
        NodeView root = mindMapDrawer.getRootNodeView();
        NodeView first = root.insertNewNode("1");
        NodeView second = root.insertNewNode("2");
        NodeView third = root.insertNewNode("3");
        NodeView a = first.insertNewNode("A");
        a.insertNewNode("dummy");
        NodeView b = first.insertNewNode("BHy ");
        NodeView c = first.insertNewNode("C<br/>d<br/>");
        NodeView d = first.insertNewNode("D");
//
        NodeView target1 = b.insertNewNode("target 1");
        NodeView target2 = b.insertNewNode("target 2");
        NodeView target3 = b.insertNewNode("target 3");

        NodeView o = c.insertNewNode("OOO");

        NodeView x = second.insertNewNode("X");
        NodeView y = second.insertNewNode("Y");
        NodeView z = second.insertNewNode("Z");
        TableNodeView zzz = (TableNodeView)second.insertExisting(new TableNodeModel(null, NodeModel.NodeSide.LEFT, 2, 2));
        z.getModel().setImagePath("/home/ivaneh/plus.png", true);

        trView = x;
//
        mindMapDrawer.doLayout();
//        a.getModel().swapDown();
        time = System.currentTimeMillis();
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                test();
            }
        });
        timer.start();
        timer.setRepeats(false);
    }

    public void test() {
        long delta = System.currentTimeMillis()-time;
        mindMapDrawer.getNodeEditor().edit(trView);
        time = System.currentTimeMillis();
    }

    private void initGui() {
    }
}
