package com.example.assignment4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InteractionModel {
    List<IModelListener> subscribers;
    Blob selected; // now superseded by variable 'selection'
    List<Blob> selection; // part 1
    double cursorX, cursorY; // part 2

    public InteractionModel() {
        subscribers = new ArrayList<>();
        selection = new ArrayList<>(); // part 1
    }

    public void addSubscriber(IModelListener sub) {
        subscribers.add(sub);
    }

    private void notifySubscribers() {
        subscribers.forEach(s -> s.iModelChanged());
    }

    // part 2: add method
    public void select(List<Blob> hitList) {
        hitList.forEach(this::addSubtract);
        notifySubscribers();
    }

    // part 1: add method
    public void select(Blob b) {
        //selected = b; // remove for part 1
        addSubtract(b); // part 1
        notifySubscribers();
    }

    // part 1: add method
    private void addSubtract(Blob b) {
        if (selection.contains(b)) {
            selection.remove(b);
        } else {
            selection.add(b);
        }
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
        return selection.containsAll(hitList);
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

