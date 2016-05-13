package com.gmail.at.ivanehreshi.actions.mindmap;

import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.customui.controllers.ChangesTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Event contract: <br/>
 * ActionEvent source expected to be a NodeView
 */
public class Swap extends YAction {
    private final Direction direction;

    public Swap(Direction direction) {
        super("");

        this.direction = direction;
        if(direction == Direction.UP) {
            putValue(Action.NAME, Strings.Popup.SWAP_UP);
        } else {
            putValue(Action.NAME, Strings.Popup.SWAP_DOWN);
        }
    }

    @Override
    public Command getCommand(ActionEvent event) {
        NodeView view = (NodeView) event.getSource();
        MindMapDrawer mindMapDrawer = (MindMapDrawer) view.getMindMapController();
        ArrayList<NodeView> capturedSelection = new ArrayList<>(mindMapDrawer.getSelection());

        return new UndoableCommand() {
            UndoableCommand positionChange = null;

            @Override
            public void undo() {
                if(direction == Direction.UP) {
                    capturedSelection.forEach(view -> view.getModel().swapDown());
                }else {
                    capturedSelection.forEach(view -> view.getModel().swapUp());
                }
                if(positionChange != null) {
                    positionChange.undo();
                }
                mindMapDrawer.revalidate();
            }

            @Override
            public void redo() {
                mindMapDrawer.getPositionTracker().startTracking();

                if(direction == Direction.UP) {
                    capturedSelection.forEach(view -> view.getModel().swapUp());
                }else {
                    capturedSelection.forEach(view -> view.getModel().swapDown());
                }
                mindMapDrawer.revalidate();

                mindMapDrawer.getPositionTracker().stopTracking();
                if(mindMapDrawer.getPositionTracker().isEmpty()) {
                    positionChange = mindMapDrawer.getPositionTracker().getUndoableCommand();
                }
                mindMapDrawer.getPositionTracker().clear();
            }
        };
    }


    public enum Direction {
        UP,
        DOWN
    }
}
