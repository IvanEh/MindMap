package com.gmail.at.ivanehreshi.actions.mindmap;

import com.gmail.at.ivanehreshi.MindMapApplication;
import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.utils.ImageFilter;
import com.gmail.at.ivanehreshi.utils.Resources;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AddNodeWithImage extends YAction {
    private String pathToImage;

    public AddNodeWithImage() {
        super(Strings.Popup.ADD_NODE_WITH_IMAGE);
    }

    private void chooseImage() {
        // TODO: singleton factory
        JFileChooser fileChooser
                = new JFileChooser(Resources.getInstance().workingDir());
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new ImageFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        int action = fileChooser.showDialog(null, "Вибрати");
        switch (action) {
            case JFileChooser.APPROVE_OPTION:
                pathToImage = fileChooser.getSelectedFile().getPath();
                break;
            default:
                pathToImage = null;
        }
    }

    @Override
    public Command getCommand(ActionEvent e) {
        chooseImage();

        return new UndoableCommand() {
            NodeView createdView = null;
            NodeView target = (NodeView) e.getSource();

            @Override
            public void undo() {
                this.createdView.remove();
            }

            @Override
            public void redo() {
                this.createdView = target.insertNewNode("");
                if(pathToImage != null) {
                    createdView.getModel().setImagePath(pathToImage, true);
                    createdView.getModel().fix();
                }
            }
        };
    }

}
