package com.example.assignment4;

public class TrialRecord {
    private long elapsedTime;
    private double id;

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
