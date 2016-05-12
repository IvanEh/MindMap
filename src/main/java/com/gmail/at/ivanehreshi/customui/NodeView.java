package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.MindMapApplication;
import com.gmail.at.ivanehreshi.customui.controllers.MindMapController;
import com.gmail.at.ivanehreshi.customui.controllers.NodeViewController;
import com.gmail.at.ivanehreshi.customui.controllers.ResizeController;
import com.gmail.at.ivanehreshi.models.NodeModel;
import com.gmail.at.ivanehreshi.utils.HtmlDrawer;
import com.gmail.at.ivanehreshi.utils.Resources;
import com.gmail.at.ivanehreshi.utils.Selectable;
import com.gmail.at.ivanehreshi.utils.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class NodeView extends JPanel implements Selectable{
    private MindMapController mindMapController;
    protected NodeModel model;
    private boolean selected = false;
    private State state;
    private NodeViewController nodeViewController;
    private BufferedImage cachedImage = null;

    public static final int BORDER_THICKNESS = 6;

    @Deprecated
    public NodeView(boolean dummy) {
        model = new NodeModel("Root", NodeModel.NodeSide.RIGHT);
    }

    private NodeView() {
        state = State.STATIC;

        setBorder(new ResizableBorder(BORDER_THICKNESS, Color.GREEN, Color.GRAY));
        setFocusable(true);
        setOpaque(false);

        initGui();

        setUpControllers();
    }

    private void initGui() {
    }

    public Rectangle activeArea() {
        Insets insets = getInsets();
        return new Rectangle(insets.left, insets.top,
                            getWidth() - insets.left - insets.right,
                            getHeight() - insets.top - insets.bottom);
    }

    public Rectangle contentArea() {
        Rectangle area = activeArea();
        int thickness = getModel().getProps().getThickness();
        area.x += thickness;
        area.y += thickness;
        area.width -= 2*thickness;
        area.height-= 2*thickness;
        return area;
    }

    protected void setUpControllers() {
        nodeViewController = new NodeViewController();
        addMouseListener(nodeViewController);
        addMouseMotionListener(nodeViewController);

        ResizeController resizeController
                = new ResizeController(((ResizableBorder) getBorder()).getThickness());
        addMouseListener(resizeController);
        addMouseMotionListener(resizeController);

    }

    public NodeView(NodeModel model, MindMapController mindMapController) {
        this();
        this.model = model;
        this.setSize(new Dimension(model.getWidth(), model.getHeight()));
        this.mindMapController = mindMapController;

    }

    public NodeView insertNewNode(NodeModel model) {
        if(mindMapController == null) {
            throw new IllegalStateException("");
        }

        mindMapController.onNodeModelCreated(model);
        return mindMapController.onNodeModelInsert(this, model);
    }

    public NodeView insertExisting(NodeModel model) {
        if(mindMapController == null) {
            throw new IllegalStateException("");
        }

        return mindMapController.onNodeModelInsertExisting(this, model);
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

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        getModel().setSize(getSize());
    }

    @Deprecated
    public NodeView insertNewNode(String s) {
        return insertNewNode(new NodeModel(s, getModel(), NodeModel.NodeSide.ROOT));
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

        Insets insets = getInsets();

        int nodeThickness = getModel().getProps().getThickness();
        int outerBorder   = getModel().getProps().getOuterMargin();

        drawBorder(g2d, insets, nodeThickness, outerBorder);

        drawContent(g2d);

    }

    /**
     * Method of drawing content of model. To support more
     * drawing options this method could be overrided
     * @param g2d
     */
    protected void drawContent(Graphics2D g2d) {
        if(getModel().isImageNode()) {
            drawImage(g2d);
        } else {
            drawTitle(g2d);
        }
    }

    private void drawBorder(Graphics2D g2d, Insets insets, int nodeThickness, int outerBorder) {
        g2d.setColor(getModel().getProps().getNodeColor());
        g2d.setStroke(new BasicStroke(nodeThickness));
        g2d.drawRoundRect(insets.right + nodeThickness/2,
                          insets.top + nodeThickness/2,
                          getWidth() - nodeThickness - insets.left - insets.right,
                          getHeight() - nodeThickness - insets.top - insets.left,
                          10, 10);
    }

    HtmlDrawer drawer = new HtmlDrawer();
    private void drawTitle(Graphics2D g2d) {
        if(state == State.STATIC) {
            // TODO: malicious
            Point textPosition = getAlignedTextPosition(g2d);
//            Point textPosition = getAlignedTextPos(g2d);
//            g2d.drawString(this.model.getTitle(), textPosition.x, textPosition.y);
            drawer.drawHtml(g2d, getModel().getFormattedTitle(), textPosition.x, textPosition.y);
        }
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    private void drawImage(Graphics2D g2d) {

        BufferedImage bufferedImage;
        Rectangle activeArea = activeArea();

        if(cachedImage != null) {
            bufferedImage = cachedImage;
        } else {
            bufferedImage = Resources.getInstance()
                    .getImage(getModel().getImagePath(), true);
        }

        g2d.drawImage(bufferedImage, activeArea.x, activeArea.y, null);
    }

    @Deprecated
    private Point getAlignedTextPos(Graphics2D g2d) {
        Rectangle2D textArea = getTextArea();
        Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(model.getFormattedTitle(), g2d);
        if(bounds.getHeight() > textArea.getHeight()) {
            textArea = getMaximumTextArea();
        }
        double x, y;
        x = textArea.getX() + (textArea.getWidth() - bounds.getWidth())/2;
        y = textArea.getY() + (textArea.getHeight() - bounds.getHeight())/2;
        y = y + bounds.getHeight();
        return new Point((int) x, (int)y);
    }

    // TODO: cache value
    @Deprecated
    public Point getAlignedTextPosition(Graphics2D g2d) {
        Rectangle2D textArea = getTextArea();
        Dimension dim = drawer.computeTextSize(getModel().getFormattedTitle());

        // TODO: test this one
        if(dim.getHeight() > textArea.getHeight()) {
            textArea = getMaximumTextArea();
        }

        double x, y;
        x = textArea.getX() + (textArea.getWidth() - dim.getWidth())/2;
        y = textArea.getY() + (textArea.getHeight() - dim.getHeight())/2;
        y = y + dim.getHeight();
        return new Point((int) x, (int)y);
    }

    public Rectangle getTextArea() {
        int x = getModel().getProps().getThickness() + getModel().getProps().getInnerMargin();
        int y = x;
        int width = this.getWidth() - 2*x;
        int height = this.getHeight() - 2*y;
        return new Rectangle(x, y, width, height);
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

    public NodeModel remove() {
        setVisible(false);
        if(nodeViewController != null) {
            return mindMapController.onViewRemove(this);
        }
        return this.getModel();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if(getModel() != null && getModel().isImageNode()) {
            BufferedImage bufferedImage = Resources.getInstance()
                    .getImage(getModel().getImagePath(), false);
            if(bufferedImage != null) {
                Rectangle activeArea = activeArea();
                cachedImage = Utilities.resize(bufferedImage, activeArea.width, activeArea.height);
            }
        }
    }

    @Override
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

    @Override
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

    enum State {
        EDIT,
        STATIC
    }
}
