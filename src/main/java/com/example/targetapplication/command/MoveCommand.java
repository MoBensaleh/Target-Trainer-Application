package com.example.targetapplication.command;

import com.example.targetapplication.interfaces.TargetCommand;
import com.example.targetapplication.Target;
import com.example.targetapplication.models.TargetModel;

/**
 * Target Command class representing a move command for targets
 */
public class MoveCommand implements TargetCommand {
    Target target;
    TargetModel model;
    double dx, dy;

    public MoveCommand(TargetModel newModel, Target newTarget, double newDX, double newDY){
        model = newModel;
        target = newTarget;
        dx = newDX;
        dy = newDY;

    }

    @Override
    public void doIt() {
        model.moveTarget(target, dx, dy);
    }

    @Override
    public void undo() {
        model.moveTarget(target, dx * -1 , dy * -1);

    }
}
