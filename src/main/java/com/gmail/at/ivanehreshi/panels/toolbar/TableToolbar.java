package com.gmail.at.ivanehreshi.panels.toolbar;

import com.gmail.at.ivanehreshi.actions.mindmap.AddColumn;
import com.gmail.at.ivanehreshi.actions.mindmap.AddRow;
import com.gmail.at.ivanehreshi.actions.mindmap.TableRemove;
import com.gmail.at.ivanehreshi.customui.MindMapDrawer;

import javax.swing.*;
import java.awt.*;

public class TableToolbar extends JToolBar {
    private JButton addColumnLeft;
    private MindMapDrawer mindMapDrawer;
    private JButton addColumnRightBtn;
    private JButton addRowTopBtn;
    private JButton addRowBottomBtn;
    private JButton removeColumnBtn;
    private JButton removeRowBtn;

    public TableToolbar(MindMapDrawer mindMapDrawer) {
        super();
        this.mindMapDrawer = mindMapDrawer;

        setLayout(new FlowLayout());
        createGui();
    }

    private void createGui() {
        addColumnLeft = new JButton(new AddColumn(mindMapDrawer, AddColumn.Side.LEFT));
        add(addColumnLeft);

        addColumnRightBtn = new JButton(new AddColumn(mindMapDrawer, AddColumn.Side.RIGHT));
        add(addColumnRightBtn);

        addRowTopBtn = new JButton(new AddRow(mindMapDrawer, AddRow.Side.TOP));
        add(addRowTopBtn);

        addRowBottomBtn = new JButton(new AddRow(mindMapDrawer, AddRow.Side.BOTTOM));
        add(addRowBottomBtn);

        removeColumnBtn = new JButton(new TableRemove(mindMapDrawer, TableRemove.What.COLUMN));
        add(removeColumnBtn);

        removeRowBtn = new JButton(new TableRemove(mindMapDrawer, TableRemove.What.ROW));
        add(removeRowBtn);
    }



}
