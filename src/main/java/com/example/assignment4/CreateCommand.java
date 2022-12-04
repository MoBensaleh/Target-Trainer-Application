package com.example.assignment4;

public class CreateCommand implements TargetCommand{
    BlobModel model;
    Blob blob;
    double x, y;

    public CreateCommand(BlobModel newModel, double newX, double newY){
        model = newModel;
        blob = null;
        x = newX;
        y = newY;
    }

    @Override
    public void doIt() {
        blob = model.addBlob(x, y);

    }

    @Override
    public void undo() {
        model.removeBlob(blob);

    }
}
