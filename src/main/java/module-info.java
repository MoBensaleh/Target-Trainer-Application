module com.example.blobdemo2022 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.targetapplication to javafx.fxml;
    exports com.example.targetapplication.views;
    opens com.example.targetapplication.views to javafx.fxml;
    exports com.example.targetapplication.models;
    opens com.example.targetapplication.models to javafx.fxml;
    exports com.example.targetapplication.interfaces;
    opens com.example.targetapplication.interfaces to javafx.fxml;
    exports com.example.targetapplication.controllers;
    opens com.example.targetapplication.controllers to javafx.fxml;
    exports com.example.targetapplication.command;
    opens com.example.targetapplication.command to javafx.fxml;
    exports com.example.targetapplication.application;
    opens com.example.targetapplication.application to javafx.fxml;
    exports com.example.targetapplication;
}