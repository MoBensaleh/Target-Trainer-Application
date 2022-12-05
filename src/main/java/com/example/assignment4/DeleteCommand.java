package com.example.assignment4;

import java.util.List;

public class DeleteCommand implements TargetCommand{
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
