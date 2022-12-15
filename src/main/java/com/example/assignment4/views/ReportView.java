package com.example.assignment4.views;

import com.example.assignment4.TrialRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

import java.util.List;

/**
 * View class for the MVC Architecture. Displays Target Trainer results using a scatter chart.
 */
public class ReportView extends StackPane {
    private ScatterChart<Number, Number> chart;

    /**
     * Default constructor for this class.
     */
    public ReportView(List<TrialRecord> records) {
        // create chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        chart = new ScatterChart<>(xAxis, yAxis);
        chart.setTitle("Targeting Performance (Fitts' Law)");
        chart.setMinSize(500, 400);
        // set axes
        xAxis.setLabel("Index of Difficulty (bits)");
        xAxis.setAutoRanging(true);
        yAxis.setLabel("Movement Time (ms)");
        yAxis.setAutoRanging(true);
        // add data series
        ObservableList<XYChart.Series<Number, Number>> data = FXCollections.observableArrayList();
        data.add(getTrialData(records));
        chart.setData(data);
        // add chart to view
        this.getChildren().add(chart);
    }

    /**
     * Method returns all trial data
     * @param records A list containing all trial records
     * @return trial data to plot
     */
    private XYChart.Series<Number, Number> getTrialData(List<TrialRecord> records) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Trials");
        // add data points for each trial
        for (TrialRecord record : records) {
            series.getData().add(new XYChart.Data<>(record.getID(), record.getElapsedTime()));
        }
        return series;
    }
}
