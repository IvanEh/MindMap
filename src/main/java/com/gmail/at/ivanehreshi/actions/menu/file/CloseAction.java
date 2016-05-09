package com.gmail.at.ivanehreshi.actions.menu.file;


import com.gmail.at.ivanehreshi.MindMapApplication;
import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.YAction;

import java.awt.event.ActionEvent;

public class CloseAction extends YAction{

    public CloseAction() {
        super(Strings.Menu.CLOSE);
    }

    @Override
    public Command getCommand(ActionEvent event) {
        return new Command() {
            @Override
            public void redo() {
                MindMapApplication.getInstance().dispose();
            }
        };
    }
}
