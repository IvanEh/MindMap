package com.gmail.at.ivanehreshi.actions.mindmap;

import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.models.NodeModel;
import com.gmail.at.ivanehreshi.utils.CopyCutBuffer;

import java.awt.*;
import java.awt.event.ActionEvent;

public class CutNode extends YAction{
    private final CopyCutBuffer<NodeModel> copyCutBuffer;

    public CutNode(CopyCutBuffer<NodeModel> copyCutBuffer) {
        super(Strings.Popup.CUT_NODE);
        this.copyCutBuffer = copyCutBuffer;
    }

    @Override
    public Command getCommand(ActionEvent e) {
        NodeView view = (NodeView) e.getSource();
        Container parent = view.getParent();
        MindMapDrawer mindMapDrawer = (MindMapDrawer) view.getMindMapController();
        int index = view.getModel().index();

        return new UndoableCommand() {
            private NodeModel removed = null;
            private NodeModel parent = null;

            @Override
            public void undo() {
                copyCutBuffer.pop();
                parent.addNode(index, removed, removed.getNodeSide());
                mindMapDrawer.addNode(removed);
                mindMapDrawer.validate();
            }

            @Override
            public void redo() {
                removed = view.remove();
                parent = removed.getParent();
                copyCutBuffer.cut(removed);
            }
        };
    }
}
