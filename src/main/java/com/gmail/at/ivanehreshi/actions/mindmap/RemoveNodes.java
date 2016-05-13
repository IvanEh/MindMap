package com.gmail.at.ivanehreshi.actions.mindmap;

import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.models.NodeModel;

import java.awt.*;
import java.awt.event.ActionEvent;

public class RemoveNodes extends YAction{
    public RemoveNodes() {
        super(Strings.Popup.REMOVE_NODE);
    }

    @Override
    public Command getCommand(ActionEvent e) {
        NodeView view = (NodeView) e.getSource();
        Container parent = view.getParent();
        MindMapDrawer mindMapDrawer = (MindMapDrawer) view.getMindMapController();
        int index = view.getModel().index();

        return new UndoableCommand() {
            private NodeModel removed = null;

            NodeView createdView = null;
            NodeView target = (NodeView) e.getSource();

            @Override
            public void undo() {
                removed.getParent().addNode(index, removed, removed.getNodeSide());
                mindMapDrawer.addNode(removed);
                mindMapDrawer.validate();
            }

            @Override
            public void redo() {
                removed = view.remove();
            }
        };
    }
}
