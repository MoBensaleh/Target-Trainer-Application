package com.example.targetapplication.interfaces;

/**
 * Interface for all subscribers to get notified that the application mode has changed.
 */
public interface AppModeListener {
    /**
     * Method to be run when app mode has changed.
     */
    void appModeChanged();
}
