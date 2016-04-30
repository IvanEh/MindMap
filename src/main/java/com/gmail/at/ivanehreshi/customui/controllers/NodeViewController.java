package com.gmail.at.ivanehreshi.customui.controllers;

import com.gmail.at.ivanehreshi.customui.NodeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    // TODO: only right button
    @Override
    public void mouseClicked(MouseEvent e) {
        NodeView view = getNodeView(e);

        if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
            view.insertNode("abcdefgs");
        } else {
            if((e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK) {
                view.setSelected(!view.isSelected(), true);
            } else {
                view.setSelected(!view.isSelected(), false);
                view.edit();
            }
        }
        if(SwingUtilities.isRightMouseButton(e)) {
            view.getMindMapController().onModelViewContextMenu(view, e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastPosition = null;
    }

    public void onEditAction(ActionEvent e) {
        JTextField field = (JTextField) e.getSource();
        NodeView view = (NodeView) field.getParent();

        view.finishEditing();
        view.getModel().setTitle(field.getText());
    }
}
