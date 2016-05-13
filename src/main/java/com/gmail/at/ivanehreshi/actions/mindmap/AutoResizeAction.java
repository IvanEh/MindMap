package com.gmail.at.ivanehreshi.actions.mindmap;

import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.sun.corba.se.impl.orbutil.graph.Node;
import com.sun.corba.se.impl.util.PackagePrefixChecker;

import java.awt.event.ActionEvent;

public class AutoResizeAction extends YAction {

    public AutoResizeAction() {
        super(Strings.Popup.AUTORESIZE);
    }

    // TODO: make correctly undoable
    @Override
    public Command getCommand(ActionEvent event) {
        final NodeView nodeView = (NodeView) event.getSource();
        final MindMapDrawer mindMapDrawer = (MindMapDrawer) nodeView.getMindMapController();

        if(nodeView.getModel().isAutoResizeEnabled()) {
            return null;
        }

        return new UndoableCommand() {
            UndoableCommand boundsChange = null;

            @Override
            public void undo() {
                nodeView.getModel().setAutoResize(false);
                if(boundsChange != null) {
                    boundsChange.undo();
                }

            }

            @Override
            public void redo() {
                if(boundsChange == null) {
                    mindMapDrawer.getPositionTracker().startTracking();
                }

                nodeView.getModel().updateModelPreferredSize(true);
                nodeView.getModel().setAutoResize(true);
                nodeView.getModel().fix();
                nodeView.revalidate();

                if(boundsChange == null) {
                    mindMapDrawer.getPositionTracker().stopTracking();
                    if(!mindMapDrawer.getPositionTracker().isEmpty()) {
                        boundsChange = mindMapDrawer.getPositionTracker().getUndoableCommand();
                    }
                }
            }
        };
    }
}
