package com.gmail.at.ivanehreshi.customui;

import java.awt.*;

public class MindMapLayout implements LayoutManager {

    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(300, 400);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(100, 100);
    }

    @Override
    public void layoutContainer(Container parent) {
        MindMapDrawer mindMapDrawer = (MindMapDrawer) parent;
        Point origin = getOrigin(parent);
        for (Component comp: parent.getComponents()) {
            layoutComponent(mindMapDrawer, origin, comp);
        }
    }

    void layoutComponent(MindMapDrawer mindMapDrawer, Point origin, Component comp) {
        if (comp instanceof NodeView) {
            NodeView nodeView = (NodeView) comp;

            if(nodeView.getModel().isRootNode()) {
                nodeView.setLocation(origin); // FIXME: accuracy
                nodeView.setSize(nodeView.getMinimumSize());
                return;
            }

            Point anchor;
            NodeView prevView =
                    mindMapDrawer.getNodeViewByModel(nodeView.getModel().prevNode());

            if(nodeView.getModel().isRelative()) {
                anchor = new Point(prevView.getLocation());
                anchor.translate(0, prevView.getHeight());
            } else {
                NodeView parentView =
                        mindMapDrawer.getNodeViewByModel(nodeView.getModel().getParent());
                anchor = new Point(parentView.getLocation());
                anchor.translate(parentView.getWidth(), 0);
            }

            Point nodePos = nodeView.getModel().getNodePos();
            int x = (int) (anchor.getX() + nodePos.getX());
            int y = (int) (anchor.getY() +  nodePos.getY()) ;
            nodeView.setLocation(x, y);
            nodeView.setSize(nodeView.getMinimumSize());
        }
    }

    Point getOrigin(Container parent) {
        int x = parent.getWidth() / 2;
        int y = parent.getHeight() / 2;
        return new Point(x, y);
    }
}
