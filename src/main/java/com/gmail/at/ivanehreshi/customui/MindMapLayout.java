package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.utils.Vector2D;

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
            if (comp instanceof NodeView) {
                NodeView nodeView = (NodeView) comp;

                if(nodeView.getModel().isRootNode()) {
                    nodeView.setLocation(origin); // FIXME: accuracy
                    nodeView.setSize(nodeView.getMinimumSize());
                    continue;
                }

                Point coordSystem;
                coordSystem = mindMapDrawer.getParent(nodeView).getLocation();
                if(nodeView.getModel().getIndex() == 0) {
                    Point relPosition = nodeView.getModel()
                            .computePosition(parent.getWidth(), parent.getHeight());
                    Point position = new Point();
                    position.x = coordSystem.x + relPosition.x;
                    position.y = coordSystem.y + relPosition.y;
                    nodeView.setLocation(position);
                    nodeView.setSize(nodeView.getMinimumSize());
                }else {
                    NodeView prevNode =
                            mindMapDrawer.getNodeViewByModel(nodeView.getModel().getPrev());
                    Point anchorPoint = prevNode.getLocation();
                    double phi = nodeView.getModel().getPhi();
                    double magnitude = nodeView.getModel().computeMagnitude(parent.getWidth());
                    Vector2D vec = new Vector2D(anchorPoint.x - origin.x, anchorPoint.y - origin.y);
                    vec.invertAxis(Vector2D.Axis.Y);
                    vec.rotate(-phi);
                    vec.norm();
                    vec.mult(magnitude);
                    vec.invertAxis(Vector2D.Axis.Y);
                    vec.move(origin.getX(), origin.getY());

                    nodeView.setLocation(new Point((int)vec.getX(), (int) vec.getY()));
                    nodeView.setSize(nodeView.getMinimumSize());
                }

            }
        }
    }

    private Point getOrigin(Container parent) {
        int x = parent.getWidth() / 2;
        int y = parent.getHeight() / 2;
        return new Point(x, y);
    }
}
