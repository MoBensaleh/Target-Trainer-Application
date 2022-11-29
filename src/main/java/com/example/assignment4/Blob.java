package com.example.assignment4;

public class Blob {
    double x,y, initialX, initialY;
    double r;

    public Blob(double nx, double ny) {
        x = nx;
        y = ny;
        initialX = nx;
        initialY = ny;
        r = 30;
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

    private double dist(double x1,double y1,double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }
}
