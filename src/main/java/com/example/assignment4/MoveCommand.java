package com.example.assignment4;

import java.util.List;

public class MoveCommand implements TargetCommand {
    List<Blob> blobs;
    BlobModel model;
    double dx, dy;

    public MoveCommand(BlobModel newModel, List<Blob> newBlobs, double newDX, double newDY){
        model = newModel;
        blobs = newBlobs;
        dx = newDX;
        dy = newDY;
    }

    @Override
    public void doIt() {
        model.moveBlobs(blobs, dx, dy);
    }

    @Override
    public void undo() {
        model.moveBlobs(blobs, dx * -1 , dy * -1);

    }
}
