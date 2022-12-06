package com.example.assignment4.views;

import com.example.assignment4.controllers.BlobController;
import com.example.assignment4.interfaces.AppModeListener;
import com.example.assignment4.models.BlobModel;
import com.example.assignment4.models.InteractionModel;
import com.example.assignment4.views.BlobView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public class MainUI extends StackPane implements AppModeListener {
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
        this.controller.handleKeyPressed(event);
    }

    @Override
    public void appModeChanged() {

    }
}
