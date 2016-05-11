package com.gmail.at.ivanehreshi.utils;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;

public class HtmlDrawer {
    private JLabel label;

    public HtmlDrawer() {
        label = new JLabel();
    }

    public void drawHtml(Graphics g, String text, int x, int y) {
        label.setText("<html><font size=12pt>" + text + "</font></html>");
        label.setSize(label.getPreferredSize());
        g.translate(x, y - label.getHeight());
        label.paint(g);
        g.translate(-x, -y + label.getHeight());
    }

    public Dimension computeTextSize(String text) {
        label.setText(text);
        return label.getPreferredSize();
    }

    public Dimension preferredSize(String text) {
        label.setText(text);
        return label.getPreferredSize();
    }


}
