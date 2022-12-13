module com.example.blobdemo2022 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.assignment4 to javafx.fxml;
    exports com.example.assignment4.views;
    opens com.example.assignment4.views to javafx.fxml;
    exports com.example.assignment4.models;
    opens com.example.assignment4.models to javafx.fxml;
    exports com.example.assignment4.interfaces;
    opens com.example.assignment4.interfaces to javafx.fxml;
    exports com.example.assignment4.controllers;
    opens com.example.assignment4.controllers to javafx.fxml;
    exports com.example.assignment4.command;
    opens com.example.assignment4.command to javafx.fxml;
    exports com.example.assignment4.application;
    opens com.example.assignment4.application to javafx.fxml;
}