package com.gmail.at.ivanehreshi.customui.controllers;

import com.gmail.at.ivanehreshi.customui.MindMapDrawer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MindMapMoveController extends MouseAdapter{
    MindMapController controller;
    Point lastPosition = null;

    public MindMapMoveController(MindMapController controller) {
        this.controller = controller;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastPosition = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(lastPosition == null) {
            lastPosition = e.getPoint();
        }

        int dx = e.getX() - lastPosition.x;
        int dy = e.getY() - lastPosition.y;

        lastPosition = e.getPoint();

        controller.onMindMapTranslate(dx, dy);
    }
}
