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

public class PasteNode extends YAction{
    private final CopyCutBuffer<NodeModel> copyCutBuffer;

    public PasteNode(CopyCutBuffer<NodeModel> copyCutBuffer) {
        super(Strings.Popup.INSERT_NODE);
        this.copyCutBuffer = copyCutBuffer;
    }

    @Override
    public Command getCommand(ActionEvent e) {
        NodeView view = (NodeView) e.getSource();
        Container parent = view.getParent();
        MindMapDrawer mindMapDrawer = (MindMapDrawer) view.getMindMapController();
        int index = view.getModel().index();

        return new UndoableCommand() {
            private NodeView inserted = null;
            private UndoableCommand boundsCommand = null;

            @Override
            public void undo() {
                // Choose appropriate method
                copyCutBuffer.cut(inserted.remove());
                if(boundsCommand != null) {
                    boundsCommand.undo();
                }
            }

            @Override
            public void redo() {
                mindMapDrawer.getPositionTracker().startTracking();

                NodeModel nodeModel = copyCutBuffer.pop();
                inserted = view.insertExisting(nodeModel);

                mindMapDrawer.getPositionTracker().stopTracking();
                if(!mindMapDrawer.getPositionTracker().isEmpty()) {
                    boundsCommand = mindMapDrawer.getPositionTracker().getUndoableCommand();
                }
                mindMapDrawer.getPositionTracker().clear();
            }
        };
    }
}
