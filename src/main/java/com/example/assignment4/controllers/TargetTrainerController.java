package com.example.assignment4.controllers;

import com.example.assignment4.Target;
import com.example.assignment4.models.TargetModel;
import com.example.assignment4.models.InteractionModel;
import com.example.assignment4.TrialRecord;
import com.example.assignment4.views.ReportView;

import java.util.List;

public class TargetTrainerController {
    private InteractionModel iModel;
    private long startTime;
    private Target previousTarget;
    private TargetModel model;

    /**
     * Method to set the iModel for this controller.
     *
     * @param newIModel : iModel to be saved
     */
    public void setIModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    /**
     * Method to set the Model for this controller.
     *
     * @param newModel : Model to be saved
     */
    public void setModel(TargetModel newModel) {
        model = newModel;
    }


    /**
     * Method to handle mouse presses in the target trainer view.
     *
     * @param currentTarget  : Target clicked
     */
    public void handleClick(Target currentTarget) {
        if (startTime == 0) {
            // first click on first target, start timer
            startTime = System.currentTimeMillis();
            previousTarget = currentTarget;
        } else {
            // calculate elapsed time and index of difficulty
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            double id = calculateID(previousTarget, currentTarget);
            // add trial record
            iModel.addTrialRecord(new TrialRecord(elapsedTime, id));
            // update start time for next trial
            startTime = endTime;
            // show next target
            previousTarget = currentTarget;

            // Upon trials completion, report target trainer findings and reset trainer
            if (iModel.getTrialRecords().size()+1 == model.getTargets().size()) {
                startTime = 0;
                previousTarget = null;
                // switch to report view
                List<TrialRecord> records = iModel.getTrialRecords();
                // Use the list of records to create the report view
                ReportView reportView = new ReportView(records);
                // Show the report view
                iModel.setReportView(reportView);
                iModel.setAppMode(InteractionModel.AppMode.REPORT);
            }
        }

    }

    /**
     * Method to calculate the index of difficulty of a click according to Fitts' law
     *
     * @param previous     : previous target clicked before current target
     * @param current     : current target
     */
    private double calculateID(Target previous, Target current) {
        // calculate distance and width
        double distance = previous.getCenter().distance(current.getCenter());
        double width = current.getRadius()*2;
        // calculate index of difficulty
        return Math.log(distance / width + 1) / Math.log(2);
    }

}
