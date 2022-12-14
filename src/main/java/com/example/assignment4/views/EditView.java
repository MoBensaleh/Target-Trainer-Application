package com.example.assignment4.views;

import com.example.assignment4.interfaces.IModelListener;
import com.example.assignment4.RubberBandRect;
import com.example.assignment4.controllers.BlobController;
import com.example.assignment4.interfaces.BlobModelListener;
import com.example.assignment4.Blob;
import com.example.assignment4.models.BlobModel;
import com.example.assignment4.models.InteractionModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.concurrent.atomic.AtomicInteger;

public class EditView extends StackPane implements BlobModelListener, IModelListener {
    GraphicsContext gc;
    Canvas myCanvas;
    BlobModel model;
    InteractionModel iModel;
    PixelReader reader; // for checking the offscreen bitmap's colours

    public EditView() {
        myCanvas = new Canvas(1000,700);
        gc = myCanvas.getGraphicsContext2D();
        this.getChildren().add(myCanvas);
        this.setStyle("-fx-background-color: lightblue");

    }

    private void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());

        // draw rubber band rectangle for selection
        RubberBandRect rb = iModel.getRubberBand();
        if (rb != null) {
            gc.setStroke(Color.GREEN);
            gc.setFill(Color.TRANSPARENT);
            gc.strokeRect(rb.getLeft(), rb.getTop(), rb.getWidth(), rb.getHeight());
            gc.fillRect(rb.getLeft(), rb.getTop(), rb.getWidth(), rb.getHeight());
        }

        // draw user path (points during creation, filled path when finished)
        if (!iModel.getPathComplete()) {
            gc.setFill(Color.RED);
            iModel.getPath().forEach(p -> gc.fillOval(p.getX(),p.getY(),4,4));
        } else {
            gc.setFill(Color.RED);
            gc.beginPath();
            gc.moveTo(iModel.getPath().get(0).getX(),iModel.getPath().get(0).getY());
            iModel.getPath().forEach(p -> gc.lineTo(p.getX(),p.getY()));
            gc.closePath();
            gc.fill();
            setupOffscreen();
        }

        AtomicInteger blobIndex = new AtomicInteger(1);
        model.getBlobs().forEach(b -> {
            //if (b == iModel.getSelected()) { // part 1
            if (iModel.isSelected(b)) { // part 1
                gc.setFill(Color.TOMATO);
            } else {
                gc.setFill(Color.STEELBLUE);
            }
            gc.fillOval(b.getX() - b.getRadius(), b.getY() - b.getRadius(), b.getRadius() * 2, b.getRadius() * 2);
            gc.setFill(Color.WHITE);
            gc.fillText(String.valueOf(blobIndex.getAndIncrement()), b.getX()-3.3, b.getY()+3.3);

            if(iModel.getPathComplete()){
                if (isContainedWithinLasso(b)) {
                    model.addToLassoHitList(b);
                }
            }

        });

    }

    private void setupOffscreen() {
        // offscreen bitmap for checking 'contains' on an oddly-shaped polygon
        Canvas checkCanvas = new Canvas(1000, 700);
        GraphicsContext checkGC = checkCanvas.getGraphicsContext2D();

        checkGC.setFill(Color.RED);
        checkGC.beginPath();
        checkGC.moveTo(iModel.getPath().get(0).getX(),iModel.getPath().get(0).getY());
        iModel.getPath().forEach(p -> checkGC.lineTo(p.getX(),p.getY()));
        checkGC.closePath();
        checkGC.fill();


        WritableImage buffer = checkCanvas.snapshot(null, null);
        reader = buffer.getPixelReader();
    }

    private boolean isContainedWithinLasso(Blob b) {
        return reader.getColor((int) (b.getX() - b.getRadius()), (int) (b.getY() - b.getRadius())).equals(Color.RED) &&
                reader.getColor((int) (b.getX() + b.getRadius()), (int) (b.getY() - b.getRadius())).equals(Color.RED)
                && reader.getColor((int) (b.getX() - b.getRadius()), (int) (b.getY() + b.getRadius())).equals(Color.RED)
                && reader.getColor((int) (b.getX() + b.getRadius()), (int) (b.getY() + b.getRadius())).equals(Color.RED);
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
