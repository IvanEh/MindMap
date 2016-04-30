package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MindMapDrawer extends JPanel implements MindMapController {
    Map<NodeModel, NodeView> modelToViewMap = new HashMap<>();
    NodeView rootNode;
    NodeModel model;
    LineManager lineManager;
    NodeModel.NodeSide side = NodeModel.NodeSide.RIGHT;
    private NodeView focusedNode;


    public MindMapDrawer(NodeModel rootModel) {
        setOpaque(false);
        this.model = rootModel;

        setLayout(new MindMapLayout());
        lineManager = new LineManager(this);

        createGui();
        createDebugGui();
        createModelProjection();
    }

    private void createDebugGui() {
    }

    private void createGui() {

    }

    public MindMapLayout getMindMapLayout() {
        return (MindMapLayout) this.getLayout();
    }

    private void createModelProjection() {
        for(NodeModel model: this.model) {
            manageSingleModel(model);
        }
    }

    NodeView manageSingleModel(NodeModel model) {
        NodeView view = new NodeView(model, this);
        if(model.isRootNode()) {
            rootNode = view;
        }
        model.addChangeListener(this);
        modelToViewMap.put(model, view);
        add(view);
        return view;
    }

    private void reset() {

    }

    protected void nextSide() {
        switch (side) {
            case LEFT:
                side = NodeModel.NodeSide.RIGHT;
                break;
            case RIGHT:
                side = NodeModel.NodeSide.LEFT;
                break;
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        lineManager.drawLines((Graphics2D) g);
        super.paintComponent(g);
    }

    public NodeView getRootNodeView() {
        return rootNode;
    }

    NodeView getParent(NodeView view) {
        return modelToViewMap.get(view.getModel().getParent());
    }

    NodeView getNodeViewByModel(NodeModel model) {
        return modelToViewMap.get(model);
    }


    void layoutNode(NodeView view) {
        MindMapLayout layout = getMindMapLayout();
        layout.layoutComponent(this, layout.getOrigin(this.getRootNodeView()), view);
    }


    /**
     * Response to the view translation. This synchronizes the model and
     * view.
     * @param view
     * @param dx
     * @param dy
     */
    @Override
    public void onViewTranslate(NodeView view, int dx, int dy) {
        view.getModel().translateAbsWithAlignment(dx, dy);
        doLayout();
    }


    protected Map<NodeModel, NodeView> getModelToViewMap() {
        return modelToViewMap;
    }

    /**
     * Manages the newly created model. Sets its position
     * @param view
     * @param model
     * @return
     */
    @Override
    public NodeView onNodeModelInsert(NodeView view, NodeModel model) {
        Point anchor;

        if(view.getModel().isRootNode()) {
            view.getModel().addNode(model, side);
            nextSide();
        }else {
            view.getModel().addNode(model);
        }

        NodeView retView = this.manageSingleModel(model);
        NodeModel.NodeSide side = model.getNodeSide();

        if(model.neighborsCnt() > 1) {
            NodeModel lastModel = view.getModel().lastModel(side).prevNode();
            NodeModel lowestModel = lastModel.findLowest(side);

            model.setNodePos(new Point(lastModel.getNodePos()));
            retView.translate(0, lowestModel.getBottom() - model.getY() + view.getModel().getProps().getMinimumGap());
            model.fixDown(side);

            int correction = (model.getBottom() - lastModel.getY() - view.getModel().getHeight())/2;
            view.getModel().firstModel(side).translateRelWithAlignment(0, -correction);
        } else {
            anchor = new Point(view.getModel().getNodePos());

            if(model.getNodeSide() == NodeModel.NodeSide.LEFT) {
                anchor.translate(-view.getModel().getProps().getRecommendedLinkLength(), 0);
                anchor.translate(-model.getWidth(), 0);
            } else {
                anchor.translate(view.getModel().getProps().getRecommendedLinkLength(), 0);
                anchor.translate(view.getWidth(), 0);
            }

            retView.getModel().setNodePos(anchor);
        }

        view.revalidate();
        return retView;
    }

    @Override
    public NodeView getFocusedNode() {
        return focusedNode;
    }

    @Override
    public boolean onNodeGainFocus(NodeView view) {
        NodeView f = getFocusedNode();
        if(f != null) {
            f.setFocused(false);
        }
        focusedNode = view;
        focusedNode.repaint();
        return true;
    }

    @Override
    public boolean onNodeLostFocus(NodeView view) {
        return false;
    }
}
