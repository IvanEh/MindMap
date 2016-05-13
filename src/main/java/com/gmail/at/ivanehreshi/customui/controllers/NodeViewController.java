package com.gmail.at.ivanehreshi.customui.controllers;

import com.gmail.at.ivanehreshi.MindMapApplication;
import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.NodeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NodeViewController extends MouseAdapter{
    Point lastPosition = null;
    boolean ignoreDragEvents = false;

    public NodeView getNodeView(MouseEvent e) {
        NodeView nodeView = (NodeView) e.getSource();
        return nodeView;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(ignoreDragEvents) {
            return;
        }

        if(SwingUtilities.isRightMouseButton(e)) {
            return;
        }

        onMove(e);
    }

    protected void onMove(MouseEvent e) {
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

        if(!getNodeView(e).activeArea().contains(e.getPoint())) {
            ignoreDragEvents = true;
            return;
        }
        ignoreDragEvents = false;

    }


    // TODO: only right button
    @Override
    public void mouseClicked(MouseEvent e) {
        if(ignoreDragEvents) {
            return;
        }

        NodeView view = getNodeView(e);
        view.requestFocus();

        if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
            onEdit(view);
        }
        if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1){
            onSelect(e, view);
        }
        if(SwingUtilities.isRightMouseButton(e)) {
            onContextMenu(e, view);
        }
    }

    protected void onContextMenu(MouseEvent e, NodeView view) {
        view.getMindMapController().onModelViewContextMenu(view, e.getX(), e.getY());
    }

    protected void onSelect(MouseEvent e, NodeView view) {
        if((e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK) {
            view.setSelected(!view.isSelected(), true);
        } else {
            view.setSelected(!view.isSelected(), false);
        }
    }

    protected void onEdit(NodeView view) {
        getMmd(view).getNodeEditor().edit(view);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastPosition = null;

        onCreateUndoCommand(e);

    }

    protected void onCreateUndoCommand(MouseEvent e) {
        MindMapDrawer mmd = getMmd(getNodeView(e));
        mmd.getPositionTracker().stopTracking();

        if(!mmd.getPositionTracker().isEmpty()) {
            MindMapApplication.getUndoManagerInstance().push(
                    mmd.getPositionTracker().getUndoableCommand()
            );
        }

        mmd.getPositionTracker().clear();
    }

    protected MindMapDrawer getMmd(NodeView view) {
        return (MindMapDrawer) view.getMindMapController();
    }
}
