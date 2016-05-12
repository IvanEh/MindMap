package com.gmail.at.ivanehreshi.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FunctionalMouseAdapter extends MouseAdapter{
    private static final GenericMouseListener PASS = e -> {};
    private GenericMouseListener mouseClickedHandler = PASS;
    private GenericMouseListener mousePressedHandler = PASS ;
    private GenericMouseListener mouseReleasedHandler = PASS;
    private GenericMouseListener mouseEnteredHandler = PASS;
    private GenericMouseListener mouseExitedHandler = PASS;
    private GenericMouseListener mouseDraggedHandler = PASS;
    private GenericMouseListener mouseMovedHandler = PASS;
    public FunctionalMouseAdapter() {
        super();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseClickedHandler.processMouseEvent(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressedHandler.processMouseEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseReleasedHandler.processMouseEvent(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseEnteredHandler.processMouseEvent(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseExitedHandler.processMouseEvent(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseDraggedHandler.processMouseEvent(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseMovedHandler.processMouseEvent(e);
    }

    public FunctionalMouseAdapter setMouseClickedHandler(GenericMouseListener mouseClickedHandler) {
        this.mouseClickedHandler = mouseClickedHandler;
        return this;
    }

    public FunctionalMouseAdapter setMousePressedHandler(GenericMouseListener mousePressedHandler) {
        this.mousePressedHandler = mousePressedHandler;
        return this;
    }

    public FunctionalMouseAdapter setMouseReleasedHandler(GenericMouseListener mouseReleasedHandler) {
        this.mouseReleasedHandler = mouseReleasedHandler;
        return this;
    }

    public FunctionalMouseAdapter setMouseEnteredHandler(GenericMouseListener mouseEnteredHandler) {
        this.mouseEnteredHandler = mouseEnteredHandler;
        return this;
    }

    public FunctionalMouseAdapter setMouseExitedHandler(GenericMouseListener mouseExitedHandler) {
        this.mouseExitedHandler = mouseExitedHandler;
        return this;
    }

    public FunctionalMouseAdapter setMouseDraggedHandler(GenericMouseListener mouseDraggedHandler) {
        this.mouseDraggedHandler = mouseDraggedHandler;
        return this;
    }

    public FunctionalMouseAdapter setMouseMovedHandler(GenericMouseListener mouseMovedHandler) {
        this.mouseMovedHandler = mouseMovedHandler;
        return this;
    }

    public interface GenericMouseListener {
        void processMouseEvent(MouseEvent e);
    }
}
