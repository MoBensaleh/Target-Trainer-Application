package com.example.assignment4.command;

import com.example.assignment4.interfaces.TargetCommand;
import com.example.assignment4.Target;
import com.example.assignment4.models.TargetModel;

/**
 * Target Command class representing a resize command for targets
 */
public class ResizeCommand implements TargetCommand {
    Target target;
    TargetModel model;
    double dx;

    public ResizeCommand(TargetModel newModel, Target newTarget, double newDX){
        model = newModel;
        target = newTarget;
        dx = newDX;
    }

    @Override
    public void doIt() {
        model.resizeTarget(target, dx);
    }

    @Override
    public void undo() {
        model.resizeTarget(target, dx * -1);

    }
}
