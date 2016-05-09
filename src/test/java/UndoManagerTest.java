import com.gmail.at.ivanehreshi.actions.UndoableCommand;
import com.gmail.at.ivanehreshi.utils.UndoManager;
import org.junit.Test;
import static org.junit.Assert.*;

public class UndoManagerTest {

    @Test
    public void testIfEmpty() {
        UndoManager undoManager = new UndoManager(3);
        assertTrue(undoManager.isEmpty());

        undoManager.redo(new MockUndoableCommand());
        assertFalse(undoManager.isEmpty());
        assertFalse(undoManager.isFull());
    }

    @Test
    public void testIfFull() {
        UndoManager undoManager = new UndoManager(2);
        undoManager.redo(new MockUndoableCommand());
        undoManager.redo(new MockUndoableCommand());
        assertTrue(undoManager.isFull());
        assertFalse(undoManager.isEmpty());

        undoManager.undo();
        assertFalse(undoManager.isFull());
        assertFalse(undoManager.isEmpty());

        undoManager.undo();
        assertFalse(undoManager.isFull());
        assertTrue(undoManager.isEmpty());
    }

    @Test
    public void testIfOverflow() {
        UndoManager undoManager = new UndoManager(3);
        undoManager.redo(new MockUndoableCommand(0));
        undoManager.redo(new MockUndoableCommand(1));
        undoManager.redo(new MockUndoableCommand(2));
        undoManager.redo(new MockUndoableCommand(3));
        undoManager.redo(new MockUndoableCommand(4));


        assertTrue(undoManager.isFull());
        assertFalse(undoManager.isEmpty());

        assertEquals(4, ((MockUndoableCommand) undoManager.undo()).id);
        assertEquals(3, ((MockUndoableCommand) undoManager.undo()).id);
        assertEquals(2, ((MockUndoableCommand) undoManager.undo()).id);

        assertTrue(undoManager.isEmpty());
        assertFalse(undoManager.isFull());

        undoManager.redo(new MockUndoableCommand(5));
        undoManager.redo(new MockUndoableCommand(6));
        undoManager.redo(new MockUndoableCommand(7));

        assertEquals(7, ((MockUndoableCommand) undoManager.undo()).id);
        assertEquals(6, ((MockUndoableCommand) undoManager.undo()).id);
        assertEquals(5, ((MockUndoableCommand) undoManager.undo()).id);
    }

    static class MockUndoableCommand implements UndoableCommand{
        public int id = 0;

        public MockUndoableCommand(int id) {
            this.id = id;
        }

        public MockUndoableCommand() {
        }

        @Override
        public void undo() {        }
        @Override
        public void redo() {    }
    }
}
