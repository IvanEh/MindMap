package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.customui.controllers.MindMapController;

import java.awt.*;

public class MindMapLayout implements LayoutManager {
    private int renderX = 0;
    private int renderY = 0;

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
        MindMapController mindMapController = (MindMapController) parent;
        Point origin = getOrigin(parent);
        for (Component comp: parent.getComponents()) {
            layoutComponent(mindMapController, origin, comp);
        }
    }

    void layoutComponent(MindMapController mindMapController, Point origin, Component comp) {
        if (comp instanceof NodeView) {
            NodeView nodeView = (NodeView) comp;

            Point nodePos = nodeView.getModel().getNodePos();
            int x = (int) (origin.getX() + nodePos.getX() );
            int y = (int) (origin.getY() +  nodePos.getY()) ;
            nodeView.setLocation(x, y);
            nodeView.setSize(nodeView.getMinimumSize());
        }
    }

    public void translate(int dx, int dy) {
        renderX += dx;
        renderY += dy;
    }

    Point getOrigin(Container parent) {
        int x = parent.getWidth() / 2 + renderX;
        int y = parent.getHeight() / 2 + renderY;
        return new Point(x, y);
    }
}
