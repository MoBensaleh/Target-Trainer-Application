package com.example.assignment4;

import java.util.*;
import java.util.stream.Collectors;

public class BlobModel {
    private List<BlobModelListener> subscribers;
    private List<Blob> blobs;

    ArrayList<Blob> tempLassoSelectionList;

    public BlobModel() {
        subscribers = new ArrayList<>();
        blobs = new ArrayList<>();
        tempLassoSelectionList = new ArrayList<>();
    }

    public Blob createBlob(double x, double y) {
        Blob newBlob = new Blob(x,y);
        blobs.add(newBlob);
        notifySubscribers();
        return newBlob;
    }

    public void addBlob(Blob blob) {
        blobs.add(blob);
        notifySubscribers();
    }

    public void removeBlob(Blob b) {
        blobs.remove(b);
        notifySubscribers();
    }

    public void moveBlob(Blob b, double dx, double dy) {
        b.move(dx,dy);
        notifySubscribers();
    }

    /**
     * Delete the selected blob(s)
     * @param selected blob(s) to be deleted
     */
    public void deleteSelected(List<Blob> selected) {
        for (Blob selectedBlob : selected) {
            blobs.removeIf(blob -> blob == selectedBlob);
        }
        notifySubscribers();
    }

    /**
     * Delete the selected blob(s)
     * @param selected blob(s) to be deleted
     */
    public void addSelected(List<Blob> selected) {
        blobs.addAll(selected);
        notifySubscribers();
    }


    public void addSubscriber(BlobModelListener sub) {
        subscribers.add(sub);
    }

    public void notifySubscribers() {
        subscribers.forEach(s -> s.modelChanged());
    }

    public List<Blob> getBlobs() {
        return blobs;
    }

    public boolean hitBlob(double x, double y) {
        for (Blob b : blobs) {
            if (b.contains(x,y)) return true;
        }
        return false;
    }


    public Blob whichHit(double x, double y) {
        for (Blob b : blobs) {
            if (b.contains(x,y)) return b;
        }
        return null;
    }

    // part 1: add method
    public void moveBlobs(List<Blob> selection, double dX, double dY) {
        selection.forEach(b -> b.move(dX,dY));
        notifySubscribers();
    }

    public void resizeBlobs(List<Blob> selection, double dX) {
        selection.forEach(b -> b.resize(dX));
        notifySubscribers();
    }

    // part 2: add method (note there are two versions that do the same thing
    // the first uses streams, the second is more traditional)
    public List<Blob> areaHit(double x, double y) {
        return blobs.stream().filter(b -> b.contains(x,y)).collect(Collectors.toList());
    }

    /**
     * Method to return all blobs that are within the given rectangle for rubber-band selection.
     *
     * @param rubberBand : rectangle to check against
     * @return : list of blobs that are within the rectangle
     */
    public List<Blob> detectRubberBandHit(RubberBandRect rubberBand) {
        // calculate x, y coordinate of the rectangle (left and right)
        double x1, y1, x2, y2;
        x1 = rubberBand.left;
        y1 = rubberBand.top;
        x2 = x1 + rubberBand.width;
        y2 = y1 + rubberBand.height;
        // add every ship that is within the rectangle
        return blobs.stream().filter(s -> s.isContainedWithinRect(x1, x2, y1, y2)).collect(Collectors.toList());

    }

    /**
     * Method to return all blobs that are within the given rectangle for rubber-band selection.
     *
     * @return
     */
    public void addToLassoHitList(Blob b) {
        tempLassoSelectionList.add(b);
    }

    /**
     * Method to return all blobs that are within the given lasso for lasso selection.
     *
     * @return
     */
    public ArrayList<Blob> getLassoHitList() {
        return tempLassoSelectionList;
    }

    /**
     * Method to clear the temporary array list used for holding the selected blobs when checking for lasso hit.
     */
    public void clearLassoSelection() {
        tempLassoSelectionList = new ArrayList<>();

    }


    // part 2: alternate method that does not use streams
    public List<Blob> areaHit2(double x, double y, double cursorRadius) {
        List<Blob> hitList = new ArrayList<>();
        blobs.forEach(b -> {
            if (b.contains(x,y)) hitList.add(b);
        });
        return hitList;
    }

}
