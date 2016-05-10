package com.gmail.at.ivanehreshi.customui.controllers;

import com.gmail.at.ivanehreshi.MindMapApplication;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.mindmap.AddNode;
import com.gmail.at.ivanehreshi.actions.mindmap.EditNodeTitle;
import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class NodeViewController extends MouseAdapter{
    Point lastPosition = null;

    public NodeView getNodeView(MouseEvent e) {
        NodeView nodeView = (NodeView) e.getSource();
        return nodeView;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)) {
            return;
        }

        if(lastPosition == null) {
            lastPosition = e.getPoint();
        }

        int dx = e.getX() - lastPosition.x;
        int dy = e.getY() - lastPosition.y;

        getNodeView(e).translate(dx, dy);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        getMmd(getNodeView(e)).getPositionTracker().startTracking();
    }


    // TODO: only right button
    @Override
    public void mouseClicked(MouseEvent e) {
        NodeView view = getNodeView(e);

        if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
            view.insertNewNode("abcdefgs");
        }
        if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1){
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

        MindMapDrawer mmd = getMmd(getNodeView(e));
        mmd.getPositionTracker().stopTracking();

        if(!mmd.getPositionTracker().isEmpty()) {
            MindMapApplication.getUndoManagerInstance().push(
                    mmd.getPositionTracker().getUndoableCommand()
            );
        }

        mmd.getPositionTracker().clear();

    }

    private final EditNodeTitle editNodeTitleAction = new EditNodeTitle();
    public void onEditAction(ActionEvent e) {
        JTextField field = (JTextField) e.getSource();
        NodeView view = (NodeView) field.getParent();

        if(!view.getEditorText().equals(view.getModel().getTitle())) {
            editNodeTitleAction.actionPerformed(new ActionEvent(view, 0, ""));
        }
//        view.getModel().setTitle(field.getText());
    }

    protected MindMapDrawer getMmd(NodeView view) {
        return (MindMapDrawer) view.getMindMapController();
    }
}
