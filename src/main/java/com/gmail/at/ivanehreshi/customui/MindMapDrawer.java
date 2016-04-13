package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MindMapDrawer extends NodeView implements ChangeListener{
    Map<NodeModel, NodeView> modelToViewMap = new HashMap<>();

    public MindMapDrawer(NodeModel rootModel) {
        super(rootModel);
        createGui();
        createDebugGui();
        createModelProjection();
    }

    private void createDebugGui() {
        setLayout(new MindMapLayout());
    }

    private void createGui() {

    }

    private void createModelProjection() {
        for(NodeModel model: this.model) {
            manageSingleModel(model);
        }
    }

    private void manageSingleModel(NodeModel model) {
        NodeView view = new NodeView(model);
        model.addChangeListener(this);
        modelToViewMap.put(model, view);
        add(view);
        view.setVisible(true);
    }

    private void reset() {

    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }

    @Override
    protected void paintComponent(Graphics g) {

    }

    @Override
    public void paintComponents(Graphics g) {

    }

    NodeView getParent(NodeView view) {
        return modelToViewMap.get(view.getModel().getParent());
    }

    NodeView getNodeViewByModel(NodeModel model) {
        return modelToViewMap.get(model);
    }
}
