package com.example.assignment4;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class InteractionModel {
    List<IModelListener> subscribers;
    Blob selected; // now superseded by variable 'selection'
    List<Blob> selection; // part 1
    double cursorX, cursorY; // part 2
    RubberBandRect rubberBandRect;

    List<Point2D> points; // Lasso
    boolean pathComplete;

    public InteractionModel() {
        subscribers = new ArrayList<>();
        selection = new ArrayList<>(); // part 1
        points = new ArrayList<>();
    }

    public void addSubscriber(IModelListener sub) {
        subscribers.add(sub);
    }

    private void notifySubscribers() {
        subscribers.forEach(s -> s.iModelChanged());
    }

//     part 2: add method
//    public void select(List<Blob> hitList) {
//        hitList.forEach(this::addSelected);
//        notifySubscribers();
//    }

    /**
     * Method to set a new ship as one of the selected items if it is not already selected.
     *
     * @param newSelection : ship to be selected
     */
    public void addSelected(List<Blob> selectedBlobs) {
        System.out.println(selection);
        for(Blob s : selectedBlobs){
            // remove the selected ship if it is already in selection
            if (selection.contains(s)) {
                selection.remove(s);
            } else {
                selection.add(s);
            }

        }
        notifySubscribers();
    }


    // part 1: this method now superseded by select()
    // we can remove this method
    public void setSelected(Blob b) {
        selected = b;
        notifySubscribers();
    }

    // part 1: add method
    public void clearSelection() {
        selection.clear();
        notifySubscribers();
    }

    // part 1: this method is now superseded by clearSelection
    // (we can remove this method)
    public void unselect() {
        selected = null;
        notifySubscribers();
    }

    // part 1: add method
    public boolean isSelected(Blob b) {
        return selection.contains(b);
    }

    // part 1: add method
    public List<Blob> getSelection() {
        return selection;
    }




    // part 2: add method
    public boolean allSelected(List<Blob> hitList) {
        return new HashSet<>(selection).containsAll(hitList);
    }

    /**
     * Method to create an object to represent the rubber-band rectangle for selection.
     *
     * @param x : starting x coordinate for the rectangle
     * @param y : starting y coordinate for the rectangle
     */
    public void createRubberBandLasso(double x, double y) {
        points.clear();
        pathComplete = false;
        rubberBandRect = new RubberBandRect(x, y);
        points.add(new Point2D(x, y));
        notifySubscribers();
    }


    /**
     * Method to resize the rubber-band rectangle for selection.
     *
     * @param prevX : starting x coordinate for the rectangle
     * @param prevY : starting y coordinate for the rectangle
     * @param x     : ending x coordinate for the rectangle
     * @param y     : ending y coordinate for the rectangle
     */
    public void resizeRubberBandLasso(double prevX, double prevY, double x, double y) {
        rubberBandRect.resize(prevX, prevY, x, y);
        points.add(new Point2D(x, y));
        notifySubscribers();
    }

    /**
     * Method to get rubber-band rectangle object.
     *
     * @return : rectangle to draw
     */
    public RubberBandRect getRubberBand() {
        return rubberBandRect;
    }


    /**
     * Method to get whether lasso path is complete.
     *
     * @return : true if path of lasso is complete, otherwise false.
     */
    public Boolean getPathComplete(){
        return pathComplete;
    }

    /**
     * Method to get the lasso path.
     *
     * @return : Array List containing points for path
     */
    public List<Point2D> getPath(){
        return points;
    }

    /**
     * Method to clear the rubber-band rectangle used for selection.
     */
    public void clearRubberBand() {
        rubberBandRect = null;
        notifySubscribers();
    }

    public void clearLasso(){
        points.clear();
        pathComplete = false;
        notifySubscribers();
    }
    public void setPathComplete(){
        pathComplete = true;
        notifySubscribers();
    }



    // part 2: add method
    public double getCursorX() {
        return cursorX;
    }

    // part 2: add method
    public double getCursorY() {
        return cursorY;
    }
}

