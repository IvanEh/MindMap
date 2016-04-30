package com.gmail.at.ivanehreshi.menu;

import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.NodeView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NodeViewPopupBuilder {

    private JPopupMenu popup;

    JPopupMenu buildPopup() {
        this.popup  = new JPopupMenu();
        return this.popup;
    }

    JMenuItem buildAddNode() {
        JMenuItem addNode = new JMenuItem(new YAction(Strings.Popup.ADD_NODE) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                JPopupMenu popup = (JPopupMenu) source.getParent();
                NodeView view = (NodeView) popup.getInvoker();
                view.insertNode("d");
            }
        });

        popup.add(addNode);

        return addNode;
    }

    JPopupMenu getPopup() {
        return popup;
    }

    public static class Director {
        public JPopupMenu build() {
            NodeViewPopupBuilder builder = new NodeViewPopupBuilder();
            builder.buildPopup();
            builder.buildAddNode();
            return builder.getPopup();
        }
    }
}