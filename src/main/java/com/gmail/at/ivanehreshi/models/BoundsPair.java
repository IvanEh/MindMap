package com.gmail.at.ivanehreshi.models;

import java.awt.*;

public class BoundsPair {
    public final Point position;
    public final Dimension dimension;

    public BoundsPair(Point position, Dimension dimension) {
        this.position = position;
        this.dimension = dimension;
    }
}
