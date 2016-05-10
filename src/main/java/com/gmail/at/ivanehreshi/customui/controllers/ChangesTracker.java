package com.gmail.at.ivanehreshi.customui.controllers;

import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.models.NodeModel;

import java.awt.event.ActionListener;
import java.util.*;

/**
 * An abstraction that generalize and facilitate capturing the
 * partial state of the NodeModel
 * @param <P> is the unary function used to acquire the partial NodeModel state
 */
public class ChangesTracker<P> {
    private boolean track = false;
    private PropertyRetriever<P> propertyRetriever;
    private PropertyRestorer<P> propertyRestorer;
    private Map<NodeModel, Change<P>> changes;
    private ActionListener retrieveUnchangedListener;
    private ActionListener retrieveChangedListener;

    public ChangesTracker(PropertyRetriever<P> propertyRetriever, PropertyRestorer<P> propertyRestorer) {
        this.propertyRetriever = propertyRetriever;
        this.propertyRestorer = propertyRestorer;

        this.changes = new HashMap<>();
        createListeners();
    }

    private void createListeners() {
        retrieveUnchangedListener = e -> {
            if(!isTracked()) {
                return;
            }

            NodeModel model = (NodeModel) e.getSource();
            Change<P> change = getChanges().get(model);
            // TODO: use exists()
            if(change == null) {
                P prop = propertyRetriever.retrieve(model);
                getChanges().put(model, new Change<P>(model, prop));
            }

        };

        retrieveChangedListener = e -> {
            if(!isTracked()) {
                return;
            }

            NodeModel model = (NodeModel) e.getSource();
            P prop = propertyRetriever.retrieve(model);
            Change<P> change = getChanges().get(model);
            if(change == null) {
                Change<P> newChange = new Change<>(model, prop);
                changes.put(model, newChange);
            } else {
                change.after = prop;
            }
        };
    }

    public boolean isTracked() {
        return track;
    }

    public void setTracked(boolean track) {
        this.track = track;
    }

    public void startTracking() {
        setTracked(true);
    }

    public void stopTracking() {
        setTracked(false);
    }

    public void clear() {
        this.getChanges().clear();
    }

    public PropertyRetriever<P> getPropertyRetriever() {
        return propertyRetriever;
    }

    public PropertyRestorer<P> getPropertyRestorer() {
        return propertyRestorer;
    }

    public Map<NodeModel, Change<P>> getChanges() {
        return changes;
    }

    public ActionListener getRetrieveUnchangedListener() {
        return retrieveUnchangedListener;
    }

    public ActionListener getRetrieveChangedListener() {
        return retrieveChangedListener;
    }

    public UndoableCommand getUndoableCommand() {
        return new UndoableCommand() {
            final ArrayList<Change<P>> changes = new ArrayList<>(getChanges().values());

            @Override
            public void undo() {
                changes.forEach(change -> {
                    getPropertyRestorer().restore(change.source, change.before);
                });
            }

            @Override
            public void redo() {
                changes.stream()
                        .filter(ch -> ch.after != null)
                        .forEach(change -> {
                              getPropertyRestorer().restore(change.source, change.after);

                 });
            }
        };
    }

    public boolean isEmpty() {
        return changes.isEmpty();
    }

    public static class Change<P> {
        public NodeModel source = null;
        public P before = null;
        public P after = null;

        public Change(NodeModel source, P before) {
            this.source = source;
            this.before = before;
        }
    }

    public interface PropertyRetriever<P> {
        P retrieve(NodeModel model);
    }

    public interface PropertyRestorer<P> {
        void restore(NodeModel model, P prop);
    }

}
