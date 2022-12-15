package com.example.targetapplication.command;

import com.example.targetapplication.interfaces.TargetCommand;
import com.example.targetapplication.Target;
import com.example.targetapplication.models.TargetModel;

/**
 * Target Command class representing a create command for targets
 */
public class CreateCommand implements TargetCommand {
    TargetModel model;
    Target target;
    double x, y;


    public CreateCommand(TargetModel newModel, double newX, double newY){
        model = newModel;
        target = null;
        x = newX;
        y = newY;
    }

    @Override
    public void doIt() {
        if(target == null){
            target = model.createTarget(x, y);
        }
        else{
            model.addTarget(target);
        }
    }

    @Override
    public void undo() {
        model.removeTarget(target);

    }
}
