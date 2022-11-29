package com.example.assignment4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.concurrent.atomic.AtomicInteger;

public class BlobView extends StackPane implements BlobModelListener, IModelListener {
    GraphicsContext gc;
    Canvas myCanvas;
    BlobModel model;
    InteractionModel iModel;

    public BlobView() {
        myCanvas = new Canvas(1000,700);
        gc = myCanvas.getGraphicsContext2D();
        this.getChildren().add(myCanvas);
        this.setStyle("-fx-background-color: lightblue");

    }

    private void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        AtomicInteger blobIndex = new AtomicInteger(1);
        model.getBlobs().forEach(b -> {
            //if (b == iModel.getSelected()) { // part 1
            if (iModel.isSelected(b)) { // part 1
                gc.setFill(Color.TOMATO);
            } else {
                gc.setFill(Color.STEELBLUE);
            }
            gc.fillOval(b.x - b.r, b.y - b.r, b.r * 2, b.r * 2);
            gc.setFill(Color.WHITE);
            gc.fillText(String.valueOf(blobIndex.getAndIncrement()), b.x-3.3, b.y+3.3);

        });

    }

    public void setModel(BlobModel newModel) {
        model = newModel;
    }

    public void setIModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    @Override
    public void modelChanged() {
        draw();

    }

    @Override
    public void iModelChanged() {
        draw();
    }

    public void setController(BlobController controller) {
        myCanvas.setOnMousePressed(controller::handlePressed);
        myCanvas.setOnMouseDragged(controller::handleDragged);
        myCanvas.setOnMouseReleased(controller::handleReleased);
    }
}
