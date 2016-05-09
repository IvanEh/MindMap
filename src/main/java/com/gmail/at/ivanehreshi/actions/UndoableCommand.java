package com.gmail.at.ivanehreshi.actions;

public interface UndoableCommand extends Command {
    void undo();
}
