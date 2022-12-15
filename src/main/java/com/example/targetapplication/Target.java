package com.example.targetapplication;

import javafx.geometry.Point2D;

/**
 * Class to represent a Target. Supports move, resizing and creation of a target. Also provides a method to check if a point
 * exists within the target.
 */
public class Target {
    /*
        Instance variables to store the relevant information needed to draw a target.
     */
    private double x;
    private double y;

    private double r;

    /**
     * Default constructor of this class. Creates a target object with the given coordinates.
     *
     * @param nx : x coordinate
     * @param ny : y coordinate
     */
    public Target(double nx, double ny) {
        x = nx;
        y = ny;
        r = 30;
    }

    /**
     * Method to get center point of target
     * @return center point
     */
    public Point2D getCenter() {
        return new Point2D(x, y);
    }

    /**
     * Helper method to move the target to a new point.
     *
     * @param dx : Distance to move x coordinate by
     * @param dy : Distance to move y coordinate by
     */
    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    /**
     * Helper method to resize the target.
     *
     * @param dX : Amount to resize the target by
     */
    public void resize(double dX) {
        if (dX >= 0) {
            r += dX;
        } else if (dX <= 0 && r >= 5) {
            r -= Math.abs(dX);
        }
    }

    /**
     * Method to check if a point exists within the target.
     *
     * @param cx : x coordinate of the point to check
     * @param cy : y coordinate of the point to check
     * @return : true if point is inside ship else false
     */
    public boolean contains(double cx, double cy) {
        return dist(cx,cy,x,y) <= r;
    }


    /**
     * Method to check if the target is within the rubber-band rectangle selection.
     *
     * @param left : left x coordinate of the rectangle
     * @param top : left y coordinate of the rectangle
     * @param right : right x coordinate of the rectangle
     * @param bottom : right y coordinate of the rectangle
     * @return : true if all points of the target are within the rectangle else false
     */
    public boolean isContainedWithinRect(double left, double right, double top, double bottom) {
        boolean result = false;
        if ( ((x-r) >= left) && ((x+r) <= (right)) && ((y-r) >= top) && ((y+r) <=(bottom)) ) {
            result = true;
        }
        return result;
    }

    /**
     * Method to return a deep copy of the target.
     * @return : deep copy
     */
    public Target duplicate() {
        return new Target(x, y);
    }

    private double dist(double x1,double y1,double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
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
