package com.gmail.at.ivanehreshi.models;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;
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

    public NodeModel(String title) {
        this.title = title;
        this.content = "";
        nodes = new ArrayList<>();
        changeListeners = new ArrayList<>();
    }

    public NodeModel(String title, NodeModel parentNode) {
        this(title);
        this.parentNode = parentNode;
    }

    public void addNode(NodeModel nodeModel) {
        nodes.add(nodeModel);
    }

    public boolean moveUp() {
        int index = parentNode.nodes.indexOf(this);
        if(index > 0) {
            Collections.swap(nodes, index, index-1);
            return true;
        }
        return false;
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

    protected void fireChangeEvent(ChangeEvent event) {
        changeListeners.forEach((listener) -> listener.stateChanged(event) );
    }

    public NodeModelIterator iterator() {
        return new NodeModelIterator();
    }

    public static class NodeModelChangeEvent extends ChangeEvent {
        private final Cause cause;
        private final Object data;

        public enum Cause {
            TEXT,
            CONTENT
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
            for(NodeModel node: ret.getNodes()) {
                nodes.push(node);
            }
            return ret;
        }
    }
}
