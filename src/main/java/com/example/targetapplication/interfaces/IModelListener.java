package com.example.targetapplication.interfaces;

/**
 * Interface for all subscribers to get notified that the interaction model has changed.
 */
public interface IModelListener {
    /**
     * Method to be run when interaction model has changed.
     */
    void iModelChanged();
}
