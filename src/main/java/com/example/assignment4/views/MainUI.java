package com.example.assignment4.views;

import com.example.assignment4.controllers.TargetController;
import com.example.assignment4.controllers.TargetTrainerController;
import com.example.assignment4.interfaces.AppModeListener;
import com.example.assignment4.models.TargetModel;
import com.example.assignment4.models.InteractionModel;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public class MainUI extends StackPane implements AppModeListener {
    TargetController controller;
    TargetModel model;
    InteractionModel iModel;
    EditView editView;
    TargetTrainerController targetTrainerController;

    /**
     * Default constructor for this class.
     */
    public MainUI() {
        model = new TargetModel();
        controller = new TargetController();
        targetTrainerController = new TargetTrainerController();
        editView = new EditView();
        iModel = new InteractionModel();

        controller.setModel(model);
        editView.setModel(model);
        controller.setIModel(iModel);
        targetTrainerController.setIModel(iModel);
        targetTrainerController.setModel(model);
        editView.setIModel(iModel);
        model.addSubscriber(editView);
        iModel.addSubscriber(editView);
        iModel.addModeSubscriber(this);

        editView.setController(controller);

        this.getChildren().add(editView);
    }

    /**
     * Method listens for key events and calls controller handler
     * @param event key event
     */
    public void setOnKeyPressed(KeyEvent event){
        this.controller.handleKeyPressed(event);
    }

    /**
     * Method runs when app mode has a change.
     */
    @Override
    public void appModeChanged() {
        this.getChildren().clear();
        switch (iModel.getCurrentAppMode()) {
            case EDIT:
                this.getChildren().add(editView);
                break;
            case TEST:
                TargetTrainerView testView = iModel.getTestView();
                iModel.getTestView().setModel(model);
                iModel.getTestView().setController(targetTrainerController);
                this.getChildren().add(testView);
                break;
            case REPORT:
                this.getChildren().add(iModel.getReportView());
                break;
        }


    }
}
