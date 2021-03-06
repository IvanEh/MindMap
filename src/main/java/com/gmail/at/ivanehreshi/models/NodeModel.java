package com.gmail.at.ivanehreshi.models;

import com.gmail.at.ivanehreshi.customui.NodeStylesheet;
import com.gmail.at.ivanehreshi.customui.NodeView;
import com.gmail.at.ivanehreshi.utils.ConcatIter;
import com.gmail.at.ivanehreshi.utils.HtmlDrawer;
import com.gmail.at.ivanehreshi.utils.Resources;
import com.gmail.at.ivanehreshi.utils.Utilities;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

public class NodeModel implements Iterable<NodeModel>{
    private String title;
    private String content;
    private String imagePath;

    @Deprecated
    private ArrayList<NodeModel> leftNodes;
    private ArrayList<NodeModel> rightNodes;
    private NodeSide nodeSide;
    private ArrayList<ChangeListener> changeListeners;
    private ArrayList<BeforeChangeListener> beforeChangeListeners;
    private NodeModel parentNode;
    private NodeStylesheet props;
    private Point pos;
    private int height = 20;
    private int width = 50;
    private boolean autoResize = true;
    private Font cachedFont;

    public NodeModel(String title, NodeSide side) {
        this.title = title;
        this.content = "";
        this.imagePath = null;
        leftNodes = new ArrayList<>();
        rightNodes = new ArrayList<>();
        nodeSide = side;
        changeListeners = new ArrayList<>();
        beforeChangeListeners = new ArrayList<>();
        pos = new Point();
        props = new NodeStylesheet();

        updateModelPreferredSize();
    }

    public void updateModelPreferredSize() {
        updateModelPreferredSize(false);
    }


    public void updateModelPreferredSize(boolean force) {
        if(!isAutoResizeEnabled() && !force)
            return;

        if(isTextNode()) {
            int border = computeTotalInset() + NodeView.BORDER_THICKNESS;
            border *= 2;

            Dimension dim = Utilities.getHtmlDrawer().computeTextSize(getFormattedTitle());
            setSize(dim.width + border, dim.height + border);

        } else {
            BufferedImage image = Resources.getInstance()
                        .getImage(getImagePath(), false);
            if(image == null) {
                return;
            }
            setSize(new Dimension(image.getWidth(), image.getHeight()));
        }
    }



    public Font cacheFont() {
        if(cachedFont == null) {
            cachedFont = new Font(getProps().getFontName(), Font.PLAIN, getProps().getFontSize());
        }
        return cachedFont;
    }

    public NodeModel(String title, NodeModel parentNode, NodeSide side) {
        this(title, side);
        this.parentNode = parentNode;
    }

    public NodeModel lastModel(NodeSide side) {
        if(getNodes(side).size() == 0)
            return null;

        return getNodes(side).get(getNodes(side).size() - 1);
    }

    public NodeModel firstModel(NodeSide side) {
        if(getNodes(side).size() == 0)
            return null;

        return getNodes(side).get(0);
    }

    public NodeModel translateRel(int dx, int dy) {
        this.translateAbs(dx, dy);
        if(!isLast()) {
            neighbors().subList(index() + 1, neighborsCnt())
                      .forEach(node -> node.translateAbs(0, dy));
        }

        return this;
    }

    public int neighborsCnt() {
        return neighbors().size();
    }

    public ArrayList<NodeModel> neighbors() {
        if(isRootNode())
            return null;

        return getParent().getNodes(getNodeSide());
    }

    public NodeModel translateAbs(int dx, int dy) {
        fireBeforeChangeEvent();

        Point backup = getNodePos();

        pos.translate(dx, dy);
        getLeftNodes().forEach(node -> node.translateAbs(dx, dy));
        getRightNodes().forEach(node -> node.translateAbs(dx, dy));

        getChangeListeners().forEach(l -> l.stateChanged(
                new ChangeEvent(this, ChangeEvent.Cause.BOUNDS, backup)));

        return this;
    }

    // TODO: Review implementation
    @Deprecated
    public NodeModel translateAbsWithAlignment(int dx, int dy) {
        this.translateAbs(dx, dy);
        if(dy < 0) {
            this.fixUp(getNodeSide());
        } else {
            this.fixDown(getNodeSide());
        }
        return this;
    }

    // TODO: Review implementation
    @Deprecated
    public NodeModel translateRelWithAlignment(int dx, int dy) {
        this.translateRel(dx, dy);
        if(dy < 0) {
            this.fixUp(getNodeSide());
        } else {
            this.fixDown(getNodeSide());
        }
        return this;

    }

