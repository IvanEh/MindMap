package com.gmail.at.ivanehreshi.models;

import com.gmail.at.ivanehreshi.customui.NodeStylesheet;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

public class NodeModel implements Iterable<NodeModel>{
    private String title;
    private String content;
    private ArrayList<NodeModel> nodes;
    private ArrayList<ChangeListener> changeListeners;
    private NodeModel parentNode;
    private NodeStylesheet props;
    private Point pos;
    private int height = 20;
    private int width = 50;

    public NodeModel(String title) {
        this.title = title;
        this.content = "";
        nodes = new ArrayList<>();
        changeListeners = new ArrayList<>();
        pos = new Point();
        props = new NodeStylesheet();
    }

    public NodeModel(String title, NodeModel parentNode) {
        this(title);
        this.parentNode = parentNode;
    }

    public NodeModel lastModel() {
        if(nodes.size() == 0)
            return null;

        return nodes.get(nodes.size() - 1);
    }

    public NodeModel firstModel() {
        if(nodes.size() == 0)
            return null;

        return nodes.get(0);
    }

    public NodeModel translateRel(int dx, int dy) {
        this.translateAbs(dx, dy);
        if(!isLast()) {
            parentNode.nodes.subList(index() + 1, neighborsCnt())
                      .forEach(node -> node.translateAbs(0, dy));
        }

        return this;
    }

    public int neighborsCnt() {
        return parentNode.getNodes().size();
    }

    public NodeModel translateAbs(int dx, int dy) {
        pos.translate(dx, dy);
        nodes.forEach(node -> node.translateAbs(dx, dy));

        return this;
    }

    public NodeModel translateAbsWithAlignment(int dx, int dy) {
        this.translateAbs(dx, dy);
        if(dy < 0) {
            this.fixUp();
        } else {
            this.fixDown();
        }
        return this;
    }

    public NodeModel translateRelWithAlignment(int dx, int dy) {
        this.translateRel(dx, dy);
        if(dy < 0) {
            this.fixUp();
        } else {
            this.fixDown();
        }
        return this;

    }

    public NodeModel translateUpperNodes(int dx,int dy) {
        int ind = index();
        this.translateAbs(dx, dy);
        parentNode.nodes.subList(0, ind).forEach((node) -> node.translateAbs(0, dy));
        return this;
    }

