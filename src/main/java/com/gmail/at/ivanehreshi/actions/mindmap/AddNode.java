package com.gmail.at.ivanehreshi.actions.mindmap;

import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.sun.xml.internal.bind.v2.TODO;

import java.awt.event.ActionEvent;

public class AddNode extends YAction{
    public AddNode() {
        super(Strings.Popup.ADD_NODE);
    }

    @Override
    public Command getCommand(ActionEvent e) {
        return new UndoableCommand() {
            NodeView createdView = null;
            NodeView target = (NodeView) e.getSource();
            MindMapDrawer mindMapDrawer = (MindMapDrawer) target.getMindMapController();

            @Override
            public void undo() {
                this.createdView.remove();
            }

            @Override
            public void redo() {
                this.createdView = target.insertNewNode("");
                mindMapDrawer.validate();
                mindMapDrawer.getNodeEditor().edit(createdView);
                // TODO: remove magic numbers
                mindMapDrawer.getNodeEditor().setSize(100, 50);
            }
        };
    }
}