    public NodeModel translateUpperNodes(int dx,int dy) {
        int ind = index();
        this.translateAbs(dx, dy);
        neighbors().subList(0, ind).forEach((node) -> node.translateAbs(0, dy));
        return this;
    }

    public NodeModel translateLowerNodes(int dx, int dy) {
        int ind = index();
        int len = neighborsCnt();

        this.translateAbs(dx, dy);
        neighbors().subList(ind + 1, len).forEach((node) -> node.translateAbs(0, dy));
        return this;
    }

    /**
     * Moves the node to the specified position with its children
     * Doesn't do alignment and don't move upper or higher nodes
     * @param x
     * @param y
     * @return current instance of node
     */
    public NodeModel moveTo(int x, int y) {
        int dx = x - pos.x;
        int dy = y - pos.y;
        translateAbs(dx, dy);
        return this;
    }

    /**
     *  Does exactly what @see moveTo(int x,int y) do
     * @param p
     * @return
     */
    public NodeModel moveTo(Point p) {
        return moveTo(p.x, p.y);
    }

    public NodeModel fixUp(NodeSide side) {
        NodeModel upBranch = this.findPrevBranch();
        if(upBranch == null) {
            return this;
        }

        NodeModel lowest = upBranch.findLowest(side);
        NodeModel highest = this.findHighest(side);

        int correction = lowest.getBottom() - highest.getY();
        if(correction >= 0) {
            upBranch.translateUpperNodes(0, -correction);
            NodeModel firstModel = upBranch.getParent().firstModel(side);
            if(firstModel != null) {
                firstModel.fixUp(side);
            }
        }

        return this;
    }

    public boolean isImageNode() {
        return imagePath != null;
    }

    public NodeModel fixDown(NodeSide side) {
        NodeModel nextBranch = findNextBranch();
        if(nextBranch == null)
            return this;

        NodeModel lowest = this.findLowest(side);
        NodeModel highest = nextBranch.findHighest(side);

        int correction = lowest.getBottom() - highest.getY();
        if(correction >= 0) {
            nextBranch.translateLowerNodes(0, correction);
            NodeModel lastModel = nextBranch.getParent().lastModel(side);
            if(lastModel != null) {
                lastModel.fixDown(side);
            }
        }

        return this;
    }

    public NodeModel fix0(NodeSide side) {
        fixUp(side);
        return fixDown(side);
    }

    public NodeModel findPrevBranch() {
        NodeModel curr = this;
        while (curr.parentNode != null && curr.prevNode() == null) {
            curr = curr.parentNode;
        }
        if(curr == null || curr.isRootNode())
            return null;

        return curr.prevNode();
    }

    public NodeModel findNextBranch() {
        NodeModel curr = this;
        while (curr.parentNode != null && curr.nextNode() == null) {
            curr = curr.parentNode;
        }
        if(curr == null || curr.isRootNode())
            return null;

        return curr.nextNode();
    }

    // TODO: make a general version for both sides
    public NodeModel findLowest(NodeSide side) {
        NodeModel lowest = this;
        NodeModel curr = this;
        while (curr != null) {
            if(curr.getY() > lowest.getY()) {
                lowest = curr;
            }
            curr = curr.lastModel(side);
        }

        return lowest;
    }

    // TODO: make a general version for both sides
    public NodeModel findHighest(NodeSide side) {
        NodeModel highest = this;
        NodeModel curr = this;
        while (curr != null) {
            if(curr.getY() < highest.getY()) {
                highest = curr;
            }
            curr = curr.firstModel(side);
        }

        return highest;
    }

    // TODO: make a general version for both sides
    public Rectangle computeNodeInvariantBounds(NodeSide side) {
        NodeModel highest = findHighest(side);
        NodeModel lowest = findLowest(side);
        return new Rectangle(highest.getX(), highest.getY(),
                0, lowest.getBottom() - highest.getY());
    }

    public boolean isRelative() {
        return !isFirst();
    }

    public void addNode(NodeModel model, NodeSide side) {
        getNodes(side).add(model);
        model.parentNode = this;
        model.setNodeSide(side);
    }

    public void addNode(int index, NodeModel removed, NodeSide nodeSide) {
        getNodes(nodeSide).add(index, removed);
        removed.parentNode = this;
        removed.setNodeSide(nodeSide);
    }

    // TODO: fix typo
    // TODO: make a general version for both sides
    public boolean hasChildren(NodeSide side) {
        return !getNodes(side).isEmpty();
    }

