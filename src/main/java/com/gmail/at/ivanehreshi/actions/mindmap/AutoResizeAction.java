package com.gmail.at.ivanehreshi.actions.mindmap;

import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.sun.corba.se.impl.orbutil.graph.Node;

import java.awt.event.ActionEvent;

public class AutoResizeAction extends YAction {
    public AutoResizeAction() {
        super(Strings.Popup.AUTORESIZE);
    }

    // TODO: make correctly undoable
    @Override
    public Command getCommand(ActionEvent event) {
        return new UndoableCommand() {
            @Override
            public void undo() {

            }

            @Override
            public void redo() {
                NodeView nodeView = (NodeView) event.getSource();
                nodeView.getModel().updateModelPreferredSize(true);
                nodeView.revalidate();
            }
        };
    }
}
