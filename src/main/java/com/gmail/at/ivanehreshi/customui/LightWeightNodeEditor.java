package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.actions.mindmap.EditNodeTitle;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class LightWeightNodeEditor extends JTextArea{
    private NodeView target;

    private static final String BR = "<br/>";
    private static final String NEW_LINE = "\n";

    public LightWeightNodeEditor(NodeView target) {
        super();
        this.target = target;
        // TODO: remove magic numbers
        setBorder(BorderFactory.createLineBorder(new Color(215, 223, 223, 255).darker()));
        setOpaque(true);
        setBackground(new Color(215, 223, 223, 220));
        setUpControllers();
    }

    private void setUpControllers() {
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { }
            @Override
            public void focusLost(FocusEvent e) {
                finishEditing();
            }
        });
    }

    public NodeView getTarget() {
        return target;
    }

    public void setTarget(NodeView target) {
        this.target = target;

        if(target == null)
            return;

        this.setText(target.getModel().getTitle().replace(BR, NEW_LINE));
        Rectangle activeArea = getTarget().activeArea();
        int border = getTarget().getModel().computeTotalInset();

        setLocation(getTarget().getX() + activeArea.x + border,
                    getTarget().getY() + activeArea.y + border);
        setSize(new Dimension(activeArea.width - 2*border,
                                     activeArea.height - 2*border));
//        setSize(getMinimumSize());
//        setSize(getPreferredSize());
    }

    public void edit(NodeView target) {;
        setTarget(target);
        setVisible(true);
        requestFocus();
        target.setState(NodeView.State.EDIT);
    }

    public void finishEditing() {
        String before = getTarget().getModel().getTitle();
        String after = getText().replace(NEW_LINE, BR);

        if(!before.equals(after)) {
            EditNodeTitle editNodeTitle = new EditNodeTitle(before, after);
            editNodeTitle.actionPerformed(new ActionEvent(getTarget(), 0, ""));
        }

        target.setState(NodeView.State.STATIC);
        setTarget(null);
        setVisible(false);
    }

}
