package com.example.assignment4.controllers;

import com.example.assignment4.command.CreateCommand;
import com.example.assignment4.command.DeleteCommand;
import com.example.assignment4.command.MoveCommand;
import com.example.assignment4.command.ResizeCommand;
import com.example.assignment4.interfaces.TargetCommand;
import com.example.assignment4.Target;
import com.example.assignment4.models.TargetModel;
import com.example.assignment4.models.InteractionModel;
import com.example.assignment4.views.TargetTrainerView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the MVC Architecture. Overloads mouse clicks and handles the interactions from view to model and imodel.
 */
public class TargetController {
    TargetModel model;
    InteractionModel iModel;
    double prevX, prevY, rubberX, rubberY;;
    double dX, dY;
    double initialX, initialY;

    List<Target> hitList;

    /*
        States of the State-Machine used by the Controller to Over-load mouse clicks and keyboard presses.
    */
    enum State {READY, PREPARE_CREATE, MOVING, RESIZING, RUBBER_BAND_LASSO}

    /*
     Current state the machine is in.
    */
    State currentState;

    /**
     * Default constructor of this class.
     */
    public TargetController() {
        currentState = State.READY;
    }

    /**
     * Method to set the Model for this controller.
     *
     * @param newModel : Model to be saved
     */
    public void setModel(TargetModel newModel) {
        model = newModel;
    }

    /**
     * Method to set the iModel for this controller.
     *
     * @param newIModel : iModel to be saved
     */
    public void setIModel(InteractionModel newIModel) {
        iModel = newIModel;
    }


    /**
     * Delete the selected target(s) if there is one
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


    /**
     * Method to handle mouse presses.
     * @param event : mouse event
     */
    public void handlePressed(MouseEvent event) {
        switch (currentState) {
            case READY -> {
                hitList = model.areaHit(event.getX(), event.getY());
                if (hitList.size() > 0) {
                    prevX = event.getX();
                    prevY = event.getY();
                    initialX = event.getX();
                    initialY = event.getY();

                    if (event.isControlDown()) {
                        iModel.addSelected(hitList);
                    } else {
                        if (!iModel.allSelected(hitList)) {
                            iModel.clearSelection();
                            iModel.addSelected(hitList);
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
                            iModel.clearSelection();
                        }
                        iModel.createRubberBandLasso(event.getX(), event.getY());
                        rubberX = event.getX();
                        rubberY = event.getY();
                        currentState = State.RUBBER_BAND_LASSO;
                    }
                }
            }
        }
    }

    /**
     * Method to handle mouse drags.
     * @param event : mouse event
     */
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
                model.resizeTargets(iModel.getSelection(), dX);
            }

            case MOVING -> {
                model.moveTargets(iModel.getSelection(), dX, dY);
            }
            // Mouse move on Rubber state resizes the rubber-band rectangle used for selection
            case RUBBER_BAND_LASSO -> iModel.resizeRubberBandLasso(rubberX, rubberY, event.getX(), event.getY());
        }
    }

    /**
     * Method to handle mouse release.
     * @param event : mouse event
     */
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
                ArrayList<TargetCommand> moveCommands = new ArrayList<>();
                iModel.getSelection().forEach(s->{
                    moveCommands.add(new MoveCommand(model, s, dX,  dY));
                });
                iModel.addToUndo(moveCommands);
                currentState = State.READY;
            }

            case RUBBER_BAND_LASSO -> {
                currentState = State.READY;
                model.clearLassoSelection();
                List<Target> rubberHitTargets = model.detectRubberBandHit(iModel.getRubberBand());
                iModel.clearRubberBand();
                iModel.setPathComplete();
                List<Target> lassoHitTargets = model.getLassoHitList();
                if(lassoHitTargets.size() >= rubberHitTargets.size()){
                    iModel.addSelected(lassoHitTargets);
                }
                else{
                    iModel.addSelected(rubberHitTargets);
                }
                iModel.clearLasso();
            }
        }
    }

    /**
     * Method to handle key presses. Supports cut, copy, paste, undo, and redo for all actions related to targets, as well as
     * initializing a target trainer using Fitts' law.
     *
     * @param keyEvent key event
     */
    public void handleKeyPressed(KeyEvent keyEvent) {
        switch (currentState) {
            case READY -> {
                if (keyEvent.isControlDown()) {
                    if (keyEvent.getCode() == KeyCode.Z) {
                        if(iModel.getUndoStack().size() > 0){
                            iModel.peekUndo().forEach(TargetCommand::undo);
                            iModel.addToRedo(iModel.peekUndo());
                            iModel.popUndo();
                        }
                    }

                    else if (keyEvent.getCode() == KeyCode.R) {
                        if(iModel.getRedoStack().size()>0){
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
                            createCommands.add(new CreateCommand(model, b.getX(), b.getY()));

                        });
                        iModel.addToUndo(createCommands);
                        iModel.peekUndo().forEach(TargetCommand::doIt);
                    }
                    else if (keyEvent.getCode() == KeyCode.T) {
                        TargetTrainerView testView = new TargetTrainerView(model.getTargets());
                        // Show the test view
                        iModel.setTestView(testView);
                        iModel.setAppMode(InteractionModel.AppMode.TEST);
                    }
                    else if (keyEvent.getCode() == KeyCode.E) {
                        iModel.setAppMode(InteractionModel.AppMode.EDIT);
                    }
                }
                else if(keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE){
                    deleteSelected();
                }
            }
        }
    }
}
