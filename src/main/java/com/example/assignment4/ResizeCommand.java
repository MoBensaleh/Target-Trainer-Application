package com.example.assignment4;

import java.util.List;

public class ResizeCommand implements TargetCommand {
    List<Blob> blobs;
    BlobModel model;
    double dx;

    public ResizeCommand(BlobModel newModel, List<Blob> newBlobs, double newDX){
        model = newModel;
        blobs = newBlobs;
        dx = newDX;
    }

    @Override
    public void doIt() {
        model.resizeBlobs(blobs, dx);
    }

    @Override
    public void undo() {
        model.resizeBlobs(blobs, dx * -1);

    }
}
