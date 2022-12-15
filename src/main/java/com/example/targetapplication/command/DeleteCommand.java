package com.example.targetapplication.command;

import com.example.targetapplication.interfaces.TargetCommand;
import com.example.targetapplication.Target;
import com.example.targetapplication.models.TargetModel;

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
        model.addTarget(target);

    }
}
