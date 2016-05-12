package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.customui.controllers.MindMapController;
import com.gmail.at.ivanehreshi.customui.controllers.ResizeController;
import com.gmail.at.ivanehreshi.models.TableNodeModel;
import com.gmail.at.ivanehreshi.utils.FunctionalMouseAdapter;

import javax.swing.*;
import java.awt.*;

public class TableNodeView extends NodeView {

    private JTable table;
    private int lastSelectedColumn = 0;
    private int lastSelectedRow = 0;

    public TableNodeView(TableNodeModel model, MindMapController mindMapController) {
        super(model, mindMapController);

        ((ResizableBorder) getBorder()).setThickness(10);

        setLayout(null);
        table = new JTable(model);
        add(table);
//        table.setSize(40, 40);
//        table.setLocation(3, 3);
        table.setVisible(true);
        table.setCellSelectionEnabled(true);

        setUpTableControllers();
    }

    private void setUpTableControllers() {
        table.addMouseListener(new FunctionalMouseAdapter()
                 .setMouseClickedHandler(e -> {
                     e.setSource(TableNodeView.this);
//                     TableNodeView.this.processMouseEvent(e);
                 })
        );

        table.getColumnModel().getSelectionModel().addListSelectionListener(
                e -> TableNodeView.this.updateLastSelectedCellPos()
        );

        table.getSelectionModel().addListSelectionListener(
                e -> TableNodeView.this.updateLastSelectedCellPos()
        );
    }
    protected void updateLastSelectedCellPos() {
        lastSelectedColumn = getTable().getSelectedColumn();
        lastSelectedRow = getTable().getSelectedRow();
    }

    @Override
    protected void drawContent(Graphics2D g2d) {

    }

    public JTable getTable() {
        return table;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if(getTable() != null) {
            Rectangle area = contentArea();
            getTable().setBounds(area);
        }
    }

    public int getLastSelectedColumn() {
        return lastSelectedColumn;
    }

    public int getLastSelectedRow() {
        return lastSelectedRow;
    }
}
