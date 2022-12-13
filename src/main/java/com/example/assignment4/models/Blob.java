package com.example.assignment4.models;

import javafx.geometry.Point2D;

public class Blob {
    private double x;
    private double y;

    private double r;
    public Blob(double nx, double ny) {
        x = nx;
        y = ny;
        r = 30;
    }

    public Point2D getCenter() {
        return new Point2D(x, y);
    }


    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void resize(double dX) {
        if (dX >= 0) {
            r += dX;
        } else if (dX <= 0 && r >= 5) {
            r -= Math.abs(dX);
        }
    }

    // part 3: add method


    public boolean contains(double cx, double cy) {
        return dist(cx,cy,x,y) <= r;
    }

    public boolean isContainedWithinRect(double left, double right, double top, double bottom) {
        boolean result = false;
        if ( ((x-r) >= left) && ((x+r) <= (right)) && ((y-r) >= top) && ((y+r) <=(bottom)) ) {
            result = true;
        }
        return result;
    }


    private double dist(double x1,double y1,double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }

    public Blob duplicate() {
        return new Blob(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public double getRadius() {
        return r;
    }
}
