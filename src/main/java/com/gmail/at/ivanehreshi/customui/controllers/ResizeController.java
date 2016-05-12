package com.gmail.at.ivanehreshi.customui.controllers;

import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.xml.soap.Node;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ResizeController extends MouseAdapter {
    private final int activeFieldSize;
    State state = null;
    private Point lastPosition = null;

    public ResizeController(int activeFieldSize) {
        this.activeFieldSize = activeFieldSize;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        state = calcState(e.getPoint(), (Component) e.getSource());
        lastPosition = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = (int) (e.getX() - lastPosition.getX());
        int dy = (int) (e.getY() - lastPosition.getY());
        lastPosition = e.getPoint();
        Component comp = e.getComponent();

        if(state == State.SOUTH_EAST) {
            stretch(comp, dx, dy);
        }
    }

    private void stretch(Component comp, int dw, int dh) {
        int newWidth = comp.getWidth() + dw;
        int newHeight = comp.getHeight() + dh;

        comp.setSize(newWidth, newHeight);
        if(comp instanceof NodeView) {
            ((NodeView) comp).getMindMapController().onViewChangeSize((NodeView) comp, dw, dh);
            ((NodeView) comp).getModel().fix0(NodeModel.NodeSide.LEFT);
            ((NodeView) comp).getModel().fix0(NodeModel.NodeSide.RIGHT);
            comp.revalidate();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        state = null;
        lastPosition = null;
    }


    public State calcState(Point p, Component comp) {
        Rectangle southEast = new Rectangle(comp.getWidth() - activeFieldSize, comp.getHeight() - activeFieldSize,
                activeFieldSize, activeFieldSize);
        if(southEast.contains(p)) {
            return State.SOUTH_EAST;
        }
        return null;
    }

    public int getActiveFieldSize() {
        return activeFieldSize;
    }

    public State getState() {
        return state;
    }

    public enum State {
        NORTH_EAST,
        NORTH_WEST,
        SOUTH_EAST,
        SOUTH_WEST
    }
}
