package com.gmail.at.ivanehreshi.models;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class TableNodeModel extends NodeModel implements TableModel {
    private int rowCount;
    private int columnCount;
    private ArrayList<ArrayList<String>> columns;
    private AbstractTableModel abstractTableModel;
    public static final int COL_PREF_SIZE = 55;

    public TableNodeModel(NodeModel parentNode, NodeSide side, int rowCount, int columnCount) {
        super("", parentNode, side);
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        columns = new ArrayList<>();
        for (int i = 0; i < getColumnCount(); i++) {
            ArrayList<String> column = new ArrayList<>();
            for(int j = 0; j < getRowCount(); j++) {
                column.add("");
            }
            columns.add(column);
        }
        abstractTableModel = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return TableNodeModel.this.getRowCount();
            }

            @Override
            public int getColumnCount() {
                return TableNodeModel.this.getColumnCount();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return TableNodeModel.this.getValueAt(rowIndex, columnIndex);
            }
        };


        updateModelPreferredSize(true);
    }

    public ArrayList<String> getColumn(int i) {
        return columns.get(i);
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return String.valueOf('A' + columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getColumn(columnIndex).get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        getColumn(columnIndex).set(rowIndex, aValue.toString());
        abstractTableModel.fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        abstractTableModel.addTableModelListener(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        abstractTableModel.removeTableModelListener(l);
    }

    @Override
    public void updateModelPreferredSize(boolean force) {
        if(!isAutoResizeEnabled() && !force)
            return;

        setSize(getColumnCount()*COL_PREF_SIZE, getRowCount()*COL_PREF_SIZE);
    }

    public void insertColumn(int i, ArrayList<String> rowData) {
        ArrayList<String> column = new ArrayList<>();
        if(rowData == null) {
            for (int j = 0; j < getRowCount(); j++) {
                column.add("");
            }
        } else {
            column.addAll(rowData);
        }

        columns.add(i, column);
        columnCount++;

        abstractTableModel.fireTableStructureChanged();
    }

    public ArrayList<String> removeColumn(int before) {
        ArrayList<String> removed = columns.remove(before);
        columnCount--;
        abstractTableModel.fireTableStructureChanged();
        return removed;
    }

    public ArrayList<String> removeRow(int index) {
        ArrayList<String> removed = new ArrayList<>();
        for (int i = 0; i < getColumnCount(); i++) {
            removed.add(getColumn(i).remove(index));
        }
        rowCount--;

        abstractTableModel.fireTableStructureChanged();

        return removed;
    }

    public void insertRow(int before, ArrayList<String> rowData) {
        ArrayList<String> row = new ArrayList<>();
        if(rowData != null) {
            row.addAll(rowData);
        }
        for(int i = row.size() - 1; i < getRowCount() + 1; i++)
            row.add("");

        for(int i = 0; i < getColumnCount(); i++) {
            getColumn(i).add(before, row.get(i));
        }
        rowCount++;

        abstractTableModel.fireTableStructureChanged();
    }
}
