package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.models.NodeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class NodeView extends JPanel {
    private MindMapDrawer mindMapDrawer;
    protected NodeModel model;

    public NodeView(boolean dummy) {
        model = new NodeModel("Root");
    }

    private NodeView() {
        setOpaque(false);
    }

    public NodeView(NodeModel model, MindMapDrawer mindMapDrawer) {
        this();
        this.model = model;
        this.setSize(new Dimension(model.getWidth(), model.getHeight()));
        this.mindMapDrawer = mindMapDrawer;
    }

    public NodeView insertNode(NodeModel model) {
        if(mindMapDrawer == null) {
            throw new IllegalStateException("");
        }

        return mindMapDrawer.onNodeModelInsert(this, model);
    }

    public NodeView translate(int dx, int dy) {
        Point p = this.getLocation();
        p.translate(dx, dy);
        this.setLocation(p);
        if(mindMapDrawer != null) {
            mindMapDrawer.onViewTranslate(this, dx, dy);
        }

        return this;
    }

    public NodeView insertNode(String s) {
        return insertNode(new NodeModel(s, getModel()));
    }

    @Override
    public Dimension getMinimumSize() {
//        int width =  SwingUtilities.computeStringWidth(this.getFontMetrics(getFont()),
//                    this.getModel().getTitle())*3/2;
//        double fontHeight = getFontMetrics(getFont()).getHeight();
//        int height = (int) (fontHeight + props().getThickness()*2);
//        return new Dimension(width, height);
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

        g2d.setFont(getFont().deriveFont(getModel().getProps().getFontSize()));

        g2d.setColor(getModel().getProps().getNodeColor());
        g2d.setStroke(new BasicStroke(nodeThickness));
        g2d.drawRoundRect(outerBorder + nodeThickness/2, outerBorder + nodeThickness/2,
                this.getWidth()-2*outerBorder - nodeThickness, this.getHeight()-2*outerBorder - nodeThickness, 10, 10);


        drawTitle(g2d);
    }

    public Font getFont() {
        //Font font = new Font("Ubuntu", Font.PLAIN, stylesheet.getFontSize());
        //return font;
        return super.getFont();
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



    @Override
    public String toString() {
        return "view( " + getModel() + ") at + getLocation()" ;
    }

}
