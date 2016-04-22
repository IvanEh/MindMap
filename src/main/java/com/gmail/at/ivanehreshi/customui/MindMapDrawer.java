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

    public void onViewTranslate(NodeView view, int dx, int dy) {
        if(dy > 0) {
            view.getModel().getNodePos().translate(dx, dy);
            this.onModelTranslate(view, dx, dy);
        } else {
            if(view.getModel().isRelative()) {
                NodeModel prevModel = view.getModel().prevNode();
                NodeView prevView = getNodeViewByModel(prevModel);
                if(view.getY() > prevView.getBottom()) {
                    view.getModel().getNodePos().translate(dx, dy);
                    view.invalidate();
                    onModelTranslate(view, dx, dy);
                } else {
                    Point origLoc = view.getLocation();
                    origLoc.translate(-dx, -dy);

                    NodeModel firstNode = view.getModel().getParent().firstModel();
                    NodeView firstView = getNodeViewByModel(firstNode);

                    int currModelAbsDy = (int) (origLoc.getY() - prevView.getBottom());
                    int firstModelAbsDy = -dy - currModelAbsDy + prevView.getHeight();
                    view.getModel().translate(dx, -currModelAbsDy);
                    onModelTranslate(view, dx, -currModelAbsDy);
                    firstView.translate(0, -firstModelAbsDy);
                }

            }
        }
    }
}
