package com.example.targetapplication.interfaces;

/**
 * Interface for all subscribers to get notified that the model has changed.
 */
public interface TargetModelListener {
    /**
     * Method to be run when model has changed.
     */
    void modelChanged();
}
