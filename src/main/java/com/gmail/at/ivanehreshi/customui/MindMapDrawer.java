package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MindMapDrawer extends JPanel implements ChangeListener{
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
        view.setVisible(true);
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


    /**
     * Response to the view translation. This synchronizes the model and
     * view.
     * @param view
     * @param dx
     * @param dy
     */
    public void onViewTranslate(NodeView view, int dx, int dy) {
        view.getModel().translateAbsWithAlignment(dx, dy);
        doLayout();
    }



    private void alignIfInterfereWithLowerBranch(NodeView view, Integer dx, Integer dy, boolean validate) {

    }

    private void alignIfInterfereWithNeighbor(NodeView view, Integer dx, Integer dy, boolean validate) {
        NodeModel model = view.getModel();
        int anotherDy = (int) model.getNodePos().getY();
        if(anotherDy < 0) {
            model.translateRel(0, (int) - anotherDy);

            NodeView prevView = getNodeViewByModel(model.prevNode());
            prevView.translate(0, (int) anotherDy);
            layoutNode(view);
        }
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
    public NodeView onNodeModelInsert(NodeView view, NodeModel model) {
        Point anchor;
        boolean hasChildren = view.getModel().hasChilds();
        NodeView retView = this.manageSingleModel(model);

        view.getModel().addNode(model);
        if(hasChildren) {
            NodeModel lastModel = view.getModel().lastModel().prevNode();
            NodeModel lowestModel = lastModel.findLowest();

            model.setNodePos(new Point(lastModel.getNodePos()));
            retView.translate(0, lowestModel.getBottom() - model.getY() + view.props().getMinimumGap());
            model.fixDown();

            int correction = (model.getBottom() - lastModel.getY() - view.getModel().getHeight())/2;
            view.getModel().firstModel().translateRelWithAlignment(0, -correction);
        } else {

            anchor = new Point(view.getModel().getNodePos());
            anchor.translate(view.props().getRecommendedLinkLength(), 0);
            anchor.translate(view.getWidth(), 0);

            retView.getModel().setNodePos(anchor);
        }


        return retView;
    }
}
