package com.example.assignment4;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class BlobController {
    BlobModel model;
    InteractionModel iModel;
    double prevX, prevY, rubberX, rubberY;;
    double dX, dY;
    double initialX, initialY;

    List<Blob> hitList;
    enum State {READY, PREPARE_CREATE, MOVING, RESIZING, RUBBER_BAND_LASSO}

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
            ArrayList<TargetCommand> deleteCommands= new ArrayList<>();
            iModel.getSelection().forEach(s->{
                deleteCommands.add(new DeleteCommand(model, s));
            });
            iModel.addToUndo(deleteCommands);
            iModel.peekUndo().forEach(TargetCommand::doIt);
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
                    prevX = event.getX();
                    prevY = event.getY();
                    initialX = event.getX();
                    initialY = event.getY();

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

                    // Check whether we are resizing or moving a blob for drag event
                    if(event.isShiftDown()){
                        currentState = State.RESIZING;
                    }
                    else{
                        currentState = State.MOVING;
                    }
                }
                else {
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
            case RESIZING -> {
                model.resizeBlobs(iModel.getSelection(), dX);
            }

            case MOVING -> {
                model.moveBlobs(iModel.getSelection(), dX, dY);
            }
            // Mouse move on Rubber state resizes the rubber-band rectangle used for selection
            case RUBBER_BAND_LASSO -> iModel.resizeRubberBandLasso(rubberX, rubberY, event.getX(), event.getY());
        }
    }

    public void handleReleased(MouseEvent event) {
        dX = event.getX() - initialX;
        dY = event.getY() - initialY;

        switch (currentState) {
            case PREPARE_CREATE -> {
                    ArrayList<TargetCommand> createCommand = new ArrayList<>();
                    createCommand.add(new CreateCommand(model, event.getX(), event.getY()));
                    iModel.addToUndo(createCommand);
                    iModel.peekUndo().forEach(TargetCommand::doIt);
                    currentState = State.READY;
            }
            case RESIZING -> {
                ArrayList<TargetCommand> resizeCommands = new ArrayList<>();
                iModel.getSelection().forEach(s->{
                    resizeCommands.add(new ResizeCommand(model, s, dX));
                });
                iModel.addToUndo(resizeCommands);
                currentState = State.READY;

            }
            case MOVING -> {
                //iModel.unselect(); // part 1 - remove this so selection is persistent
                ArrayList<TargetCommand> moveCommands = new ArrayList<>();
                iModel.getSelection().forEach(s->{
                    moveCommands.add(new MoveCommand(model, s, dX,  dY));
                });
                iModel.addToUndo(moveCommands);
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

    /**
     * Method to handle key presses. Supports cut, copy, paste, undo, and redo for all actions related to blobs.
     *
     * @param keyEvent key event
     */
    public void handleKeyPressed(KeyEvent keyEvent) {
        switch (currentState) {
            case READY -> {
                if (keyEvent.isControlDown()) {
                    if (keyEvent.getCode() == KeyCode.Z) {
                        if(iModel.undoStack.size() > 0){
                            iModel.peekUndo().forEach(TargetCommand::undo);
                            iModel.addToRedo(iModel.peekUndo());
                            iModel.popUndo();
                        }
                    }

                    else if (keyEvent.getCode() == KeyCode.R) {
                        if(iModel.redoStack.size()>0){
                            iModel.addToUndo(iModel.peekRedo());
                            iModel.peekRedo().forEach(TargetCommand::doIt);
                            iModel.popRedo();
                        }
                    }

                    else if (keyEvent.getCode() == KeyCode.C) {
                        iModel.copyToClipboard();
                    } else if (keyEvent.getCode() == KeyCode.X) {
                        ArrayList<TargetCommand> deleteCommands = new ArrayList<>();
                        iModel.cutToClipboard().forEach(b ->{
                            deleteCommands.add(new DeleteCommand(model, b));
                        });
                        iModel.addToUndo(deleteCommands);
                        iModel.peekUndo().forEach(TargetCommand::doIt);
                    } else if (keyEvent.getCode() == KeyCode.V) {
                        ArrayList<TargetCommand> createCommands = new ArrayList<>();
                        iModel.pasteFromClipboard().forEach(b->{
                            createCommands.add(new CreateCommand(model, b.x, b.y));

                        });
                        iModel.addToUndo(createCommands);
                        iModel.peekUndo().forEach(TargetCommand::doIt);
                    }
                }
                else if(keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE){
                    deleteSelected();
                }
            }
        }
    }
}