    public int index() {
        if(isRootNode()) {
            return 0;
        }
        return neighbors().indexOf(this);
    }

    public NodeModel prevNode() {
        int ind = index();
        if(ind > 0) {
            return neighbors().get(ind-1);
        }

        return null;
    }

    public boolean swapDown() {
        int index = index();
        if(index != -1 && index != neighborsCnt()-1) {
            NodeModel currNode = this;
            NodeModel nextNode = neighbors().get(index + 1);

            Rectangle currRect = currNode.computeNodeInvariantBounds(getNodeSide());
            Rectangle nextRect = nextNode.computeNodeInvariantBounds(getNodeSide());

            int currCorrection = (int) (nextRect.getMaxY() - currRect.getMaxY());
            int nextCorrection = (int) (nextRect.getY() - currRect.getY());

            currNode.translateAbs(0, currCorrection);
            nextNode.translateAbs(0, -nextCorrection);

            Collections.swap(neighbors(), index, index+1);
            return true;
        }
        return false;
    }

    public boolean swapUp() {
        int index = index();
        if(index != -1 && index != 0) {
            NodeModel prevNode = neighbors().get(index - 1);
            return prevNode.swapDown();
        }
        return false;
    }

    public void addChangeListener(ChangeListener l) {
        changeListeners.add(l);
    };

    public void addBeforeChangeListener(BeforeChangeListener l) {
        beforeChangeListeners.add(l);
    }

    public void addChangeListener(ChangeListener l, ChangeEvent.Cause cause) {
        addChangeListener(e -> {
            if (e instanceof NodeModel.ChangeEvent){
                NodeModel.ChangeEvent event = (NodeModel.ChangeEvent) e;
                if(event.getCause().equals(cause)) {
                    l.stateChanged(event);
                }
            }
        });
    }

    public void removeChangeListener(ChangeListener l) {
        changeListeners.remove(l);
    }

    public void removeBeforeChangeListener(BeforeChangeListener l) {
        beforeChangeListeners.remove(l);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        fireBeforeChangeEvent();

        this.title = title;

        fireChangeEvent(new ChangeEvent(this, ChangeEvent.Cause.TITLE, null));
    }

    public void updatePreferredSizeAndPush(boolean pushItself) {
        updatePreferredSizeAndPush(false, pushItself);
    }

    public void updatePreferredSizeAndPush(boolean force, boolean pushItself) {
        int oldWidth = getWidth();
        int oldHeight = getHeight();

        updateModelPreferredSize(force);

        if(pushItself) {
            int dw = getWidth() - oldWidth;
            if(isLeft()) {
                translateAbs(-dw, 0);
            }
        }

        pushNodes(oldWidth, oldHeight);
    }

