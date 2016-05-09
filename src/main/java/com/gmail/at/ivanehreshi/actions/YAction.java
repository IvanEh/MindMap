package com.gmail.at.ivanehreshi.actions;

import com.gmail.at.ivanehreshi.MindMapApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This class used to represent general actions that
 * the user can do in the program. Also it manages
 * undoable command.
 * <p>
 *     The general use of this class involves overriding the
 *     actionPerformed method and calling in it super.actionPerformed
 *     with appropriate ActionEvent.
 * </p>
 * <p>
 *     Note that some command can demand derived instances of ActionEvent
 * </p>
 */
abstract public class YAction extends AbstractAction {
    public YAction(String name) {
        super(name);
    }

    public YAction(String name, Icon icon) {
        super(name, icon);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Command command = getCommand(e);
        if(command != null) {
            if (command instanceof UndoableCommand) {
                MindMapApplication.
                        getUndoManagerInstance().redo((UndoableCommand) command);
            } else {
                command.redo();
            }
        }
    }

    /**
     * A Command instance that should be executed
     * Normally getCommand() returns a new instance of command at
     * every invocation of the method
     * @return  null if there is no associated command
     */
    public Command getCommand(ActionEvent event) {
        return null;
    };
}
