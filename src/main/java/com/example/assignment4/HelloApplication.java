package com.example.assignment4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        MainUI uiRoot = new MainUI();
        Scene scene = new Scene(uiRoot);

        scene.setOnKeyPressed(uiRoot::setOnKeyPressed);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}