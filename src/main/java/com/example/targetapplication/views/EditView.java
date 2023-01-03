package com.example.targetapplication.views;

import com.example.targetapplication.interfaces.IModelListener;
import com.example.targetapplication.RubberBandRect;
import com.example.targetapplication.controllers.TargetController;
import com.example.targetapplication.interfaces.TargetModelListener;
import com.example.targetapplication.Target;
import com.example.targetapplication.models.TargetModel;
import com.example.targetapplication.models.InteractionModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * View class for the MVC Architecture. Draws a canvas of 1000x700 size where targets are drawn. Can be subscribed for
 * model changes.
 */
public class EditView extends StackPane implements TargetModelListener, IModelListener {
    /*
        Instance variables to store canvas, graphic context and models from the MVC architecture.
     */
    GraphicsContext gc;
    Canvas myCanvas;
    Canvas checkCanvas;
    TargetModel model;
    InteractionModel iModel;
    PixelReader reader; // for checking the offscreen bitmap's colours

    /**
     * Default constructor for this class.
     */
    public EditView() {
        myCanvas = new Canvas(1000,700);
        checkCanvas = new Canvas(1000, 700);
        gc = myCanvas.getGraphicsContext2D();
        this.getChildren().add(myCanvas);
        this.setStyle("-fx-background-color: lightblue");

    }

    /**
     * Method to draw targets on the canvas.
     */
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
            gc.beginPath();
            iModel.getPath().forEach(p -> gc.lineTo(p.getX(), p.getY()));
            gc.setStroke(Color.RED);
            gc.stroke();
        } else {
            gc.setFill(Color.RED);
            gc.beginPath();
            gc.moveTo(iModel.getPath().get(0).getX(),iModel.getPath().get(0).getY());
            iModel.getPath().forEach(p -> gc.lineTo(p.getX(),p.getY()));
            gc.closePath();
            gc.fill();
            setupOffscreen();
        }

        AtomicInteger targetIdx = new AtomicInteger(1);
        model.getTargets().forEach(b -> {
            if(b.getX() >= 0 && b.getX() <= myCanvas.getWidth() && b.getY() >= 0 && b.getY() <= myCanvas.getHeight()){
                if (iModel.isSelected(b)) {
                    gc.setFill(Color.TOMATO);
                } else {
                    gc.setFill(Color.STEELBLUE);
                }
                gc.fillOval(b.getX() - b.getRadius(), b.getY() - b.getRadius(), b.getRadius() * 2, b.getRadius() * 2);
                gc.setFill(Color.WHITE);
                gc.fillText(String.valueOf(targetIdx.getAndIncrement()), b.getX()-3.3, b.getY()+3.3);

                if(iModel.getPathComplete()){
                    if (isContainedWithinLasso(b)) {
                        model.addToLassoHitList(b);
                    }
                }
            }
        });

    }

    /**
     *  Offscreen bitmap for checking 'contains' for lasso-selection
     */
    private void setupOffscreen() {
        checkCanvas = new Canvas(1000, 700);
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

    /**
     * Method to check whether a target is within lasso selection
     * @param target target to be checked
     * @return boolean for whether a target is within the lasso
     */
    private boolean isContainedWithinLasso(Target target) {
        return reader.getColor((int) (target.getX() - target.getRadius()), (int) (target.getY())).equals(Color.RED) &&
                reader.getColor((int) (target.getX() + target.getRadius()), (int) (target.getY())).equals(Color.RED)
                && reader.getColor((int) (target.getX()), (int) (target.getY() + target.getRadius())).equals(Color.RED)
                && reader.getColor((int) (target.getX()), (int) (target.getY() - target.getRadius())).equals(Color.RED);
    }

    /**
     * Method to store reference to the model.
     *
     * @param newModel : model of this view
     */
    public void setModel(TargetModel newModel) {
        model = newModel;
    }

    /**
     * Method to store reference to the iModel.
     *
     * @param newIModel : iModel of this view
     */
    public void setIModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    /**
     * Method runs when model has a change.
     */
    @Override
    public void modelChanged() {
        draw();
    }

    /**
     * Method runs when iModel has a change.
     */
    @Override
    public void iModelChanged() {
        draw();
    }

    /**
     * Method to set up event handlers for the view via the controller.
     *
     * @param controller : controller to trigger events
     */
    public void setController(TargetController controller) {

        // re-draw canvas when application is resized
        this.widthProperty().addListener((observable, oldVal, newVal) -> {
            myCanvas.setWidth(newVal.doubleValue());
            checkCanvas.setWidth(newVal.doubleValue());
            draw();
        });
        this.heightProperty().addListener((observable, oldVal, newVal) -> {
            myCanvas.setHeight(newVal.doubleValue());
            checkCanvas.setHeight(newVal.doubleValue());
            draw();
        });
        myCanvas.setOnMousePressed(controller::handlePressed);
        myCanvas.setOnMouseDragged(controller::handleDragged);
        myCanvas.setOnMouseReleased(controller::handleReleased);
    }
}
