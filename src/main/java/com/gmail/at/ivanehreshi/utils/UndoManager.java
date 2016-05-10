package com.gmail.at.ivanehreshi.utils;

import com.gmail.at.ivanehreshi.actions.UndoableCommand;

public class UndoManager {
    private final int size;
    private final UndoableCommand[] deque;
    private int beforeTail;
    private int last;
    private int currUndo;
    private boolean full = false;
    private boolean exhausted = false;

    public UndoManager(int size) {
        this.size = size;
        this.deque = new UndoableCommand[this.size];
        beforeTail = size - 1;
        last = beforeTail;
        currUndo = beforeTail;
    }

    public void push(UndoableCommand command) {
        boolean flag = false;
        if(isFull() && currUndo == last) {
            beforeTail = normIndex(beforeTail + 1);
            flag = true;
        }

        currUndo = normIndex(currUndo + 1);
        deque[currUndo] = command;
        last = currUndo;

        if(isEmpty()) {
            full = true;
        } else if(!flag){
            full = false;
        }
        exhausted = false;
    }

    public UndoableCommand redo() {
        if(isEmpty()) {
            return null;
        }
        if(currUndo == last) {
            return null;
        }

        int currDo = normIndex(currUndo + 1);

        UndoableCommand command = deque[currDo];
        command.redo();

        currUndo = currDo;

        exhausted = false;
        return command;
    }


    // FIX: undo after last command undone
    public UndoableCommand undo() {
        if(isEmpty()) {
            return null;
        }
        if(exhausted) {
            return null;
        }
        UndoableCommand command = deque[currUndo];
        command.undo();

        currUndo = normIndex(currUndo - 1);

        if(currUndo == beforeTail) {
            exhausted = true;
        }

        return command;
    }

    public int tail() {
        return normIndex(beforeTail + 1);
    }

    public boolean isFull() {
        return full;
    }

    public boolean isEmpty() {
        return beforeTail == last && !full;
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

    public void redo(UndoableCommand command) {
        command.redo();
        push(command);
    }
}
