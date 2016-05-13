package com.gmail.at.ivanehreshi.customui.controllers;

import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.event.ChangeListener;
import java.util.ArrayList;

public interface MindMapController extends ChangeListener {


    void onViewTranslate(NodeView view, int dx, int dy);

    NodeView onNodeModelInsert(NodeView view, NodeModel model);

    void onNodeModelCreated(NodeModel model);

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

    void onMindMapTranslate(int dx, int dy);

    void onModelViewContextMenu(NodeView view, int x, int y);

    void onModelChangeContent(NodeModel.ChangeEvent ch);

    void onModelChangeTitle(NodeModel.ChangeEvent ch);

    /**
     * Fired when the user tries to remove a node
     * @param view
     * @return
     */
    NodeModel onViewRemove(NodeView view);

    NodeView onNodeModelInsertExisting(NodeView view, NodeModel model);

    void onViewChangeSize(NodeView view, int dx, int dy);


}
