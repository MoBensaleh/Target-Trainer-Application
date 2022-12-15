package com.example.assignment4.command;

import com.example.assignment4.interfaces.TargetCommand;
import com.example.assignment4.Target;
import com.example.assignment4.models.TargetModel;

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
            model.addBlob(target);
        }
    }

    @Override
    public void undo() {
        model.removeTarget(target);

    }
}
