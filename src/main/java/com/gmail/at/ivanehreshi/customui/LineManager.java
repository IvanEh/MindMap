package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.*;
import java.awt.*;

public class LineManager {
    MindMapDrawer mindMapDrawer;
    MindMapLayout mindMapLayout;

    public LineManager(MindMapDrawer mindMapDrawer) {
        this.mindMapDrawer = mindMapDrawer;
        this.mindMapLayout = mindMapDrawer.getMindMapLayout();
    }

    public void drawLine(Graphics2D g2d, Point origin, NodeModel modelFrom, NodeModel modelTo) {
        Point start = new Point();
        Point end = new Point();

        if(modelTo.isRight()) {
            start.x = origin.x + modelFrom.getX() + modelFrom.getWidth() - NodeView.BORDER_THICKNESS;
            start.y = origin.y + modelFrom.getY() + modelFrom.getHeight() / 2;
            end.x = origin.x + modelTo.getX() + NodeView.BORDER_THICKNESS;;
            end.y = origin.y + modelTo.getY() + modelTo.getHeight()/2;
        } else {
            start.x = origin.x + modelFrom.getX() + NodeView.BORDER_THICKNESS;;
            start.y = origin.y + modelFrom.getY() + modelFrom.getHeight()/2;
            end.x = origin.x + modelTo.getX() + modelTo.getWidth() - NodeView.BORDER_THICKNESS;;
            end.y = origin.y + modelTo.getY() + modelTo.getHeight() / 2;
        }


        g2d.drawLine(start.x, start.y, end.x, end.y);
    }

    public void drawLines(Graphics2D g2d) {
        Point origin = mindMapLayout.getOrigin(mindMapDrawer);

        mindMapDrawer.getModelToViewMap().keySet().forEach(nodeModel -> {
            nodeModel.getNodes(NodeModel.NodeSide.LEFT).forEach(to -> {
                drawLine(g2d, origin, nodeModel, to);
            });
            nodeModel.getNodes(NodeModel.NodeSide.RIGHT).forEach(to -> {
                drawLine(g2d, origin, nodeModel, to);
            });

        });
    }
}
