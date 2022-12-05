package com.example.assignment4;

import java.util.List;

public class DeleteCommand implements TargetCommand{
    BlobModel model;
    List<Blob> blobs;

    public DeleteCommand(BlobModel newModel, List<Blob> newBlobs){
        model = newModel;
        blobs = newBlobs;
    }

    @Override
    public void doIt() {
        model.deleteSelected(blobs);
    }

    @Override
    public void undo() {
        model.addSelected(blobs);

    }
}
