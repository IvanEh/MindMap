package com.gmail.at.ivanehreshi.menu;

import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.models.NodeModel;
import com.gmail.at.ivanehreshi.utils.CopyCutBuffer;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NodeViewPopupBuilder {

    private JPopupMenu popup = null;
    private CopyCutBuffer<NodeModel> copyCutBuffer = null;

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
                view.insertNewNode("d");
            }
        });

        popup.add(addNode);

        return addNode;
    }

    JMenuItem buildRemoveNode() {
        JMenuItem menu = new JMenuItem(new YAction(Strings.Popup.REMOVE_NODE) {
            @Override
            public void actionPerformed(ActionEvent e) {
                NodeView view = unwrapView(e);
                view.remove();
            }
        });

        popup.add(menu);

        return menu;
    }

    public void buildCutCopyInsertNode() {
        JMenuItem cut = new JMenuItem(new YAction(Strings.Popup.CUT_NODE) {

            @Override
            public void actionPerformed(ActionEvent e) {
                NodeView view = unwrapView(e);
                NodeModel model = view.remove();
                if(copyCutBuffer != null) {
                    copyCutBuffer.cut(model);
                }
            }
        });
        popup.add(cut);

        JMenuItem insert = new JMenuItem(new YAction(Strings.Popup.INSERT_NODE) {
            @Override
            public void actionPerformed(ActionEvent e) {
                NodeView view = unwrapView(e);
                if (copyCutBuffer != null && copyCutBuffer.hasElement()) {
                    NodeModel modelToInsert = copyCutBuffer.pop();
                    view.insertExisting(modelToInsert);
                }
            }
        });
        popup.add(insert);

    }

    public CopyCutBuffer<NodeModel> buildCopyCutBuffer() {
        this.copyCutBuffer = new CopyCutBuffer<>();
        return this.copyCutBuffer;
    }

    NodeView unwrapView(ActionEvent e) {
        JMenuItem source = (JMenuItem) e.getSource();
        JPopupMenu popup = (JPopupMenu) source.getParent();
        NodeView view = (NodeView) popup.getInvoker();
        return view;
    }

    JPopupMenu getPopup() {
        return popup;
    }

    public static class Director {
        public JPopupMenu build() {
            NodeViewPopupBuilder builder = new NodeViewPopupBuilder();
            builder.buildPopup();
            builder.buildAddNode();
            builder.buildRemoveNode();
            builder.buildCopyCutBuffer();
            builder.buildCutCopyInsertNode();
            return builder.getPopup();
        }
    }
}
