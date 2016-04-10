package com.gmail.at.ivanehreshi.actions.menu.file;


import com.gmail.at.ivanehreshi.MindMapApplication;
import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.YAction;

import java.awt.event.ActionEvent;

public class CloseAction extends YAction{

    public CloseAction() {
        super(Strings.Menu.CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MindMapApplication.getInstance().dispose();
    }
}
