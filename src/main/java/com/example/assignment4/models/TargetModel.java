package com.example.assignment4.models;

import com.example.assignment4.Target;
import com.example.assignment4.RubberBandRect;
import com.example.assignment4.interfaces.TargetModelListener;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Model for this MVC Architecture. Stores a list of targets.
 */
public class TargetModel {
    /*
        Instance variables to store the targets and subscribers of the model.
     */
    ArrayList<TargetModelListener> subscribers;
    ArrayList<Target> targets;
    ArrayList<Target> tempLassoSelectionList;

    /**
     * Default constructor of this class.
     */
    public TargetModel() {
        subscribers = new ArrayList<>();
        targets = new ArrayList<>();
        tempLassoSelectionList = new ArrayList<>();
    }

    /**
     * Method to create a new target which is to be stored inside model.
     *
     * @param x : x coordinate for the target to be created at
     * @param y : y coordinate for the target to be created at
     * @return : newly created target object
     */
    public Target createTarget(double x, double y) {
        Target newTarget = new Target(x,y);
        targets.add(newTarget);
        notifySubscribers();
        return newTarget;
    }

    /**
     * Method to add a target to the model.
     *
     * @param target : target to be added
     */
    public void addTarget(Target target) {
        targets.add(target);
        notifySubscribers();
    }

    /**
     * Method to remove a target from the model.
     *
     * @param target : target to be removed
     */
    public void removeTarget(Target target) {
        targets.remove(target);
        notifySubscribers();
    }

    /**
     * Method to add a subscriber to be notified of model changes.
     *
     * @param sub : new subscriber
     */
    public void addSubscriber(TargetModelListener sub) {
        subscribers.add(sub);
    }

    /**
     * Method to notify all the subscribers of model changes.
     */
    public void notifySubscribers() {
        subscribers.forEach(TargetModelListener::modelChanged);
    }

    /**
     * Method to return all targets from the model.
     */
    public List<Target> getTargets() {
        return targets;
    }


    /**
     * Method to determine if a click is on a target.
     * @param x mouse x position
     * @param y mouse y position
     * @return boolean determining if a target has been clicked
     */
    public boolean hitTarget(double x, double y) {
        for (Target b : targets) {
            if (b.contains(x,y)) return true;
        }
        return false;
    }

    /**
     * Method to move targets
     *
     * @param targets : List of targets to be moved
     * @param dX    : Distance to move x coordinate by
     * @param dY    : Distance to move y coordinate by
     */
    public void moveTargets(List<Target> targets, double dX, double dY) {
        targets.forEach(t -> t.move(dX,dY));
        notifySubscribers();
    }

    /**
     * Method to move a target.
     *
     * @param target : target to be moved
     * @param dx    : Distance to move x coordinate by
     * @param dy    : Distance to move y coordinate by
     */
    public void moveTarget(Target target, double dx, double dy) {
        target.move(dx,dy);
        notifySubscribers();
    }


    /**
     * Method to resize targets
     * @param targets : List of targets to be resized
     * @param dX    : Amount to resize target by
     */
    public void resizeTargets(List<Target> targets, double dX) {
        targets.forEach(t -> t.resize(dX));
        notifySubscribers();
    }
    /**
     * Method to resize targets
     * @param target : target to be resized
     * @param dX    : Amount to resize target by
     */
    public void resizeTarget(Target target, double dX) {
        target.resize(dX);
        notifySubscribers();
    }

    /**
     * Method to get all targets within a given x and y coordinate area
     * @param x x-coordinate of area
     * @param y y-coordinate of area
     */
    public List<Target> areaHit(double x, double y) {
        return targets.stream().filter(b -> b.contains(x,y)).collect(Collectors.toList());
    }

    /**
     * Method to return all targets that are within the given rectangle for rubber-band selection.
     *
     * @param rubberBand : rectangle to check against
     * @return : list of target that are within the rectangle
     */
    public List<Target> detectRubberBandHit(RubberBandRect rubberBand) {
        // calculate x, y coordinate of the rectangle (left and right)
        double x1, y1, x2, y2;
        x1 = rubberBand.getLeft();
        y1 = rubberBand.getTop();
        x2 = x1 + rubberBand.getWidth();
        y2 = y1 + rubberBand.getHeight();
        // add every target that is within the rectangle
        return targets.stream().filter(s -> s.isContainedWithinRect(x1, x2, y1, y2)).collect(Collectors.toList());

    }

    /**
     * Method to add a target to the lasso selection hit list
     */
    public void addToLassoHitList(Target b) {
        tempLassoSelectionList.add(b);
    }

    /**
     * Method to return all targets that are within the given lasso for lasso selection.
     */
    public ArrayList<Target> getLassoHitList() {
        return tempLassoSelectionList;
    }

    /**
     * Method to clear the temporary array list used for holding the selected target when checking for lasso hit.
     */
    public void clearLassoSelection() {
        tempLassoSelectionList = new ArrayList<>();
    }

}
