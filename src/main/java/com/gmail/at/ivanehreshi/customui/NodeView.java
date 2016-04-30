package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.customui.controllers.MindMapController;
import com.gmail.at.ivanehreshi.customui.controllers.NodeViewController;
import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class NodeView extends JPanel {
    private MindMapController mindMapController;
    protected NodeModel model;
    private boolean selected = false;

    @Deprecated
    public NodeView(boolean dummy) {
        model = new NodeModel("Root", NodeModel.NodeSide.RIGHT);
    }

    private NodeView() {
        setOpaque(false);
        setUpControllers();
    }

    protected void setUpControllers() {
        NodeViewController nodeViewController = new NodeViewController();
        addMouseListener(nodeViewController);
        addMouseMotionListener(nodeViewController);
    }

    public NodeView(NodeModel model, MindMapController mindMapController) {
        this();
        this.model = model;
        this.setSize(new Dimension(model.getWidth(), model.getHeight()));
        this.mindMapController = mindMapController;

        setFont(model.getCachedFont());
    }

    public NodeView insertNode(NodeModel model) {
        if(mindMapController == null) {
            throw new IllegalStateException("");
        }

        return mindMapController.onNodeModelInsert(this, model);
    }

    public NodeView translate(int dx, int dy) {
        Point p = this.getLocation();
        p.translate(dx, dy);
        this.setLocation(p);
        if(mindMapController != null) {
            mindMapController.onViewTranslate(this, dx, dy);
        }

        return this;
    }

    @Deprecated
    public NodeView insertNode(String s) {
        return insertNode(new NodeModel(s, getModel(), NodeModel.NodeSide.ROOT));
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(model.getWidth(), model.getHeight());
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        getParent().repaint(); // FIXME: malicious

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(isOpaque()) {
            return;
        }

        int nodeThickness = getModel().getProps().getThickness();
        int outerBorder = getModel().getProps().getOuterMargin();

        g2d.setColor(getModel().getProps().getNodeColor());

        g2d.setStroke(new BasicStroke(nodeThickness));
        g2d.drawRoundRect(outerBorder + nodeThickness/2, outerBorder + nodeThickness/2,
                this.getWidth()-2*outerBorder - nodeThickness, this.getHeight()-2*outerBorder - nodeThickness, 10, 10);

        drawTitle(g2d);


        if(isSelected()) {
            drawFocused(g2d);
        }

    }

    private void drawFocused(Graphics2D g2d) {
        final double length = 0.2;
        final int width = (int) (getWidth()*length);
        final int height = (int) (getHeight()*length);
        final int dx = (getWidth() - width)/2;
        final int dy = (getHeight() - height)/2;
        final int thickness = 4;

        g2d.setColor(getModel().getProps().getFocusedMarkerColor());
        g2d.setStroke(new BasicStroke(thickness));

        g2d.drawLine(thickness/2, dy, thickness/2, height + dy);
        g2d.drawLine(getWidth()-thickness/2, dy, getWidth()-thickness/2, height + dy);

        g2d.drawLine(dx, thickness/2, width + dx, thickness/2);
        g2d.drawLine(dx, getHeight()-thickness/2, width + dx, getHeight() - thickness/2);
    }

    private void drawTitle(Graphics2D g2d) {
        Point textPosition = getAlignedTextPos(g2d);
//        g2d.setStroke(new BasicStroke(1));
//        g2d.drawRoundRect(textPosition.x, textPosition.y, 10, 10, 10, 10);
        g2d.drawString(this.model.getTitle(), textPosition.x, textPosition.y);
    }

    private Point getAlignedTextPos(Graphics2D g2d) {
        Rectangle2D textArea = getTextArea();
        Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(model.getTitle(), g2d);
        if(bounds.getHeight() > textArea.getHeight()) {
            textArea = getMaximumTextArea();
        }
        double x, y;
        x = textArea.getX() + (textArea.getWidth() - bounds.getWidth())/2;
        y = textArea.getY() + (textArea.getHeight() - bounds.getHeight())/2;
        y = y + bounds.getHeight();
        return new Point((int) x, (int)y);
    }

    private Rectangle2D getTextArea() {
        double x = getModel().getProps().getOuterMargin() + getModel().getProps().getThickness() + getModel().getProps().getInnerMargin();
        double y = x;
        double width = this.getWidth() - 2*x;
        double height = this.getHeight() - 2*y;
        return new Rectangle2D.Double(x, y, width, height);
    }

    public Rectangle2D getMaximumTextArea() {
        double x = getModel().getProps().getThickness();
        double y = x;
        double width = this.getWidth() - 2*x;
        double height = this.getHeight() - 2*y - 1;
        return new Rectangle2D.Double(x, y, width, height);
    }


    public NodeModel getModel() {
        return model;
    }

    public int getBottom() {
        return (int) getLocation().getY() + (int) getSize().getHeight();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected, boolean add) {
        if(selected) {
            this.selected = getMindMapController().onNodeSelect(this, add);
        } else {
            this.selected = getMindMapController().onNodeUnselect(this, add);
        }
    }

    public void setSelected(boolean selected) {
        setSelected(selected, false);
    }

    public MindMapController getMindMapController() {
        return mindMapController;
    }

    public void setMindMapController(MindMapController mindMapController) {
        this.mindMapController = mindMapController;
    }

    @Override
    public String toString() {
        return "view( " + getModel() + ") at + getLocation()" ;
    }

}
