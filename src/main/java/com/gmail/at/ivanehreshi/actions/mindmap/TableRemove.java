package com.gmail.at.ivanehreshi.actions.mindmap;

import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.TableNodeView;
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
public class TableRemove extends YAction{
    private final What side;
    MindMapDrawer mindMapDrawer;

    public TableRemove(MindMapDrawer mindMapDrawer, What side) {
        super("");
        this.side = side;
        if(side == What.COLUMN) {
            putValue(Action.NAME, "|X|");
        } else {
            putValue(Action.NAME, "-X-");
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
            final int index;
            if(side == What.COLUMN) {
                index = view.getLastSelectedColumn() ;
            } else {
                index = view.getLastSelectedRow();
            }

            if(index == -1)
                return null;

            return new UndoableCommand() {
                ArrayList<String> data = null;

                @Override
                public void undo() {
                    if(side == What.COLUMN) {
                        model.insertColumn(index, data);
                    }  else {
                        model.insertRow(index, data);
                    }
                }

                @Override
                public void redo() {
                    if(side == What.COLUMN) {
                        data = model.removeColumn(index);
                    } else {
                        data = model.removeRow(index);
                    }
                }
            };
        }

        return null;
    }

    public enum What {
        COLUMN, ROW
    }
}
