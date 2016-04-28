package com.gmail.at.ivanehreshi.customui.controllers;

import com.gmail.at.ivanehreshi.customui.NodeView;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.dnd.DragSourceAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class NodeViewController extends MouseAdapter{
    Point lastPosition = null;

    public NodeView getNodeView(MouseEvent e) {
        NodeView nodeView = (NodeView) e.getSource();
        return nodeView;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(lastPosition == null) {
            lastPosition = e.getPoint();
        }

        int dx = e.getX() - lastPosition.x;
        int dy = e.getY() - lastPosition.y;

        getNodeView(e).translate(dx, dy);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() > 1 && SwingUtilities.isLeftMouseButton(e)) {
            getNodeView(e).insertNode("abcdefgs");
        }
    }
}
