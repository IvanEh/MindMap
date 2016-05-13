package com.gmail.at.ivanehreshi.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface UndoableCommand extends Command {
    void undo();

    public static UndoableCommand createMacro(List<UndoableCommand> commands) {
        return new UndoableCommand() {
            private final List<UndoableCommand> undoableCommands
                    = commands;

            @Override
            public void undo() {
                undoableCommands.forEach(command -> command.undo());
            }

            @Override
            public void redo() {
                undoableCommands.forEach(command -> command.redo());
            }
        };
    }
}