    public NodeModel translateLowerNodes(int dx, int dy) {
        int ind = index();
        int len = parentNode.nodes.size();

        this.translateAbs(dx, dy);
        parentNode.nodes.subList(ind + 1, len).forEach((node) -> node.translateAbs(0, dy));
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

    public NodeModel fixUp() {
        NodeModel upBranch = this.findPrevBranch();
        if(upBranch == null) {
            return this;
        }

        NodeModel lowest = upBranch.findLowest();
        NodeModel highest = this.findHighest();

        int correction = lowest.getBottom() - highest.getY();
        if(correction > 0) {
            upBranch.translateUpperNodes(0, -correction);
            upBranch.getParent().firstModel().fixUp();
        }

        return this;
    }

    public NodeModel fixDown() {
        NodeModel nextBranch = findNextBranch();
        if(nextBranch == null)
            return this;

        NodeModel lowest = this.findLowest();
        NodeModel highest = nextBranch.findHighest();

        int correction = lowest.getBottom() - highest.getY();
        if(correction > 0) {
            nextBranch.translateLowerNodes(0, correction);
            nextBranch.getParent().lastModel().fixDown();
        }

        return this;
    }

    public NodeModel fix0() {
        fixUp();
        return fixDown();
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

    public NodeModel findLowest() {
        NodeModel lowest = this;
        NodeModel curr = this;
        while (curr != null) {
            if(curr.getY() > lowest.getY()) {
                lowest = curr;
            }
            curr = curr.lastModel();
        }

        return lowest;
    }

    public NodeModel findHighest() {
        NodeModel highest = this;
        NodeModel curr = this;
        while (curr != null) {
            if(curr.getY() < highest.getY()) {
                highest = curr;
            }
            curr = curr.firstModel();
        }

        return highest;
    }

    public Rectangle computeNodeInvariantBounds() {
        NodeModel highest = findHighest();
        NodeModel lowest = findLowest();
        return new Rectangle(highest.getX(), highest.getY(),
                0, lowest.getBottom() - highest.getY());
    }

    public boolean isRelative() {
        return getParent().firstModel() != this;
    }

    /**
     * Adds the NodeModel at the given relative position
     * @param nodeModel
     * @param dx
     * @param dy
     */
    public void addNodeAtRelPos(NodeModel nodeModel, int dx, int dy) {
        Point pos = new Point(getNodePos());
        pos.translate(dx, dy);
        nodeModel.setNodePos(pos);
        nodes.add(nodeModel);
        nodeModel.parentNode = this;
    }

    public void addNode(NodeModel model) {
        nodes.add(model);
        model.parentNode = this;
    }

    // TODO: fix typo
    public boolean hasChilds() {
        return !nodes.isEmpty();
    }

    @Deprecated
    public boolean moveUp() {
        int index = parentNode.nodes.indexOf(this);
        if(index > 0) {
            Collections.swap(nodes, index, index-1);
            return true;
        }
        return false;
    }

    public int index() {
        if(isRootNode()) {
            return 0;
        }
        return parentNode.nodes.indexOf(this);
    }

    public NodeModel prevNode() {
        int ind = index();
        if(ind > 0) {
            return parentNode.nodes.get(ind-1);
        }

        return null;
    }

    public boolean swapDown() {
        int index = parentNode.nodes.indexOf(this);
        if(index != -1 && index != neighborsCnt()-1) {
            NodeModel currNode = this;
            NodeModel nextNode = parentNode.nodes.get(index + 1);

            Rectangle currRect = currNode.computeNodeInvariantBounds();
            Rectangle nextRect = nextNode.computeNodeInvariantBounds();

            int currCorrection = (int) (nextRect.getMaxY() - currRect.getMaxY());
            int nextCorrection = (int) (nextRect.getY() - currRect.getY());

            currNode.translateAbs(0, currCorrection);
            nextNode.translateAbs(0, -nextCorrection);

            Collections.swap(getParent().nodes, index, index+1);
            return true;
        }
        return false;
    }

    public boolean swapUp() {
        int index = parentNode.nodes.indexOf(this);
        if(index != -1 && index != 0) {
            NodeModel prevNode = parentNode.nodes.get(index - 1);
            return prevNode.swapDown();
        }
        return false;
    }

    public void addChangeListener(ChangeListener l) {
        changeListeners.add(l);
    };

    public void removeChangeListener(ChangeListener l) {
        changeListeners.remove(l);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Object getFormattedTitle() {
        throw new NotImplementedException();
    }

    protected void fireChangeEvent() {
        ChangeEvent event = new ChangeEvent(this);
        fireChangeEvent(event);
    }

    public ArrayList<NodeModel> getNodes() {
        return nodes;
    }

    public Point getPosition() {
        return new Point((int) pos.x, (int) pos.y);
    }

    protected void fireChangeEvent(ChangeEvent event) {
        changeListeners.forEach((listener) -> listener.stateChanged(event) );
    }

    public Point getNodePos() {
        return pos;
    }

    public void setNodePos(Point nodePos) {
        this.pos = new Point(nodePos);
    }

    // TODO: optimize
    public int getIndex() {
        if (parentNode == null) {
            return 0;
        }

        return parentNode.getNodes().indexOf(this);
    }

    public boolean isRootNode() {
        return parentNode == null;
    }

    public NodeModelIterator iterator() {
        return new NodeModelIterator();
    }

    public NodeModel getParent() {
        return parentNode;
    }

    public NodeModel getPrev() {
        if(isRootNode()) {
            return null;
        }

        int index = getIndex();
        if(index != 0) {
            return getParent().getNodes().get(index - 1);
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

        return getParent().lastModel() == this;
    }

    public NodeModel computeSmallerUpperBranchNode() {
        if(isRootNode()) {
            return null;
        }

        NodeModel model = getParent();
        while (model.getParent() != null && model.prevNode() == null) {
            model = model.getParent();
        }

        model = model.prevNode();
        NodeModel lowest = null;
        // TODO: has childs redendant
        while (model != null) {

            if(model.getBottom() > this.getNodePos().getY()) { // TODO: opt memory - cache pos
                lowest = model;
                return lowest;
            }
            model = model.lastModel();
        }

        return null;
    }


    public NodeModel nextNode() {
        if(isRootNode()) {
            return null;
        }

        int ind = index();
        if(ind < parentNode.getNodes().size()-1) {
            return parentNode.getNodes().get(ind + 1);
        }

        return null;
    }

    public boolean isFirst() {
        if(isRootNode())
            return false;

        return getParent().firstModel() == this;
    }

    public NodeModel computeFirstSmallestUpperLeaning() {
        if(isRootNode()) {
            return null;
        }

        NodeModel model = this;
        NodeModel from = this;
        while (model.getParent() != null && model.prevNode() == null) {
            model = model.getParent();
        }

        model = model.prevNode();
        NodeModel shortest = model;
        while (model != null && model.hasChilds()) {
            if(model.getNodePos().getY() < shortest.getNodePos().getY() || shortest.isRootNode()) { // TODO: opt memory - cache pos
                shortest = model;
            }
            model = model.lastModel();
        }

        return shortest;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /**
     *
     * @param dy > 0
     */
    public void translateUp(int dy) {
        if(isFirst()) {
            translateAbs(0, -dy);
            NodeModel smallest;
//            do {
                smallest = computeSmallerUpperBranchNode();
                if (smallest != null) {
                    int correction = (int) (smallest.getBottom() - getNodePos().getY());
                    smallest.getParent().translateUpRel(correction);
                    correction++;
                }
//            } while(smallest != null);
        } else {
            translateAbs(0, -dy);
            NodeModel prev = computeSmallerUpperBranchNode();
            if(prev != null) {
                int correction = (int) (prev.getBottom() - getNodePos().getY());
                if(correction >= 0) {
                    prev.translateUpRel(correction);
                }
            } else {
                prevNode().translateUp(dy);
            }
        }
    }

    public void translateUpRel(int dy) {
        if(isFirst()) {
            translateUp(dy);
        } else {
            translateAbs(0, -dy);
            prevNode().translateUpRel(dy);
        }
    }

    public void translateDownRes(int dy) {
        if(isLast()) {
            translateDown(dy);
        } else {
            translateAbs(0, dy);
            nextNode().translateDown(dy);
        }
    }

    public void translateDown(int dy) {
        if(isLast()) {
            translateAbs(0, dy);
            NodeModel highest;
            do {
                highest = computeHigherLowerBranchNode();
                if (highest != null) {
                    int correction = (int) (this.getBottom() - highest.getNodePos().getY());
                    highest.translateDown(correction);
                }
            }while (highest != null);
        } else {
            translateAbs(0, dy);
            int correction = (int) (getBottom() - nextNode().getNodePos().getY());
            if(correction >= 0) {
                nextNode().translateDownRes(correction);
            }
        }

    }

    public void fix() {
        translateUp(0);
        translateDown(0);
    }

    private NodeModel computeHigherLowerBranchNode() {
        if(isRootNode()) {
            return null;
        }

        NodeModel model = getParent();
        while (model.getParent() != null && model.nextNode() == null) {
            model = model.getParent();
        }

        model = model.nextNode();
        NodeModel lowest = null;
        // TODO: has childs redendant
        while (model != null) {

            if(this.getBottom() > model.getNodePos().getY() ) { // TODO: opt memory - cache pos
                lowest = model;
                return lowest;
            }
            model = model.firstModel();
        }

        return null;

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

    public NodeStylesheet getProps() {
        return props;
    }

    public void setProps(NodeStylesheet props) {
        this.props = props;
    }

    public static class NodeModelChangeEvent extends ChangeEvent {
        private final Cause cause;
        private final Object data;

        public enum Cause {
            TEXT,
            CONTENT,
            SIZE
        }
        /**
         * Constructs a ChangeEvent object.
         *
         * @param source the Object that is the source of the event
         *               (typically <code>this</code>)
         */
        public NodeModelChangeEvent(Object source, Cause cause, Object data) {
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
    }

    private class NodeModelIterator implements Iterator<NodeModel> {
        Stack<NodeModel> nodes = new Stack<>();

        public NodeModelIterator() {
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

            for (int i = ret.nodes.size() - 1; i >= 0; i--) {
                nodes.push(ret.nodes.get(i));
            }

            return ret;
        }
    }

}
