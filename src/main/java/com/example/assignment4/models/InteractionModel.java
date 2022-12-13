package com.example.assignment4.models;

import com.example.assignment4.interfaces.AppModeListener;
import com.example.assignment4.interfaces.IModelListener;
import com.example.assignment4.interfaces.TargetCommand;
import com.example.assignment4.views.ReportView;
import com.example.assignment4.views.TargetTrainerView;
import javafx.geometry.Point2D;

import java.util.*;

public class InteractionModel {
    ArrayList<IModelListener> subscribers;
    ArrayList<AppModeListener> appModeSubscribers;
    ArrayList<Blob> selection; // part 1
    RubberBandRect rubberBandRect;

    List<Point2D> points; // Lasso
    boolean pathComplete;

    Stack<ArrayList<TargetCommand>> undoStack;
    Stack<ArrayList<TargetCommand>> redoStack;
    TargetClipboard clipboard;
    AppMode currentMode;
    List<TrialRecord> trials;

    ReportView reportView;
    TargetTrainerView testView;

    public enum AppMode {
        EDIT, TEST, REPORT
    }


    public InteractionModel() {
        subscribers = new ArrayList<>();
        appModeSubscribers = new ArrayList<>();
        selection = new ArrayList<>(); // part 1
        points = new ArrayList<>();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        clipboard = new TargetClipboard();
        currentMode = AppMode.EDIT;
        this.trials = new ArrayList<>();
    }

    public void setAppMode(AppMode newMode){
        if(newMode == AppMode.TEST){
            this.trials = new ArrayList<>();
        }
        this.currentMode = newMode;
        notifyModeSubscribers();
    }

    public void setReportView(ReportView newReportView){
        reportView = newReportView;
    }

    public ReportView getReportView(){
        return reportView;
    }

    public void setTestView(TargetTrainerView newTestView){
        testView = newTestView;
    }

    public TargetTrainerView getTestView(){
        return testView;
    }
    public void addTrialRecord(TrialRecord record) {
        trials.add(record);
    }

    public List<TrialRecord> getTrialRecords() {
        return trials;
    }


    public AppMode getCurrentAppMode(){
        return currentMode;
    }

    public void addSubscriber(IModelListener sub) {
        subscribers.add(sub);
    }
    public void addModeSubscriber(AppModeListener sub) {
        appModeSubscribers.add(sub);
    }

    private void notifySubscribers() {
        subscribers.forEach(IModelListener::iModelChanged);
    }
    private void notifyModeSubscribers() {
        appModeSubscribers.forEach(AppModeListener::appModeChanged);
    }

    /**
     * Method to cut the current selected blobs to the clipboard.
     *
     * @return : the list of current selected blobs
     */
    public ArrayList<Blob> cutToClipboard() {
        clipboard.addItems(selection);
        return selection;
    }

    /**
     * Method to copy the current selected blobs to the clipboard.
     */
    public void copyToClipboard() {
        clipboard.addItems(selection);
    }

    /**
     * Method to paste the blobs from the clipboard and set those blobs as current selection.
     *
     * @return : Blobs from the clipboard
     */
    public ArrayList<Blob> pasteFromClipboard() {
        ArrayList<Blob> pastedBlobs = clipboard.getItems();
        selection = pastedBlobs;
        return pastedBlobs;
    }

//     part 2: add method
//    public void select(List<Blob> hitList) {
//        hitList.forEach(this::addSelected);
//        notifySubscribers();
//    }

    /**
     * Method to set a new ship as one of the selected items if it is not already selected.
     *
     * @param newSelection : ship to be selected
     */
    public void addSelected(List<Blob> selectedBlobs) {
        System.out.println(selection);
        for(Blob s : selectedBlobs){
            // remove the selected ship if it is already in selection
            if (selection.contains(s)) {
                selection.remove(s);
            } else {
                selection.add(s);
            }

        }
        notifySubscribers();
    }


    // part 1: add method
    public void clearSelection() {
        selection.clear();
        notifySubscribers();
    }


    // part 1: add method
    public boolean isSelected(Blob b) {
        return selection.contains(b);
    }

    // part 1: add method
    public List<Blob> getSelection() {
        return selection;
    }




    // part 2: add method
    public boolean allSelected(List<Blob> hitList) {
        return new HashSet<>(selection).containsAll(hitList);
    }

    /**
     * Method to create an object to represent the rubber-band rectangle for selection.
     *
     * @param x : starting x coordinate for the rectangle
     * @param y : starting y coordinate for the rectangle
     */
    public void createRubberBandLasso(double x, double y) {
        points.clear();
        pathComplete = false;
        rubberBandRect = new RubberBandRect(x, y);
        points.add(new Point2D(x, y));
        notifySubscribers();
    }


    /**
     * Method to resize the rubber-band rectangle for selection.
     *
     * @param prevX : starting x coordinate for the rectangle
     * @param prevY : starting y coordinate for the rectangle
     * @param x     : ending x coordinate for the rectangle
     * @param y     : ending y coordinate for the rectangle
     */
    public void resizeRubberBandLasso(double prevX, double prevY, double x, double y) {
        rubberBandRect.resize(prevX, prevY, x, y);
        points.add(new Point2D(x, y));
        notifySubscribers();
    }

    /**
     * Method to get rubber-band rectangle object.
     *
     * @return : rectangle to draw
     */
    public RubberBandRect getRubberBand() {
        return rubberBandRect;
    }


    /**
     * Method to get whether lasso path is complete.
     *
     * @return : true if path of lasso is complete, otherwise false.
     */
    public Boolean getPathComplete(){
        return pathComplete;
    }

    /**
     * Method to get the lasso path.
     *
     * @return : Array List containing points for path
     */
    public List<Point2D> getPath(){
        return points;
    }

    /**
     * Method to clear the rubber-band rectangle used for selection.
     */
    public void clearRubberBand() {
        rubberBandRect = null;
        notifySubscribers();
    }

    public void clearLasso(){
        points.clear();
        pathComplete = false;
        notifySubscribers();
    }
    public void setPathComplete(){
        pathComplete = true;
        notifySubscribers();
    }

    public void addToUndo(ArrayList<TargetCommand> commands){
        undoStack.push(commands);
    }
    public ArrayList<TargetCommand> peekUndo(){
        return undoStack.peek();
    }
    public void popUndo(){
        undoStack.pop();
    }
    public void addToRedo(ArrayList<TargetCommand> commands){
        redoStack.push(commands);
    }
    public ArrayList<TargetCommand> peekRedo(){
        return redoStack.peek();
    }

    public void popRedo(){
        redoStack.pop();
    }

    public Stack<ArrayList<TargetCommand>> getUndoStack(){
        return undoStack;
    }
    public Stack<ArrayList<TargetCommand>> getRedoStack(){
        return redoStack;
    }

}

