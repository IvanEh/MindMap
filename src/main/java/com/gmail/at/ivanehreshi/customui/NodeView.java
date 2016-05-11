package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.MindMapApplication;
import com.gmail.at.ivanehreshi.customui.controllers.MindMapController;
import com.gmail.at.ivanehreshi.customui.controllers.NodeViewController;
import com.gmail.at.ivanehreshi.customui.controllers.ResizeController;
import com.gmail.at.ivanehreshi.models.NodeModel;
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
    private JTextField editor;
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

        setOpaque(false);

        initGui();

        setUpControllers();
    }

    private void initGui() {
        editor = new JTextField();
        add(editor);
        editor.setVisible(false);
        editor.setBorder(null);
        editor.setOpaque(true);
        editor.setBackground(new Color(215, 223, 223, 150));
    }

    public Rectangle activeArea() {
        Insets insets = getInsets();
        return new Rectangle(insets.left, insets.top,
                            getWidth() - insets.left - insets.right,
                            getHeight() - insets.top - insets.bottom);
    }

    protected void setUpControllers() {
        nodeViewController = new NodeViewController();
        addMouseListener(nodeViewController);
        addMouseMotionListener(nodeViewController);

        ResizeController resizeController = new ResizeController(6);
        addMouseListener(resizeController);
        addMouseMotionListener(resizeController);

        editor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nodeViewController.onEditAction(e);
                finishEditing();
            }
        });

        editor.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { }

            @Override
            public void focusLost(FocusEvent e) {
                nodeViewController.onEditAction(new ActionEvent(editor, 0, ""));
                finishEditing();
            }
        });
    }

    public NodeView(NodeModel model, MindMapController mindMapController) {
        this();
        this.model = model;
        this.setSize(new Dimension(model.getWidth(), model.getHeight()));
        this.mindMapController = mindMapController;

        setFont(model.getCachedFont());
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

    public void edit() {
        editor.setVisible(true);
        editor.grabFocus();
        editor.setText(getModel().getTitle());
        Rectangle2D textArea = getTextArea();

        editor.setBounds((int) textArea.getX(), (int)textArea.getY(),
                (int)textArea.getWidth(), (int)textArea.getHeight());
        this.state = State.EDIT;
    }

    public void finishEditing() {
        if(state == State.EDIT) {
            state = State.STATIC;
            editor.setVisible(false);
        }
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

        g2d.setColor(getModel().getProps().getNodeColor());

        g2d.setStroke(new BasicStroke(nodeThickness));
        g2d.drawRoundRect(insets.right + outerBorder + nodeThickness/2,
                          insets.top + outerBorder + nodeThickness/2,
                          getWidth()-2*outerBorder - nodeThickness - insets.left - insets.right,
                          getHeight()-2*outerBorder - nodeThickness - insets.top - insets.left,
                          10, 10);

        if(getModel().isImage()) {
            drawImage(g2d);
        } else {
            drawTitle(g2d);
        }


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
        if(state == State.STATIC) {
            Point textPosition = getAlignedTextPos(g2d);
            g2d.drawString(this.model.getTitle(), textPosition.x, textPosition.y);
        }
    }

    private void drawImage(Graphics2D g2d) {

        BufferedImage bufferedImage;
        Rectangle activeArea = activeArea();

        if(cachedImage != null) {
            bufferedImage = cachedImage;
        } else {
            bufferedImage = MindMapApplication.getInstance().getResources()
                    .getImage(getModel().getImagePath(), true);
        }

        g2d.drawImage(bufferedImage, activeArea.x, activeArea.y, null);
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
        if(getModel() != null && getModel().isImage()) {
            BufferedImage bufferedImage = MindMapApplication.getInstance().
                    getResources().getImage(getModel().getImagePath(), false);
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

    public String getEditorText() {
        return editor.getText();
    }

    enum State {
        EDIT,
        STATIC
    }
}
