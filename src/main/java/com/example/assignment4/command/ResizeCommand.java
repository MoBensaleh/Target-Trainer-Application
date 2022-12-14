package com.example.assignment4.command;

import com.example.assignment4.interfaces.TargetCommand;
import com.example.assignment4.Blob;
import com.example.assignment4.models.BlobModel;

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
