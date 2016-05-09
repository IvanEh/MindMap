package com.gmail.at.ivanehreshi.actions.mindmap;

import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.models.NodeModel;

import java.awt.event.ActionEvent;

/**
 * Event contract: <br/>
 * ActionEvent source expected to be a NodeView
 */
public class EditNodeTitle extends YAction {
    public EditNodeTitle() {
        super("Edit node");
    }

    @Override
    public Command getCommand(ActionEvent event) {
        final NodeView view = (NodeView) event.getSource();
        return new UndoableCommand() {
            final String unchanged = view.getModel().getTitle();
            final String changed   = view.getEditorText();

            @Override
            public void undo() {
                view.getModel().setTitle(unchanged);
            }

            @Override
            public void redo() {
                view.getModel().setTitle(changed);
            }
        };
    }
}
