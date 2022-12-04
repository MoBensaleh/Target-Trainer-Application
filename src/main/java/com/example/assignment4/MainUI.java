package com.example.assignment4;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainUI extends StackPane {
    BlobController controller;

    public MainUI() {

        BlobModel model = new BlobModel();
        controller = new BlobController();
        BlobView view = new BlobView();
        InteractionModel iModel = new InteractionModel();

        controller.setModel(model);
        view.setModel(model);
        controller.setIModel(iModel);
        view.setIModel(iModel);
        model.addSubscriber(view);
        iModel.addSubscriber(view);

        view.setController(controller);

        this.getChildren().add(view);
    }

    public void setOnKeyPressed(KeyEvent event){
        // add event handler for when delete/backspace key is pressed
        if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
            this.controller.deleteSelected();
        }
        else{
            this.controller.handleKeyPressed(event);
        }
    }
}
