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
        view.getModel().translateAbs(dx, 0);
        if(dy < 0) {
            view.getModel().translateUp(-dy);
        } else if(dy > 0) {
            view.getModel().translateDown(dy);
        }
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

        if(hasChildren) {
            NodeModel lastModel = view.getModel().lastModel();
            NodeView lastView = getNodeViewByModel(lastModel);

            view.getModel().addNode(model);

            anchor = new Point(lastModel.getNodePos());
            int dy = lastView.getHeight() + props().getMinimumGap();
            anchor.translate(0, dy);

            retView.getModel().setNodePos(anchor);

            int correction = retView.getModel().getBottom() - retView.getModel().prevNode().getNodePos().y;
            correction /= 4;
            retView.getModel().getParent().firstModel().translateRel(0, -correction);
            retView.getModel().getParent().firstModel().fix();
            retView.getModel().fix();
        } else {
            view.getModel().addNode(model);

            anchor = new Point(view.getModel().getNodePos());
            anchor.translate(props().getRecommendedLinkLength(), 0);
            anchor.translate(view.getWidth(), 0);

            retView.getModel().setNodePos(anchor);
        }


        return retView;
    }
}
