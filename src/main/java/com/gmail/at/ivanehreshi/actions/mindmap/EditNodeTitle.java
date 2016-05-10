package com.gmail.at.ivanehreshi.actions.mindmap;

import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.customui.controllers.ChangesTracker;
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
        final ChangesTracker tracker =
                ((MindMapDrawer)view.getMindMapController()).getPositionTracker();

        return new UndoableCommand() {
            final String unchanged = view.getModel().getTitle();
            final String changed   = view.getEditorText();
            UndoableCommand positionChange = null;

            @Override
            public void undo() {
                view.getModel().setTitle(unchanged);
                if(positionChange != null) {
                    positionChange.undo();
                }
            }

            @Override
            public void redo() {

                if(positionChange == null) {
                    tracker.startTracking();
                }

                view.getModel().setTitle(changed);

                if(positionChange == null) {
                    tracker.stopTracking();
                    positionChange = tracker.getUndoableCommand();
                    tracker.clear();
                } else {
                    positionChange.redo();
                }
            }
        };
    }
}
