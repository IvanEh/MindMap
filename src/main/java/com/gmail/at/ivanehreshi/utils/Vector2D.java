package com.gmail.at.ivanehreshi.utils;

import java.awt.*;
import java.awt.geom.Point2D;

// TODO: Point2D adapter
public class Vector2D extends Point {
    double x;
    double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D rotate(double phi) {
        double cosPhi = Math.cos(phi);
        double sinPhi = Math.sin(phi);

        double newX = x*cosPhi - y*sinPhi;
        double newY = x*sinPhi + y*cosPhi;

        this.x = newX;
        this.y = newY;

        return this;
    }

    public Vector2D invertAxis(Axis axis) {
        switch (axis) {
            case X:
                this.x = - this.x;
                break;
            case Y:
                this.y = - this.y;
                break;
        }
        return this;
    }

    public Vector2D mult(double k) {
        this.x*= k;
        this.y*= k;
        return this;
    }

    public double computeMagnitude() {
        return Math.sqrt(x*x + y*y);
    }

    public Vector2D norm() {
        this.mult(1 / computeMagnitude());
        return this;
    }

    public Vector2D move(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public enum Axis {
        X,
        Y
    }
}
