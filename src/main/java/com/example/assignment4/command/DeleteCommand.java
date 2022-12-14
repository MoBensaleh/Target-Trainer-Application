package com.example.assignment4.command;

import com.example.assignment4.interfaces.TargetCommand;
import com.example.assignment4.Blob;
import com.example.assignment4.models.BlobModel;

public class DeleteCommand implements TargetCommand {
    BlobModel model;
    Blob blob;

    public DeleteCommand(BlobModel newModel, Blob newBlob){
        model = newModel;
        blob = newBlob;
    }

    @Override
    public void doIt() {
        model.removeBlob(blob);
    }

    @Override
    public void undo() {
        model.addBlob(blob);

    }
}
