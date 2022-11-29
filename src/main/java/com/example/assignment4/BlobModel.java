package com.example.assignment4;

import java.util.*;
import java.util.stream.Collectors;

public class BlobModel {
    private List<BlobModelListener> subscribers;
    private List<Blob> blobs;

    public BlobModel() {
        subscribers = new ArrayList<>();
        blobs = new ArrayList<>();
    }

    public void addBlob(double x, double y) {
        blobs.add(new Blob(x,y));
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

    public void addSubscriber(BlobModelListener sub) {
        subscribers.add(sub);
    }

    private void notifySubscribers() {
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

    public void resizeShapes(List<Blob> selection, double dX) {
        selection.forEach(b -> b.resize(dX));
        notifySubscribers();
    }

    // part 2: add method (note there are two versions that do the same thing
    // the first uses streams, the second is more traditional)
    public List<Blob> areaHit(double x, double y) {
        return blobs.stream().filter(b -> b.contains(x,y)).collect(Collectors.toList());
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
