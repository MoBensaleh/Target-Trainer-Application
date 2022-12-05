package com.example.assignment4;

import java.util.ArrayList;

public class CreateCommand implements TargetCommand{
    BlobModel model;
    Blob blob;

    ArrayList<Blob> blobs;
    double x, y;


    public CreateCommand(BlobModel newModel, double newX, double newY){
        model = newModel;
        blob = null;
        x = newX;
        y = newY;
    }
    public CreateCommand(BlobModel newModel, ArrayList<Blob> newBlobs){
        model = newModel;
        blobs = newBlobs;
    }

    @Override
    public void doIt() {
        if(blob == null && blobs == null){
            blob = model.createBlob(x, y);
        }
        else{
            if(blobs == null){
                model.addBlob(blob);
            }
            else{
                model.addBlobList(blobs);
            }
        }
    }

    @Override
    public void undo() {
        if (blobs == null) {
            model.removeBlob(blob);
        }
        else{
            model.removeBlobList(blobs);
        }
    }
}
