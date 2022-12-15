package com.example.assignment4.application;

import com.example.assignment4.views.MainUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application class which starts up a JavaFX stage with a canvas to create, move, select, and resize Targets or a group of Targets.
 */
public class TargetApplication extends Application {
    @Override
    public void start(Stage stage) {
        MainUI uiRoot = new MainUI();
        Scene scene = new Scene(uiRoot);

        scene.setOnKeyPressed(uiRoot::setOnKeyPressed);

        stage.setTitle("Fitts' Law Target Trainer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}