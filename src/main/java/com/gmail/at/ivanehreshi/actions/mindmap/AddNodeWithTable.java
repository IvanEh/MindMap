package com.gmail.at.ivanehreshi.actions.mindmap;

import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.dialogs.TableSizeDialog;
import com.gmail.at.ivanehreshi.models.NodeModel;
import com.gmail.at.ivanehreshi.models.TableNodeModel;
import com.gmail.at.ivanehreshi.utils.ImageFilter;
import com.gmail.at.ivanehreshi.utils.Resources;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AddNodeWithTable extends YAction {
    private String pathToImage;

    public AddNodeWithTable() {
        super(Strings.Popup.ADD_NODE_WITH_TABLE);
    }

    @Override
    public Command getCommand(ActionEvent e) {
        final TableSizeDialog.Inputs inputs = TableSizeDialog.showDialog(1, 1);
        final int columns = inputs.columnsSlider.getValue();
        final int rows = inputs.rowsSlider.getValue();

        return new UndoableCommand() {
            NodeView createdView = null;
            NodeView target = (NodeView) e.getSource();

            @Override
            public void undo() {
                this.createdView.remove();
            }

            @Override
            public void redo() {
                this.createdView =
                        target.insertNewNode(new TableNodeModel(null, NodeModel.NodeSide.LEFT, rows, columns));
                if(pathToImage != null) {
                    createdView.getModel().setImagePath(pathToImage, true);
                }
            }
        };
    }



}
