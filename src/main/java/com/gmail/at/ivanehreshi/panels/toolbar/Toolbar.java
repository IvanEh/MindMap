package com.gmail.at.ivanehreshi.panels.toolbar;

import com.gmail.at.ivanehreshi.MindMapApplication;
import com.gmail.at.ivanehreshi.customui.TableNodeView;
import com.gmail.at.ivanehreshi.models.NodeModel;
import com.gmail.at.ivanehreshi.models.TableNodeModel;

import javax.swing.*;
import java.awt.*;

public class Toolbar extends JPanel{
    private JButton undoBtn;
    private JButton redoBtn;

    public Toolbar() {
        setBorder(BorderFactory.createLineBorder(new Color(0, 255, 0)));
        setLayout(new GridLayout(1, 16, 5, 5));

        createGui();
        setUpControllers();
    }

    private void setUpControllers() {
        undoBtn.addActionListener((e) -> MindMapApplication.getUndoManagerInstance().undo());
        redoBtn.addActionListener((e) -> MindMapApplication.getUndoManagerInstance().redo());
    }

    private void createGui() {
        undoBtn = new JButton("<-");
        add(undoBtn);

        redoBtn = new JButton("->");
        add(redoBtn);

    }
}
