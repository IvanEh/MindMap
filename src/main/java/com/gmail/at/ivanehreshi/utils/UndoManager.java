package com.gmail.at.ivanehreshi.utils;

import com.gmail.at.ivanehreshi.actions.Command;
import com.gmail.at.ivanehreshi.actions.UndoableCommand;

public class UndoManager {
    private final int size;
    private final UndoableCommand[] deque;
    private int tail;
    private int free;
    private int curr;
    private boolean full = false;

    public UndoManager(int size) {
        this.size = size;
        this.deque = new UndoableCommand[this.size];
        tail = 0;
        free = 0;
        curr = 0;
    }

    public void redo(UndoableCommand command) {
        command.redo();
        if(isFull()) {
            tail = normIndex(tail + 1);
        }
        deque[free] = command;
        curr = free;

        free = normIndex(free + 1);
        if(isEmpty()) {
            full = true;
        }
    }

    public UndoableCommand undo() {
        if(isEmpty()) {
            return null;
        }
        UndoableCommand command = deque[curr];
        command.undo();

        free = curr;
        curr = normIndex(curr - 1);

        if(isFull())
            full = false;

        return command;
    }

    public boolean isFull() {
        return full;
    }

    public boolean isEmpty() {
        return tail == free && !full;
    }

    public int getSize() {
        return size;
    }

    private int normIndex(int i) {
        if(i < 0)
            return size - 1;
        if(i >= size)
            return 0;
        return i;
    }
}
