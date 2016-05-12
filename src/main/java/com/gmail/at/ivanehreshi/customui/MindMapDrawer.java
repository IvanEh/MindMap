package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.customui.controllers.ChangesTracker;
import com.gmail.at.ivanehreshi.customui.controllers.FocusMonitor;
import com.gmail.at.ivanehreshi.customui.controllers.MindMapController;
import com.gmail.at.ivanehreshi.customui.controllers.MindMapMoveController;
import com.gmail.at.ivanehreshi.menu.NodeViewPopupBuilder;
import com.gmail.at.ivanehreshi.models.NodeModel;
import com.gmail.at.ivanehreshi.models.TableNodeModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MindMapDrawer extends JPanel implements MindMapController {
    Map<NodeModel, NodeView> modelToViewMap = new HashMap<>();
    NodeView rootNode;
    NodeModel model;
    LineManager lineManager;
    NodeModel.NodeSide side = NodeModel.NodeSide.RIGHT;
    private JPopupMenu nodePopup = new NodeViewPopupBuilder.Director().build();
    private ArrayList<NodeView> selectedNodes = new ArrayList<>();
    private ChangesTracker<Point> positionTracker;
    private ChangesTracker<Dimension> dimensionTracker;
    private LightWeightNodeEditor nodeEditor;
    private FocusMonitor focusMonitor;

    public MindMapDrawer(NodeModel rootModel) {
        setOpaque(false);
        this.model = rootModel;

        setLayout(new MindMapLayout());
        lineManager = new LineManager(this);
        setFocusable(true);

        focusMonitor = new FocusMonitor();

        createGui();
        createDebugGui();
        createModelProjection();
        createChangesTrackers();
        setUpControllers();
    }

    private void createChangesTrackers() {
        positionTracker = new ChangesTracker<>(
                model ->  model.getPosition(),
                (model, prop) -> {
                    model.setNodePos(prop);
                    getNodeViewByModel(model).revalidate();
                });

        dimensionTracker = new ChangesTracker<>(
                model -> model.getSize(),
                (model, dim) -> model.setSize(dim)
        );
    }

    private void setUpControllers() {
        MindMapMoveController moveController = new MindMapMoveController(this);

        addMouseMotionListener(moveController);
        addMouseListener(moveController);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                grabFocus();
            }
        });

    }

    private void createDebugGui() {
    }

    private void createGui() {
        nodeEditor = new LightWeightNodeEditor(null);
        add(nodeEditor);
        nodeEditor.setVisible(false);
    }

    public MindMapLayout getMindMapLayout() {
        return (MindMapLayout) this.getLayout();
    }

    public FocusMonitor getFocusMonitor() {
        return focusMonitor;
    }

    private void createModelProjection() {
        for(NodeModel model: this.model) {
            onNodeModelInsert(null, model);
        }
    }

    NodeView manageSingleModel(NodeModel model) {
        NodeView view;
        if(model instanceof TableNodeModel) {
            view = new TableNodeView((TableNodeModel)model, this);
        } else {
            view = new NodeView(model, this);
        }
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

    @Override
    public void onViewChangeSize(NodeView view, int dx, int dy) {
        view.getModel().setAutoResize(false);
    }


    protected Map<NodeModel, NodeView> getModelToViewMap() {
        return modelToViewMap;
    }

    public LightWeightNodeEditor getNodeEditor() {
        return nodeEditor;
    }

    /**
     * Manages the newly created model. Sets its position
     * @param view
     * @param model
     * @return
     */
    // TODO: rename to onNodeViewModelInsert
    @Override
    public NodeView onNodeModelInsert(NodeView view, NodeModel model) {
        NodeView retView = null;
        if(view == null) {
            if(model.isRootNode()) {
                retView =  manageSingleModel(model);
            }
        } else {
            retView = createLaidOutView(view, model);
        }

        model.addChangeListener((e -> {
            NodeModel.ChangeEvent ch = (NodeModel.ChangeEvent) e;
            switch (ch.getCause()) {
                case TITLE:
                    onModelChangeTitle(ch);
                    break;
                case CONTENT:
                    onModelChangeContent(ch);
                    break;
            }
        }));

        getFocusMonitor().monitor(retView);
        if(retView instanceof TableNodeView) {
            getFocusMonitor().monitor(((TableNodeView) retView).getTable());
        }
        return retView;
    }

    @Override
    public void onNodeModelCreated(NodeModel model) {
        model.addBeforeChangeListener(
                m -> getPositionTracker().getRetrieveUnchangedListener()
                    .actionPerformed(new ActionEvent(model, 0, "")));
        model.addChangeListener(
                e -> getPositionTracker().getRetrieveChangedListener()
                    .actionPerformed(new ActionEvent(model, 0, "")),
                NodeModel.ChangeEvent.Cause.BOUNDS
        );
    }

    public void onModelChangeContent(NodeModel.ChangeEvent ch) {
    }

    public void onModelChangeTitle(NodeModel.ChangeEvent ch) {
        NodeModel model = ch.getSource();

        validate();
    }

    @Override
    public NodeModel onViewRemove(NodeView view) {
        NodeModel model = view.getModel();
        model.removeFromParent();
        for(NodeModel m: model) {
            this.remove(getModelToViewMap().remove(m));
        }
        return model;
    }

    @Override
    public NodeView onNodeModelInsertExisting(NodeView view, NodeModel model) {
        final ArrayList<NodeModel> EMPTY = new ArrayList<>();

        model.makeCoordsRelative(true);

        // HACK: make NodeModel think it has no children
        ArrayList<NodeModel> right = model.getRightNodes();
        ArrayList<NodeModel> left = model.getLeftNodes();
        model.setRightNodes(EMPTY);
        model.setLeftNodes(EMPTY);

        NodeModel.NodeSide oldSide = model.getNodeSide();

        NodeView retView = view.insertNewNode(model);


        // Remove HACK!
        if(oldSide != model.getNodeSide()) {
            model.setRightNodes(left);
            model.setLeftNodes(right);
            model.getFixedNodes().forEach(node -> node.forEach(x -> x.inverseLeaningIfRelativeCoords()));
        } else {
            model.setRightNodes(right);
            model.setLeftNodes(left);
        }

        model.getFixedNodes().forEach(node -> {
            node.makeCoordsAbs();
            node.forEach(_node -> manageSingleModel(_node));
        });
        model.fix0(NodeModel.NodeSide.LEFT);
        model.fix0(NodeModel.NodeSide.RIGHT);

        getFocusMonitor().monitor(retView);
        return retView;
    }

    private NodeView createLaidOutView(NodeView parent, NodeModel model) {
        Point anchor;

        if(parent.getModel().isRootNode()) {
            parent.getModel().addNode(model, side);
            nextSide();
        }else {
            parent.getModel().addNode(model);
        }

        NodeView retView = this.manageSingleModel(model);
        NodeModel.NodeSide side = model.getNodeSide();

        if(model.neighborsCnt() > 1) {
            NodeModel lastModel = parent.getModel().lastModel(side).prevNode();
            NodeModel lowestModel = lastModel.findLowest(side);

            model.setNodePos(new Point(lastModel.getMutNodePos()));
            retView.translate(0, lowestModel.getBottom() - model.getY() + parent.getModel().getProps().getMinimumGap());
            model.fixDown(side);

            int correction = (model.getBottom() - lastModel.getY() - parent.getModel().getHeight())/2;
            parent.getModel().firstModel(side).translateRelWithAlignment(0, -correction);
        } else {
            anchor = new Point(parent.getModel().getMutNodePos());

            if(model.getNodeSide() == NodeModel.NodeSide.LEFT) {
                anchor.translate(-parent.getModel().getProps().getRecommendedLinkLength(), 0);
                anchor.translate(-model.getWidth(), 0);
            } else {
                anchor.translate(parent.getModel().getProps().getRecommendedLinkLength(), 0);
                anchor.translate(parent.getWidth(), 0);
            }

            retView.getModel().setNodePos(anchor);
        }

        parent.revalidate();
        return retView;
    }

    @Override
    public ArrayList<NodeView> getSelection() {
        return selectedNodes;
    }

    @Override
    public boolean onNodeSelect(NodeView view, boolean add) {
        ArrayList<NodeView> selection = getSelection();
        if(!add) {
            onNodeUnselect(null, false);
            selection.add(view);
        } else {
            selection.add(view);
        }

        return true;
    }

    @Override
    public boolean onNodeUnselect(NodeView view, boolean add) {
        if(!add) {
            ArrayList<NodeView> copy = new ArrayList<>(getSelection());
            getSelection().clear();
            copy.forEach(v -> v.setSelected(false, true));
        } else {
            getSelection().remove(view);
        }

        return false;
    }

    @Override
    public void onMindMapTranslate(int dx, int dy) {
        getMindMapLayout().translate(dx, dy);
        doLayout();
    }

    @Override
    public void onModelViewContextMenu(NodeView view, int x, int y) {
        nodePopup.show(view, x, y);
    }

    public ChangesTracker<Point> getPositionTracker() {
        return positionTracker;
    }

    public ChangesTracker<Dimension> getDimensionTracker() {
        return dimensionTracker;
    }
}
