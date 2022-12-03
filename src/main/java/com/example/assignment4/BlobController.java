package com.example.assignment4;

import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class BlobController {
    BlobModel model;
    InteractionModel iModel;
    double prevX, prevY, rubberX, rubberY;;
    double dX, dY;

    List<Blob> hitList;
    enum State {READY, PREPARE_CREATE, DRAGGING, RUBBER_BAND_LASSO}

    State currentState = State.READY;

    public BlobController() {

    }

    public void setModel(BlobModel newModel) {
        model = newModel;
    }

    public void setIModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    /**
     * Delete the selected blob(s) if there is one
     */
    public void deleteSelected() {
        if (iModel.getSelection().size()>0) {
            model.deleteSelected(iModel.getSelection());
//            iModel.setSelected(null);
            currentState = State.READY;
        }
    }



    public void handlePressed(MouseEvent event) {
        switch (currentState) {
            case READY -> {
                //if (model.hitBlob(event.getX(),event.getY())) { // part 2
                //Blob b = model.whichHit(event.getX(),event.getY()); // part 2
                hitList = model.areaHit(event.getX(), event.getY()); // part 2
                if (hitList.size() > 0) { // part 2

                    if (event.isControlDown()) { // part 1
                        //iModel.setSelected(b); // part 1
                        //iModel.select(b); // part 1
                        iModel.addSelected(hitList); // part 2
                    } else { // part 1
                        //if (!iModel.isSelected(b)) { // part 1: only clear if click is on an unselected blob
                        if (!iModel.allSelected(hitList)) { // part 2: only clear if the cursor is not on a selected
                            iModel.clearSelection(); // part 1
                            // iModel.select(b); // part 1
                            iModel.addSelected(hitList); // part 2
                        }
                    }
                    prevX = event.getX();
                    prevY = event.getY();
                    currentState = State.DRAGGING;
                } else {
                    if(event.isShiftDown()){
                        currentState = State.PREPARE_CREATE;
                    }
                    else{
                        if (!(event.isControlDown())) {
                            // context: when control is not pressed
                            // side effect: allows rubber-band to deselect ships that are already selected when control is down
                            // and clear existing selection
                            iModel.clearSelection();
                        }
                        // context: shift button is not pressed
                        // side effect: clear selection and start drawing rubber band
                        // and switch state to Rubber Band drawing
                        iModel.createRubberBandLasso(event.getX(), event.getY());
                        rubberX = event.getX();
                        rubberY = event.getY();
                        currentState = State.RUBBER_BAND_LASSO;

                    }
                }
            }
        }
    }

    public void handleDragged(MouseEvent event) {
        dX = event.getX() - prevX;
        dY = event.getY() - prevY;
        prevX = event.getX();
        prevY = event.getY();
        switch (currentState) {
            case PREPARE_CREATE -> {
                currentState = State.READY;
            }
            case DRAGGING -> {
                // model.moveBlob(iModel.getSelected(), dX,dY); // part 1
                if(event.isShiftDown()){
                    model.resizeShapes(iModel.getSelection(), dX);
                }
                else{
                    model.moveBlobs(iModel.getSelection(), dX, dY); // part 1 (handle moving multiple)

                }
            }
            // Mouse move on Rubber state resizes the rubber-band rectangle used for selection
            case RUBBER_BAND_LASSO -> iModel.resizeRubberBandLasso(rubberX, rubberY, event.getX(), event.getY());
        }
    }

    public void handleReleased(MouseEvent event) {
        switch (currentState) {
            case PREPARE_CREATE -> {
                    model.addBlob(event.getX(), event.getY());
                    currentState = State.READY;

            }
            case DRAGGING -> {
                //iModel.unselect(); // part 1 - remove this so selection is persistent
                currentState = State.READY;
            }

            case RUBBER_BAND_LASSO -> {
                // side effect: check all ships that are within the rectangle and select them in iModel
                // clear rectangle object and clear temporary model's selection
                currentState = State.READY;
                model.clearLassoSelection();
                List<Blob> rubberHitBlobs = model.detectRubberBandHit(iModel.getRubberBand());
                iModel.clearRubberBand();
                iModel.setPathComplete();
                List<Blob> lassoHitBlobs = model.getLassoHitList();
//                System.out.println(lassoHitBlobs);
                if(lassoHitBlobs.size() >= rubberHitBlobs.size()){

                    iModel.addSelected(lassoHitBlobs);


                }
                else{
                    iModel.addSelected(rubberHitBlobs);
                }
                iModel.clearLasso();
            }
        }
    }
}
