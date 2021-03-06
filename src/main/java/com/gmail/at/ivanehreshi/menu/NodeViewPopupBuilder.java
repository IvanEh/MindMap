package com.gmail.at.ivanehreshi.menu;

import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.YAction;
import com.gmail.at.ivanehreshi.actions.mindmap.*;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.models.NodeModel;
import com.gmail.at.ivanehreshi.utils.CopyCutBuffer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NodeViewPopupBuilder {

    private JPopupMenu popup = null;
    private CopyCutBuffer<NodeModel> copyCutBuffer = null;

    JPopupMenu buildPopup() {
        this.popup  = new JPopupMenu();
        return this.popup;
    }

    JMenuItem buildAddNode() {
        JMenuItem addNode = new JMenuItem(new AddNode() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                JPopupMenu popup = (JPopupMenu) source.getParent();
                NodeView view = (NodeView) popup.getInvoker();

                super.actionPerformed(new ActionEvent(view, ActionEvent.ACTION_FIRST, ""));
            }
        });

        popup.add(addNode);

        return addNode;
    }

    JMenuItem buildAddNodeWithImage() {
        JMenuItem addNode = new JMenuItem(new AddNodeWithImage() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                JPopupMenu popup = (JPopupMenu) source.getParent();
                NodeView view = (NodeView) popup.getInvoker();

                super.actionPerformed(new ActionEvent(view, ActionEvent.ACTION_FIRST, ""));
            }
        });

        popup.add(addNode);

        return addNode;
    }

    JMenuItem buildAddNodeWithTable() {
        JMenuItem addNode = new JMenuItem(new AddNodeWithTable() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                JPopupMenu popup = (JPopupMenu) source.getParent();
                NodeView view = (NodeView) popup.getInvoker();

                super.actionPerformed(new ActionEvent(view, ActionEvent.ACTION_FIRST, ""));
            }
        });
        popup.add(addNode);

        return addNode;
    }

    JMenuItem buildRemoveNode() {
        JMenuItem menu = new JMenuItem(new RemoveNodes() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NodeView view = unwrapView(e);
                super.actionPerformed(new ActionEvent(view, 0, ""));
            }
        });
        popup.add(menu);

        return menu;
    }

    public void buildCutCopyInsertNode() {
        JMenuItem cut = new JMenuItem(new CutNode(copyCutBuffer) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                JPopupMenu popup = (JPopupMenu) source.getParent();
                NodeView view = (NodeView) popup.getInvoker();

                super.actionPerformed(new ActionEvent(view, ActionEvent.ACTION_FIRST, ""));
            }
        });
        popup.add(cut);

        JMenuItem insert = new JMenuItem(new PasteNode(copyCutBuffer) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                JPopupMenu popup = (JPopupMenu) source.getParent();
                NodeView view = (NodeView) popup.getInvoker();

                if (copyCutBuffer != null && copyCutBuffer.hasElement()) {
                    super.actionPerformed(new ActionEvent(view, ActionEvent.ACTION_FIRST, ""));
                }
            }
        });
        popup.add(insert);

    }

    public CopyCutBuffer<NodeModel> buildCopyCutBuffer() {
        this.copyCutBuffer = new CopyCutBuffer<>();
        return this.copyCutBuffer;
    }

    public JMenuItem buildAutoResizeMenu() {
        JMenuItem autoResize = new JMenuItem(new AutoResizeAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                JPopupMenu popup = (JPopupMenu) source.getParent();
                NodeView view = (NodeView) popup.getInvoker();
                super.actionPerformed(new ActionEvent(view, 0, ""));
            }
        });
        popup.add(autoResize);

        return autoResize;
    }

    public JSeparator buildSeperator() {
        JSeparator separator = new JSeparator();
        popup.add(separator);
        return separator;
    }

    public void buildSwapMenus() {
        JMenuItem swapUp =  new JMenuItem(new Swap(Swap.Direction.UP) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                JPopupMenu popup = (JPopupMenu) source.getParent();
                NodeView view = (NodeView) popup.getInvoker();
                super.actionPerformed(new ActionEvent(view, 0, ""));
            }
        });
        popup.add(swapUp);

        JMenuItem swapDown = new JMenuItem(new Swap(Swap.Direction.DOWN) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                JPopupMenu popup = (JPopupMenu) source.getParent();
                NodeView view = (NodeView) popup.getInvoker();
                super.actionPerformed(new ActionEvent(view, 0, ""));
            }
        });
        popup.add(swapDown);
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
            builder.buildAddNodeWithImage();
            builder.buildAddNodeWithTable();
            builder.buildRemoveNode();
            builder.buildSeperator();
            builder.buildCopyCutBuffer();
            builder.buildCutCopyInsertNode();
            builder.buildSeperator();
            builder.buildAutoResizeMenu();
            builder.buildSeperator();
            builder.buildSwapMenus();
            return builder.getPopup();
        }
    }
}
