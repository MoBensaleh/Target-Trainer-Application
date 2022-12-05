package com.example.assignment4;

import java.util.List;

public class ResizeCommand implements TargetCommand {
    Blob blob;
    BlobModel model;
    double dx;

    public ResizeCommand(BlobModel newModel, Blob newBlob, double newDX){
        model = newModel;
        blob = newBlob;
        dx = newDX;
    }

    @Override
    public void doIt() {
        model.resizeBlob(blob, dx);
    }

    @Override
    public void undo() {
        model.resizeBlob(blob, dx * -1);

    }
}
