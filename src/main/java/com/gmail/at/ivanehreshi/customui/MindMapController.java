package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.event.ChangeListener;
import java.util.ArrayList;

/**
 * Created by ivaneh on 28.04.16.
 */
public interface MindMapController extends ChangeListener {
    void onViewTranslate(NodeView view, int dx, int dy);

    NodeView onNodeModelInsert(NodeView view, NodeModel model);

    ArrayList<NodeView> getSelection();

    /**
     * Trigers when somebody want to focus on a node
     * @param view is a node that need to be focused
     * @param add
     * @return true if the node can be focused, false - otherwise
     */
    boolean onNodeSelect(NodeView view, boolean add);


    /**
     * Triggered when somebody want to focus remove focus from the node
     * Typically focus removal done by MindMapController
     * @param view is a node that need to be focused
     * @param add
     * @return true if the node can be focused, false - otherwise
     */
    boolean onNodeUnselect(NodeView view, boolean add);
}
