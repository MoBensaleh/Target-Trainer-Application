package com.example.assignment4.command;

import com.example.assignment4.interfaces.TargetCommand;
import com.example.assignment4.Target;
import com.example.assignment4.models.TargetModel;

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
