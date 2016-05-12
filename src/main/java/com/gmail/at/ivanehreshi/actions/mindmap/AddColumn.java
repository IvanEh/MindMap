package com.gmail.at.ivanehreshi.actions.mindmap;

import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.customui.TableNodeView;
import com.gmail.at.ivanehreshi.models.NodeModel;
import com.gmail.at.ivanehreshi.models.TableNodeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Contract
 *  source is the button or whatever but not the
 *  target itself
 */
public class AddColumn extends YAction{
    private final Side side;
    MindMapDrawer mindMapDrawer;

    public AddColumn(MindMapDrawer mindMapDrawer, Side side) {
        super("");
        this.side = side;
        if(side == Side.LEFT) {
            putValue(Action.NAME, "<-|");
        } else {
            putValue(Action.NAME, "|->");
        }

        this.mindMapDrawer = mindMapDrawer;
    }

    @Override
    public Command getCommand(ActionEvent event) {
        Component comp = mindMapDrawer.getFocusMonitor().getPrevFocused();
        if(comp == null) {
            return null;
        }

        if (comp instanceof JTable) {
            JTable table = (JTable) comp;
            TableNodeModel model = (TableNodeModel) table.getModel();
            TableNodeView view = (TableNodeView) table.getParent();
            final int before;
            if(side == Side.RIGHT) {
                before = view.getLastSelectedColumn() + 1;
            } else {
                before = view.getLastSelectedColumn();
            }

            if(before == -1)
                return null;

            return new UndoableCommand() {
                ArrayList<String> rowData = null;

                @Override
                public void undo() {
                    rowData = model.removeColumn(before);
                }

                @Override
                public void redo() {
                    model.insertColumn(before, rowData);
                }
            };
        }

        return null;
    }

    public enum Side {
        LEFT, RIGHT
    }
}
