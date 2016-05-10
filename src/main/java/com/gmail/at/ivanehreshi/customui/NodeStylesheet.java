package com.gmail.at.ivanehreshi.customui;
import java.awt.Color;

public class NodeStylesheet extends Stylesheet{
    public static final String NODE_COLOR = "NODE_COLOR";
    public static final String THICKNESS = "THICKNESS";
    public static final String OUTER_MARGIN = "OUTER_MARGIN";
    public static final String INNER_MARGIN = "INNER_MARGIN";
    public static final String FONT_SIZE = "FONT_SIZE";
    public static final String MINIMUM_GAP = "MINIMUM_GAP";
    public static final String RECOMMENDED_LINK_LENGTH = "RECOMMENDED_LINK_LENGTH";
    public static final String FONT_NAME = "FONT_NAME";
    public static final String MINIMUM_HEIGHT = "MINIMUM_HEIGHT";
    public static final String MINIMUM_WIDTH = "MINIMUM_WIDTH";
    public static final String FOCUSED_MARKER_COLOR = "FOCUSED_MARKER_COLOR";

    {
        put(NODE_COLOR, new Color(84, 51, 116));
        put(THICKNESS, 3);
        put(OUTER_MARGIN, 0);
        put(INNER_MARGIN, 3);
        put(FONT_SIZE, 12);
        put(MINIMUM_GAP, 5);
        put(RECOMMENDED_LINK_LENGTH, 15);
        put(FONT_NAME, "Ubuntu");
        put(MINIMUM_HEIGHT, 25);
        put(MINIMUM_WIDTH, 50);
        put(FOCUSED_MARKER_COLOR, new Color(4, 111, 12));
    }

    public Color getNodeColor() {
        return getColor(NODE_COLOR);
    }

    public int getThickness() {
        return getInteger(THICKNESS);
    }

    public int getOuterMargin() {
        return getInteger(OUTER_MARGIN);
    }

    public int getInnerMargin() {
        return getInteger(INNER_MARGIN);
    }

    public int getFontSize() {
        return getInteger(FONT_SIZE);
    }

    public int getMinimumGap() {
        return getInteger(MINIMUM_GAP);
    }

    public int getRecommendedLinkLength() {
        return getInteger(RECOMMENDED_LINK_LENGTH);
    }

    public String getFontName() {
        return (String) get(FONT_NAME);
    }

    public int getMinimumHeight() {
        return (int) get(MINIMUM_HEIGHT);
    }

    public int getMinimumWidth() {
        return (int) get(MINIMUM_WIDTH);
    }

    public Color getFocusedMarkerColor() {
        return (Color) get(FOCUSED_MARKER_COLOR);
    }
}
