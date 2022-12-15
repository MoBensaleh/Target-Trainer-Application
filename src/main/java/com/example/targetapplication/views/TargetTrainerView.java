package com.example.targetapplication.views;

import com.example.targetapplication.controllers.TargetTrainerController;
import com.example.targetapplication.Target;
import com.example.targetapplication.models.TargetModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * View class for the MVC Architecture. Displays one target at a time.
 */
public class TargetTrainerView extends StackPane {
    private TargetModel model;
    private int currentTargetIndex;
    private List<Target> targets;
    GraphicsContext gc;
    Canvas myCanvas;

    /**
     * Default constructor for this class.
     * @param targets targets to be displayed one at a time.
     */
    public TargetTrainerView(List<Target> targets) {
        this.targets = targets;
        this.currentTargetIndex = 0;
        myCanvas = new Canvas(1000,700);
        gc = myCanvas.getGraphicsContext2D();
        this.getChildren().add(myCanvas);
        this.setStyle("-fx-background-color: #F6DDB5");
        showNextTarget();
    }

    /**
     * Method redraws canvas when target is clicked and shows next target
     */
    public Target showNextTarget() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        gc.setFill(Color.STEELBLUE);
        gc.fillOval(targets.get(currentTargetIndex).getX() - targets.get(currentTargetIndex).getRadius(), targets.get(currentTargetIndex).getY() - targets.get(currentTargetIndex).getRadius(),
                targets.get(currentTargetIndex).getRadius() * 2, targets.get(currentTargetIndex).getRadius() * 2);
        return targets.get(currentTargetIndex);
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
     * Method to set up event handlers for the view via the controller.
     *
     * @param controller : controller to trigger events
     */
    public void setController(TargetTrainerController controller) {
        myCanvas.setOnMousePressed(e->{
            if(model.hitTarget(e.getX(), e.getY())) {
                    controller.handleClick(showNextTarget());
                    currentTargetIndex++;
                    if(currentTargetIndex < targets.size()){
                        showNextTarget();
                    }
            }
        });
    }
}

