package com.example.assignment4;

import java.util.List;

public class MoveCommand implements TargetCommand {
    Blob blob;
    BlobModel model;
    double dx, dy;

    public MoveCommand(BlobModel newModel, Blob newBlob, double newDX, double newDY){
        model = newModel;
        blob = newBlob;
        dx = newDX;
        dy = newDY;

    }

    @Override
    public void doIt() {
        model.moveBlob(blob, dx, dy);
    }

    @Override
    public void undo() {
        model.moveBlob(blob, dx * -1 , dy * -1);

    }
}
