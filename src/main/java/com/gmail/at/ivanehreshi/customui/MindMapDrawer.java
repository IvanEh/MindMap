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
        boolean hasChildren = view.getModel().hasChildren();
        NodeView retView = this.manageSingleModel(model);

        view.getModel().addNode(model);
        if(hasChildren) {
            NodeModel lastModel = view.getModel().lastModel().prevNode();
            NodeModel lowestModel = lastModel.findLowest();

            model.setNodePos(new Point(lastModel.getNodePos()));
            retView.translate(0, lowestModel.getBottom() - model.getY() + view.getModel().getProps().getMinimumGap());
            model.fixDown();

            int correction = (model.getBottom() - lastModel.getY() - view.getModel().getHeight())/2;
            view.getModel().firstModel().translateRelWithAlignment(0, -correction);
        } else {

            anchor = new Point(view.getModel().getNodePos());
            anchor.translate(view.getModel().getProps().getRecommendedLinkLength(), 0);
            anchor.translate(view.getWidth(), 0);

            retView.getModel().setNodePos(anchor);
        }

        view.revalidate();
        return retView;
    }
}
