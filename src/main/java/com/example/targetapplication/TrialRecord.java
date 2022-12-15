package com.example.targetapplication;

/**
 * Class to represent a trial for the target trainer.
 */
public class TrialRecord {
    private long elapsedTime;
    private double id;

    /**
     * Default constructor for this class.
     */
    public TrialRecord(long elapsedTime, double id) {
        this.elapsedTime = elapsedTime;
        this.id = id;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public double getID() {
        return id;
    }
}
