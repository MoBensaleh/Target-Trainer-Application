package com.example.assignment4.command;

import com.example.assignment4.interfaces.TargetCommand;
import com.example.assignment4.models.Blob;
import com.example.assignment4.models.BlobModel;

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
