package com.gmail.at.ivanehreshi.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

abstract public class YAction extends AbstractAction {
    public YAction() {
    }

    protected YAction(String name) {
        super(name);
    }

    protected YAction(String name, Icon icon) {
        super(name, icon);
    }
}
