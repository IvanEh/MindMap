package com.gmail.at.ivanehreshi.models;

import com.gmail.at.ivanehreshi.utils.Vector2D;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.soap.Node;
import java.awt.*;
import java.awt.geom.Point2D;
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
    private Point nodePos;

    public NodeModel(String title) {
        this.title = title;
        this.content = "";
        nodes = new ArrayList<>();
        changeListeners = new ArrayList<>();
        nodePos = new Point();
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

    public NodeModel translate(int dx, int dy) {
        nodePos.translate(dx, dy);
        return this;
    }

    public NodeModel translateAbs(int dx, int dy) {
        nodePos.translate(dx, dy);
        if(!isLast() && dy < 0) {
            nextNode().translate(0, -dy);
        }

        return this;
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
    public void addNodeAtPos(NodeModel nodeModel, int dx, int dy) {
        nodeModel.setNodePos(new Point(dx, dy));
        nodes.add(nodeModel);
        nodeModel.parentNode = this;
    }

    public boolean hasChilds() {
        return !nodes.isEmpty();
    }

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

    public boolean moveDown() {
        int index = parentNode.nodes.indexOf(this);
        if(index != -1 && index != nodes.size()-1) {
            Collections.swap(nodes, index, index+1);
            return true;
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
        return new Point((int) nodePos.x, (int) nodePos.y);
    }

    protected void fireChangeEvent(ChangeEvent event) {
        changeListeners.forEach((listener) -> listener.stateChanged(event) );
    }

    public Point getNodePos() {
        return nodePos;
    }

    public void setNodePos(Point nodePos) {
        this.nodePos = nodePos;
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

    public void setPolarCoord(double rho, double phi) {

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

    public NodeModel computeFirstLargestLeaning() {
        if(isRootNode()) {
            return null;
        }

        NodeModel model = this.getParent();
        NodeModel from = this;
        while (model.getParent() != null && model.nextNode() == null) {
            from = model;
            model = model.getParent();
        }

        model = model.nextNode();
        NodeModel tallest = model;
        while (model != null && model.hasChilds()) {
            if(model.getNodePos().getY() > tallest.getNodePos().getY() || tallest.isRootNode()) { // TODO: opt memory - cache pos
                tallest = model;
            }
            model = model.firstModel();
        }

        return tallest;
    }

    public Point computePosition() {
        Point p = new Point();

        NodeModel model = this;
        while (!model.isRootNode()) {
            p.x += model.nodePos.x;
            p.y += model.nodePos.y;
            if(model.isRelative()) {
                model = model.prevNode();
            }else {
                model = model.getParent();
            }
        }
        return p;
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

        NodeModel model = this.getParent();
        NodeModel from = this;
        while (model.getParent() != null && model.prevNode() == null) {
            from = model;
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
