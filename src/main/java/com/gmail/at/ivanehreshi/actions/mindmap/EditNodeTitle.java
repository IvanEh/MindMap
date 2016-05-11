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
    private final String before;
    private final String after;

    public EditNodeTitle(String before, String after) {
        super("Edit node");

        this.before = before;
        this.after = after;
    }

    @Override
    public Command getCommand(ActionEvent event) {
        final NodeView view = (NodeView) event.getSource();
        final ChangesTracker tracker =
                ((MindMapDrawer)view.getMindMapController()).getPositionTracker();

        return new UndoableCommand() {
            UndoableCommand positionChange = null;

            @Override
            public void undo() {
                view.getModel().setTitle(before);
                if(positionChange != null) {
                    positionChange.undo();
                }

                view.validate();
                view.repaint();
            }

            @Override
            public void redo() {

                if(positionChange == null) {
                    tracker.startTracking();
                }

                view.getModel().setTitle(after);

                if(positionChange == null) {
                    tracker.stopTracking();
                    positionChange = tracker.getUndoableCommand();
                    tracker.clear();
                } else {
                    positionChange.redo();
                }

                // TODO: replace with appropriate controller or lazy layout()
                view.getParent().doLayout();
            }
        };
    }
}
