package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.event.ChangeListener;

/**
 * Created by ivaneh on 28.04.16.
 */
public interface MindMapController extends ChangeListener {
    void onViewTranslate(NodeView view, int dx, int dy);

    NodeView onNodeModelInsert(NodeView view, NodeModel model);
}
