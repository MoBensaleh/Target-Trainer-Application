package com.example.assignment4.command;

import com.example.assignment4.interfaces.TargetCommand;
import com.example.assignment4.Target;
import com.example.assignment4.models.TargetModel;

/**
 * Target Command class representing a delete command for targets
 */
public class DeleteCommand implements TargetCommand {
    TargetModel model;
    Target target;

    public DeleteCommand(TargetModel newModel, Target newTarget){
        model = newModel;
        target = newTarget;
    }

    @Override
    public void doIt() {
        model.removeTarget(target);
    }

    @Override
    public void undo() {
        model.addBlob(target);

    }
}
