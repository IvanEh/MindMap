package com.gmail.at.ivanehreshi.customui.controllers;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FocusMonitor implements FocusListener {
    private Container focused = null;
    private Container prevFocused = null;

    public boolean haveFocused() {
        return focused != null;
    }

    public Container getFocused() {
        return focused;
    }

    public Container getPrevFocused() {
        return prevFocused;
    }

    public void monitor(Container container) {
        container.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        focused = (Container) e.getSource();
        System.out.println(focused);
    }

    @Override
    public void focusLost(FocusEvent e) {
        prevFocused = focused;
        focused = null;
    }
}
