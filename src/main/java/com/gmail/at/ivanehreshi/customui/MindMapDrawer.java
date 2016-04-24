package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MindMapDrawer extends NodeView implements ChangeListener{
    Map<NodeModel, NodeView> modelToViewMap = new HashMap<>();
    private NodeView rootNode;


    public MindMapDrawer(NodeModel rootModel) {
        super(rootModel, null);
        createGui();
        createDebugGui();
        createModelProjection();
    }

    private void createDebugGui() {
        setLayout(new MindMapLayout());
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
        view.setVisible(true);
        return view;
    }

    private void reset() {

    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }

    @Override
    protected void paintComponent(Graphics g) {

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

    // TODO: Чи може медіатор повертати значення?
    public NodeView onNodeModelInsert(NodeModel model) {
        NodeView view = this.manageSingleModel(model);

        if(view.getModel().isRelative()) {
            int halfHeight = view.getPreferredSize().height / 2;
            view.getModel().getParent().firstModel().getNodePos().translate(0, -halfHeight); // TODO: fire an event
        }

        return view;
    }


    public void onModelTranslate(NodeView view, int dx, int dy) {
        if(dy > 0) {
            onModelMoveDown(view, dx, dy);
        } else {
            onModelMoveUp(view, dx, dy);
        }

    }

    private void onModelMoveUp(NodeView view, int dx, int dy) {
        NodeModel model = view.getModel();
        Point viewLoc = view.getLocation();

//        view.revalidate();
        for (NodeModel m: model.getNodes()) {
            getNodeViewByModel(m).revalidate();
        }

        if(model.isFirst()) {
            NodeModel p = model.computeFirstSmallestUpperLeaning();
            if(p == null) {
                return;
            }
            NodeView leaningView = getNodeViewByModel(p);
            Point leaningPos = leaningView.getLocation();
            if(viewLoc.y < leaningPos.y) {
                leaningView.translate(0, viewLoc.y - leaningPos.y); // TODO: remove magic number
            }
            return;
        }


        NodeModel p = model.prevNode();
        NodeView viewToTranslate = getNodeViewByModel(p);
        Point toTranslateLoc = viewToTranslate.getLocation();
        if(viewLoc.y < toTranslateLoc.y + viewToTranslate.getHeight()) {
            viewToTranslate.translate(0, viewLoc.y - toTranslateLoc.y - viewToTranslate.getHeight());
        }
    }

    void layoutNode(NodeView view) {
        MindMapLayout layout = getMindMapLayout();
        layout.layoutComponent(this, layout.getOrigin(this.getRootNodeView()), view);
    }

    private void onModelMoveDown(NodeView view, int dx, int dy) {
        NodeModel model = view.getModel();
        Point viewLoc = view.getLocation();

        for (NodeModel m: model.getNodes()) {
            getNodeViewByModel(m).revalidate();
        }

        if(model.isLast()) {
            NodeModel p = model.computeFirstLargestLeaning();
            if(p == null) {
                return;
            }
            NodeView leaningView = getNodeViewByModel(p);
            Point leaningPos = leaningView.getLocation();
            if(viewLoc.y + view.getHeight() > leaningPos.y) {
                leaningView.translate(0, viewLoc.y + view.getHeight() - leaningPos.y); // TODO: remove magic number
            }
            return;
        }


        NodeModel p = model.nextNode();
        NodeView viewToTranslate = getNodeViewByModel(p);
        Point toTranslateLoc = viewToTranslate.getLocation();
        viewToTranslate.revalidate();
        onModelTranslate(viewToTranslate, 0, dy);
    }

    /**
     * Response to the view translation. This synchronizes the model and
     * view.
     * @param view
     * @param dx
     * @param dy
     */
    public void onViewTranslate(NodeView view, int dx, int dy) {
        view.getModel().translate(dx, dy);
        if(!view.getModel().isLast()) {
            int ind = view.getModel().index();
            int len = view.getModel().getParent().getNodes().size();

            if(dy < 0) {
                NodeModel nextModel = view.getModel().nextNode();
                nextModel.translate(0, -dy);
            }


            for (int i = ind + 1; i < len; i++) {
                NodeModel currModel = view.getModel().getParent().getNodes().get(i);
                NodeView modelView = getNodeViewByModel(currModel);
                layoutNode(modelView);
            }
        }

        this.align(view, dx, dy, true);
    }

    private void align(NodeView view, Integer dx, Integer dy, boolean validate) {
        NodeModel model = view.getModel();
        if(model.isFirst()) {
            // TODO: dy == 0?
            if(dy > 0) {

            } else {
                // Interference with the higher branch, all neighbors need revalidation
                alignIfInterfereWithUpBranch(view, dx, dy, false);
            }
        } else if(model.isRelative()) {
            // Interference possible only with negative positions
            alignIfInterfereWithNeighbor(view, dx, dy, false);

        } else if(model.isLast()) {
            // Interference possible only with the lower branch, no nodes need to be revalidated
            alignIfInterfereWithLowerBranch(view, dx, dy, false);
        }
        if(validate) {

            layoutNode(view); // TODO: revalidate ancestors
        }
    }

    private void alignIfInterfereWithLowerBranch(NodeView view, Integer dx, Integer dy, boolean validate) {

    }

    private void alignIfInterfereWithNeighbor(NodeView view, Integer dx, Integer dy, boolean validate) {
        NodeModel model = view.getModel();
        int anotherDy = (int) model.getNodePos().getY();
        if(anotherDy < 0) {
            model.translate(0, (int) - anotherDy);

            NodeView prevView = getNodeViewByModel(model.prevNode());
            prevView.translate(0, (int) anotherDy);
            layoutNode(view);
        }
    }

    /**
     *
     * @param view
     * @param dx
     * @param dy is necessary;it assumed that dy < 0
     * @param validate
     */
    private void alignIfInterfereWithUpBranch(NodeView view, int dx, int dy, boolean validate) {
        NodeModel model = view.getModel();

        NodeView lowestView = computeSmallerUpperBranchNode(view); // TODO: align against all nodes
        if(lowestView == null) {
            return;
        }

        if(view.getY() < lowestView.getBottom()) {
            int correction = -view.getY() + lowestView.getBottom();
            lowestView.translate(0, -correction); // TODO: test against recursive check
        }
    }

    public NodeView computeSmallerUpperBranchNode(NodeView view) {
        NodeModel model0 = view.getModel();

        if(model0.isRootNode()) {
            return null;
        }

        NodeModel model = model0.getParent();
        while (model.getParent() != null && model.prevNode() == null) {
            model = model.getParent();
        }

// TODO: return null if not smaller
        model = model.prevNode();
        NodeModel lowest = null;
        NodeView lowestView = null;
        while (model != null && model.hasChilds() ) {
            NodeView currView = getNodeViewByModel(model);

            if(currView.getBottom() > view.getY()) { // TODO: opt memory - cache pos
                lowest = model;
                lowestView = getNodeViewByModel(lowest);
                return lowestView;
            }
            model = model.lastModel();
        }

        return null;
    }
}