    public void pushNodes(int oldWidth, int oldHeight) {
        int dw = (int) (getWidth() - oldWidth);
        // TODO: take into account dh, left and right nodes
        int dh = (int) (getHeight() - oldHeight);

        final int finalDw = dw;
        if (isRight()) {
            getRightNodes().forEach(m -> m.translateAbs(finalDw, 0));
        } else {
//            translateAbs(-finalDw/2, 0);
//            getRightNodes().forEach(m -> m.translateAbs(finalDw, 0));
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getFormattedContent() {
        throw new NotImplementedException();
    }

    public String getFormattedTitle() {
        return
          HtmlDrawer.encloseWithHtml(
           HtmlDrawer.encloseWithDecoration(
            HtmlDrawer.encloseWithFont(getTitle(),
                  getProps().getFontSize(),
                  getProps().getFontName(),
                  getProps().getFontColor()
                  ),
            props.getItalicTitle(), props.getBoldTitle(), props.getUnderscoredTitle())
        );
    }

    protected void fireChangeEvent() {
        javax.swing.event.ChangeEvent event = new javax.swing.event.ChangeEvent(this);
        fireChangeEvent(event);
    }
    private void fireChangeEvent(ChangeEvent.Cause cause) {
        fireChangeEvent(new ChangeEvent(this, cause, null));
    }

    protected void fireBeforeChangeEvent() {
        beforeChangeListeners.forEach(l -> l.beforeChange(this));
    }

    public ArrayList<NodeModel> getNodes(NodeSide side) {
        switch (side){
            case LEFT:
                return getLeftNodes();
            case RIGHT:
                return getRightNodes();
        }
        return null;
    }

    public Point getPosition() {
        return new Point((int) pos.x, (int) pos.y);
    }

    protected void fireChangeEvent(javax.swing.event.ChangeEvent event) {
        changeListeners.forEach((listener) -> listener.stateChanged(event) );
    }

    public Point getMutNodePos() {
        return pos;
    }

    public Point getNodePos() {
        return new Point(pos);
    }

    public void setNodePos(Point nodePos) {
        fireBeforeChangeEvent();

        this.pos = new Point(nodePos);

        fireChangeEvent(new ChangeEvent(this, ChangeEvent.Cause.BOUNDS, null));
    }

    public boolean isRootNode() {
        return getParent() == null;
    }

    public NodeModelIterator iterator(NodeSide side) {
        return new NodeModelIterator(side);
    }

    public NodeModel getParent() {
        return parentNode;
    }

    public NodeModel getPrev() {
        if(isRootNode()) {
            return null;
        }

        int index = index();
        if(index != 0) {
            return neighbors().get(index - 1);
        }

        return null;
    }

    @Override
    public String toString() {
        return this.title;
    }


    public Point computeRelativePosition() {
        Point parentLoc = getParent().getPosition();
        Point thisLoc = getPosition();
        return new Point((int)(thisLoc.getX() - parentLoc.getX()), (int)(thisLoc.getY() - parentLoc.getY()));
    }

    public boolean isLast() {
        if(isRootNode()) {
            return true;
        }

        return neighbors().get(neighborsCnt() - 1) == this;
    }

    /**
     * Makes the all the coordinates relative to the parent
     * Called on all the descendant nodes
     * @param isOrigin if is true then the relative root will have (0, 0) coordinate
     */
    public void makeCoordsRelative(boolean isOrigin) {
        Point rel = getNodePos();
        getNodes(NodeSide.RIGHT).forEach(node -> {
            node.makeCoordsRelative(false);
        });
        getNodes(NodeSide.LEFT).forEach(node -> {
            node.makeCoordsRelative(false);
        });
        if (!isOrigin) {
            this.setNodePos(computeRelativePosition());
        } else {
            this.setNodePos(new Point(0, 0));
        }
    }

    /**
     * Restores absolute coordinates. Typically called after makeCoordsRelative()
     * Called on descendant nodes
     */
    public void makeCoordsAbs() {
        int nx = getX() + getParent().getX();
        int ny = getY() + getParent().getY();
        this.setNodePos(nx, ny);
        getNodes(NodeSide.RIGHT).forEach( node -> node.makeCoordsAbs());
        getNodes(NodeSide.LEFT).forEach( node -> node.makeCoordsAbs());
    }

    @Deprecated
    public void inverseLeaningIfRelativeCoords() {
        ArrayList<NodeModel> temp = leftNodes;
        leftNodes = rightNodes;
        rightNodes = temp;
        this.setNodeSide(getNodeSide().inverse());
        int dx = getX() - getParent().getWidth() + getWidth();
        setNodePos(-dx, getY());
    }

    private void setNodePos(int nx, int ny) {
        getMutNodePos().setLocation(nx, ny);
    }

    @Deprecated
    public NodeModel computeSmallerUpperBranchNode(NodeSide side) {
        if(isRootNode()) {
            return null;
        }

        NodeModel model = getParent();
        while (model.getParent() != null && model.prevNode() == null) {
            model = model.getParent();
        }

        model = model.prevNode();
        NodeModel lowest = null;
        while (model != null) {

            if(model.getBottom() > this.getMutNodePos().getY()) { // TODO: opt memory - cache pos
                lowest = model;
                return lowest;
            }
            model = model.lastModel(side);
        }

        return null;
    }


    public NodeModel nextNode() {
        if(isRootNode()) {
            return null;
        }

        int ind = index();
        if(ind < neighborsCnt() - 1) {
            return neighbors().get(ind + 1);
        }

        return null;
    }

    public boolean isFirst() {
        if(isRootNode())
            return false;

        return neighbors().get(0) == this;
    }

    public int computeTotalInset() {
        return getProps().getThickness() + getProps().getInnerMargin();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }


    public int getBottom() {
        return pos.y + height;
    }

    public int getY() {
        return pos.y;
    }

    public int getX() {
        return pos.x;
    }

    public ArrayList<NodeModel> getRightNodes() {
        return rightNodes;
    }

    public void setRightNodes(ArrayList<NodeModel> rightNodes) {
        this.rightNodes = rightNodes;
    }

    public void setLeftNodes(ArrayList<NodeModel> leftNodes) {
        this.leftNodes = leftNodes;
    }

    public ArrayList<NodeModel> getLeftNodes() {
        return leftNodes;
    }

    public NodeSide getNodeSide() {
        return nodeSide;
    }

    public void setNodeSide(NodeSide nodeSide) {
        this.nodeSide = nodeSide;
    }

    public NodeStylesheet getProps() {
        return props;
    }

    public void setProps(NodeStylesheet props) {
        this.props = props;
    }

    public Font getCachedFont() {
        return cachedFont;
    }

    public ArrayList<ChangeListener> getChangeListeners() {
        return changeListeners;
    }

    @Override
    public Iterator<NodeModel> iterator() {
        ConcatIter<NodeModel> it = new ConcatIter<NodeModel>(iterator(NodeSide.RIGHT), iterator(NodeSide.LEFT));
        it.getSecond().next();
        return it;
    }

    public void addNode(NodeModel model) {
        addNode(model, this.getNodeSide());
    }

    public boolean isLeft() {
        return getNodeSide() == NodeSide.LEFT;
    }

    public boolean isRight() {
        return getNodeSide() == NodeSide.RIGHT;
    }

    public void removeFromParent() {
        NodeModel p = getParent();
        if(p != null)
            p.getNodes(this.getNodeSide()).remove(this);
    }

    public ArrayList<NodeModel> getFixedNodes() {
        ArrayList<NodeModel> nodes = new ArrayList<>();
        nodes.ensureCapacity(getRightNodes().size() + getLeftNodes().size());
        nodes.addAll(getRightNodes());
        nodes.addAll(getLeftNodes());
        return nodes;

    }

    public Dimension getSize() {
        return new Dimension(width, height);
    }

    public void setSize(int width, int height) {
        fireBeforeChangeEvent();

        this.width = (int) width;
        this.height = (int) height;

        fireChangeEvent(ChangeEvent.Cause.BOUNDS);
    }


    public void setSize(Dimension size) {
        setSize((int) size.getWidth(), (int) size.getHeight());
    }

    public boolean isAutoResizeEnabled() {
        return autoResize;
    }

    public void setAutoResize(boolean autoResize) {
        this.autoResize = autoResize;
    }

    public String getImagePath() {
        return imagePath;
    }

    public BufferedImage getImage() {
        return Resources.getInstance().getImage(imagePath, true);
    }

    public void setImagePath(String imagePath, boolean updateSize) {
        fireBeforeChangeEvent();

        this.imagePath = imagePath;
        if(updateSize) {
            Resources.getInstance().loadImage(getImagePath());
            int oldWidth = getWidth();
            int oldHeight = getHeight();

            updatePreferredSizeAndPush(true);
            pushNodes(oldWidth, oldHeight);
        }


    }

    public void setImagePath(String imagePath) {
        setImagePath(imagePath, false);
    }

    public boolean isTextNode() {
        return !isImageNode();
    }

    public void fix() {
        fix0(NodeSide.LEFT);
        fix0(NodeSide.RIGHT);
    }


    public interface BeforeChangeListener {
        void beforeChange(NodeModel model);
    }

    public static class ChangeEvent extends javax.swing.event.ChangeEvent {
        private final Cause cause;
        private final Object data;

        public enum Cause {
            TITLE,
            CONTENT,
            BOUNDS,
            IMAGE
        }
        /**
         * Constructs a ChangeEvent object.
         *
         * @param source the Object that is the source of the event
         *               (typically <code>this</code>)
         */
        public ChangeEvent(NodeModel source, Cause cause, Object data) {
            super(source);
            this.cause = cause;
            this.data = data;
        }

        public Cause getCause() {
            return cause;
        }

        public Object getData() {
            return data;
        }

        @Override
        public NodeModel getSource() {
            return (NodeModel) super.getSource();
        }
    }


    private class NodeModelIterator implements Iterator<NodeModel> {
        private final NodeSide side;
        Stack<NodeModel> nodes = new Stack<>();

        public NodeModelIterator(NodeSide side) {
            this.side = side;
            nodes.push(NodeModel.this);
        }

        @Override
        public boolean hasNext() {
            return !nodes.empty();
        }

        @Override
        public NodeModel next() {
            if(nodes.empty()) {
                throw new IndexOutOfBoundsException("Stack underflow");
            }

            NodeModel ret = nodes.pop();

            for (int i = ret.getNodes(side).size() - 1; i >= 0; i--) {
                nodes.push(ret.getNodes(side).get(i));
            }

            return ret;
        }
    }


    public enum NodeSide {
        LEFT,
        RIGHT,
        ROOT;

        public NodeSide inverse() {
            switch (this) {
                case LEFT:
                    return RIGHT;
                case RIGHT:
                    return LEFT;
                case ROOT:
                    return ROOT;
            }
            return ROOT;
        }
    }
}
