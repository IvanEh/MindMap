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
        label.setText(text);
        label.setSize(label.getPreferredSize());
        g.translate(x, y - label.getHeight());
        label.paint(g);
        g.translate(-x, -y + label.getHeight());
    }

    public Dimension computeTextSize(String text) {
        label.setText(text);
        return label.getPreferredSize();
    }

    public static String encloseWithHtml(String s) {
        return "<html>" + s + "</html>";
    }

    public static String encloseWithFont(String s, int size, String face, Color color) {
        return String.format(
            "<font size=%1$spx face=%2$s color=%3$s>%4$s</font>",
                size, face, hexColor(color), s
        );
    }

    public static String hexColor(Color color) {
        return String.format("#%02x%02x%02x",
                color.getRed(), color.getGreen(), color.getBlue());
    }


}
