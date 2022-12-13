package com.example.assignment4.views;

import com.example.assignment4.controllers.TargetTrainerController;
import com.example.assignment4.models.Blob;
import com.example.assignment4.models.BlobModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;


public class TargetTrainerView extends StackPane {
    private BlobModel model;
    private int currentTargetIndex;
    private List<Blob> targets;
    GraphicsContext gc;
    Canvas myCanvas;

    public TargetTrainerView(List<Blob> targets) {
        this.targets = targets;
        this.currentTargetIndex = 0;
        myCanvas = new Canvas(1000,700);
        gc = myCanvas.getGraphicsContext2D();
        this.getChildren().add(myCanvas);
        this.setStyle("-fx-background-color: #F6DDB5");
        showNextTarget();
    }

    public Blob showNextTarget() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        gc.setFill(Color.STEELBLUE);
        gc.fillOval(targets.get(currentTargetIndex).getX() - targets.get(currentTargetIndex).getRadius(), targets.get(currentTargetIndex).getY() - targets.get(currentTargetIndex).getRadius(),
                targets.get(currentTargetIndex).getRadius() * 2, targets.get(currentTargetIndex).getRadius() * 2);
        return targets.get(currentTargetIndex);
    }

    public void setModel(BlobModel newModel) {
        model = newModel;
    }

    public void setController(TargetTrainerController controller) {
        myCanvas.setOnMousePressed(e->{
            System.out.println(targets);
            if(model.hitBlob(e.getX(), e.getY())) {


                    controller.handleClick(showNextTarget());
                    currentTargetIndex++;
                    if(currentTargetIndex < targets.size()){
                        showNextTarget();
                    }









            }
        });
    }


//    @Override
//    public void iModelChanged() {
//        showNextTarget();
//    }
}

