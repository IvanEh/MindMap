package com.gmail.at.ivanehreshi.actions.mindmap;

import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.NodeView;

import java.awt.event.ActionEvent;

public class AddNode extends YAction{
    private static final String TITLE = "TITLE";

    public AddNode() {
        super(Strings.Popup.ADD_NODE);
    }

    @Override
    public Command getCommand(ActionEvent e) {
        return new UndoableCommand() {
            NodeView createdView = null;
            NodeView target = (NodeView) e.getSource();

            @Override
            public void undo() {
                this.createdView.remove();
            }

            @Override
            public void redo() {
                this.createdView = target.insertNewNode("");
            }
        };
    }
}
