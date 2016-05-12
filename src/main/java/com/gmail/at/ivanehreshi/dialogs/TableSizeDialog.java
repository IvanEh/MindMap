package com.gmail.at.ivanehreshi.dialogs;

import javax.swing.*;

public class TableSizeDialog{

    public static Inputs showDialog(int defaultColumns, int defaultRows) {
        JSlider columnsTextField = new JSlider(1, 10, defaultColumns);
        JSlider rowsTextField = new JSlider(1, 10, defaultRows);

        JComponent[] components = {
                new JLabel("Колонки"),
                columnsTextField,
                new JLabel("Рядки"),
                rowsTextField
        };

        JOptionPane.showMessageDialog(null, components, "", JOptionPane.PLAIN_MESSAGE);

        return new Inputs(columnsTextField, rowsTextField);
    }

    public static class Inputs {
        public JSlider columnsSlider;
        public JSlider rowsSlider;

        public Inputs(JSlider columnsSlider, JSlider rowsSlider) {
            this.columnsSlider = columnsSlider;
            this.rowsSlider = rowsSlider;
        }
    }
}
