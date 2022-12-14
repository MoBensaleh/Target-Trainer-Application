package com.example.assignment4.controllers;

import com.example.assignment4.Blob;
import com.example.assignment4.models.BlobModel;
import com.example.assignment4.models.InteractionModel;
import com.example.assignment4.TrialRecord;
import com.example.assignment4.views.ReportView;

import java.util.List;

public class TargetTrainerController {
    private InteractionModel iModel;
    private long startTime;
    private Blob previousTarget;
    private BlobModel model;

    public TargetTrainerController() {
    }

    public void setIModel(InteractionModel newIModel) {
        iModel = newIModel;
    }
    public void setModel(BlobModel newModel) {
        model = newModel;
    }

    public void handleClick(Blob currentTarget) {
        System.out.println(currentTarget);
        // check if all targets have been shown
        // record elapsed time and index of difficulty for current trial
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

            System.out.println(iModel.getTrialRecords());
            if (iModel.getTrialRecords().size()+1 == model.getBlobs().size()) {
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

    private double calculateID(Blob previous, Blob current) {
        // calculate distance and width
//        System.out.println(previous);
//        System.out.println(current);
        double distance = previous.getCenter().distance(current.getCenter());
        double width = current.getRadius()*2;
        // calculate index of difficulty
        return Math.log(distance / width + 1) / Math.log(2);
    }

}
