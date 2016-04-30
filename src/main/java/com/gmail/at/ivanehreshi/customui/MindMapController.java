package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.event.ChangeListener;

/**
 * Created by ivaneh on 28.04.16.
 */
public interface MindMapController extends ChangeListener {
    void onViewTranslate(NodeView view, int dx, int dy);

    NodeView onNodeModelInsert(NodeView view, NodeModel model);

    NodeView getFocusedNode();

    /**
     * Trigers when somebody want to focus on a node
     * @param view is a node that need to be focused
     * @return true if the node can be focused, false - otherwise
     */
    boolean onNodeGainFocus(NodeView view);

    /**
     * Triggered when somebody want to focus remove focus from the node
     * Typically focus removal done by MindMapController
     * @param view is a node that need to be focused
     * @return true if the node can be focused, false - otherwise
     */
    boolean onNodeLostFocus(NodeView view);
}
