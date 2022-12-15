package com.example.assignment4.interfaces;

/**
 * Interface for actions related to targets (create, delete, move, resize).
 */
public interface TargetCommand {
    /**
     * Method to be run when action is done.
     */
    void doIt();

    /**
     * Method to be run when action is undone.
     */
    void undo();
}
