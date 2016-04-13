package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.models.NodeModel;
import com.sun.java.swing.SwingUtilities3;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class NodeView extends JPanel {
    protected NodeModel model;
    protected NodeStylesheet stylesheet;

    public NodeView(boolean dummy) {
        model = new NodeModel("Root");
        stylesheet = new NodeStylesheet();
    }

    private NodeView() {
        setOpaque(false);
        stylesheet = new NodeStylesheet();
    }

    public NodeView(NodeModel model) {
        this();
        this.model = model;
    }

    @Override
    public Dimension getMinimumSize() {
        int width =  SwingUtilities.computeStringWidth(this.getFontMetrics(getFont()),
                    this.getModel().getTitle())*3/2;
        double fontHeight = getFontMetrics(getFont()).getHeight();
        int height = (int) (fontHeight + props().getThickness()*2);
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(isOpaque()) {
            return;
        }

        int nodeThickness = stylesheet.getThickness();
        int outerBorder = stylesheet.getOuterMargin();

        g2d.setFont(getFont().deriveFont(stylesheet.getFontSize()));

        g2d.setColor(stylesheet.getNodeColor());
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
        double x = stylesheet.getOuterMargin() + stylesheet.getThickness() + stylesheet.getInnerMargin();
        double y = x;
        double width = this.getWidth() - 2*x;
        double height = this.getHeight() - 2*y;
        return new Rectangle2D.Double(x, y, width, height);
    }

    public Rectangle2D getMaximumTextArea() {
        double x = stylesheet.getThickness();
        double y = x;
        double width = this.getWidth() - 2*x;
        double height = this.getHeight() - 2*y - 1;
        return new Rectangle2D.Double(x, y, width, height);
    }

    protected NodeStylesheet props() {
        return stylesheet;
    }

    public Object prop(String p) {
        return props().get(p);
    }

    public NodeModel getModel() {
        return model;
    }

    @Override
    public String toString() {
        return "view( " + getModel() + ") at + getLocation()" ;
    }

}
