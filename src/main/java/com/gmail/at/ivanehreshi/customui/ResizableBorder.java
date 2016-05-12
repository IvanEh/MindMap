package com.gmail.at.ivanehreshi.customui;

import com.gmail.at.ivanehreshi.utils.Selectable;
import org.w3c.dom.css.Rect;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ResizableBorder implements Border {
    private int thickness;
    private Color color;
    private Color lineColor;

    public ResizableBorder(int thickness, Color color, Color lineColor) {
        this.thickness = thickness;
        this.color = color;
        this.lineColor = lineColor;
    }

    private Rectangle getRect(int width, int height, int location) {
        switch (location) {
            case SwingConstants.NORTH_WEST:
                return new Rectangle(0, 0,thickness, thickness);
            case SwingConstants.NORTH_EAST:
                return new Rectangle(width - thickness, 0,
                        thickness, thickness);
            case SwingConstants.SOUTH_WEST:
                return new Rectangle(0, height-thickness,
                        thickness, thickness);
            case SwingConstants.SOUTH_EAST:
                return new Rectangle(width - thickness, height - thickness,
                        thickness, thickness);
        }
        return null;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (c instanceof Selectable) {
            Selectable selectable = (Selectable) c;
            if(!selectable.isSelected())
                return;
        }

        Graphics2D g2d = (Graphics2D) g;

        Rectangle rect;

        g2d.setColor(lineColor);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(x, y + thickness/2, width, y + thickness/2);
        g2d.drawLine(x, y + height - thickness/2, width, y +height - thickness/2);
        g2d.drawLine(x + thickness/2, y, x + thickness/2, height);
        g2d.drawLine(x + width - thickness/2, y, x + width - thickness/2, height);

        g2d.setColor(color);
        rect = getRect(width, height, SwingConstants.NORTH_WEST);
        g2d.fillRect(x + rect.x, y + rect.y, rect.width, rect.height);
        rect = getRect(width, height, SwingConstants.NORTH_EAST);
        g2d.fillRect(x + rect.x, y + rect.y, rect.width, rect.height);
        rect = getRect(width, height, SwingConstants.SOUTH_EAST);
        g2d.fillRect(x + rect.x, y + rect.y, rect.width, rect.height);
        rect = getRect(width, height, SwingConstants.SOUTH_WEST);
        g2d.fillRect(x + rect.x, y + rect.y, rect.width, rect.height);


    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public int getThickness() {
        return thickness;
    }
}
