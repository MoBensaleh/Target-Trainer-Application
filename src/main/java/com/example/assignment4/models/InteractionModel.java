package com.example.assignment4.models;

import com.example.assignment4.Target;
import com.example.assignment4.RubberBandRect;
import com.example.assignment4.TargetClipboard;
import com.example.assignment4.TrialRecord;
import com.example.assignment4.interfaces.AppModeListener;
import com.example.assignment4.interfaces.IModelListener;
import com.example.assignment4.interfaces.TargetCommand;
import com.example.assignment4.views.ReportView;
import com.example.assignment4.views.TargetTrainerView;
import javafx.geometry.Point2D;

import java.util.*;

/**
 * Interaction Model for the MVC Architecture. Handles clipboard interactions, multi-selections, Undo/Redo,
 * and setting the application mode.
 */
public class InteractionModel {
    /*
        Instance variables to store subscribers, reference to the selected targets, rubber-band rect used for selection,
        a reference to the clipboard, Undo/Redo stacks, Lasso-Selection points, references to the report and target-trainer views,
        and trials for the target trainer
     */
    ArrayList<IModelListener> subscribers;
    ArrayList<AppModeListener> appModeSubscribers;
    ArrayList<Target> selection;
    ArrayList<TrialRecord> trials;
    ArrayList<Point2D> points; // Lasso selection
    boolean pathComplete; // Lasso selection
    RubberBandRect rubberBandRect;
    Stack<ArrayList<TargetCommand>> undoStack;
    Stack<ArrayList<TargetCommand>> redoStack;
    TargetClipboard clipboard;
    AppMode currentMode;
    ReportView reportView;
    TargetTrainerView testView;


    /**
     * Enum that describes all application modes
     */
    public enum AppMode {
        EDIT, TEST, REPORT
    }

    /**
     * Default constructor for this class.
     */
    public InteractionModel() {
        subscribers = new ArrayList<>();
        appModeSubscribers = new ArrayList<>();
        selection = new ArrayList<>(); // part 1
        points = new ArrayList<>();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        clipboard = new TargetClipboard();
        currentMode = AppMode.EDIT;
        trials = new ArrayList<>();
    }

    /**
     * Method to set the current application mode
     */
    public void setAppMode(AppMode newMode){
        if(newMode == AppMode.TEST){
            trials = new ArrayList<>();
        }
        currentMode = newMode;
        notifyModeSubscribers();
    }

    /**
     * Method to return the current application mode
     */
    public AppMode getCurrentAppMode(){
        return currentMode;
    }

    /**
     * Method to add a trial record to trials
     */
    public void addTrialRecord(TrialRecord record) {
        trials.add(record);
    }

    /**
     * Method to return all trials
     */
    public List<TrialRecord> getTrialRecords() {
        return trials;
    }

    /**
     * Method to add a subscriber to be notified of interaction model changes.
     *
     * @param sub : new subscriber
     */
    public void addSubscriber(IModelListener sub) {
        subscribers.add(sub);
    }
    /**
     * Method to notify all the subscribers of interaction model changes.
     */
    private void notifySubscribers() {
        subscribers.forEach(IModelListener::iModelChanged);
    }

    /**
     * Method to add a subscriber to be notified of application mode changes.
     *
     * @param sub : new subscriber
     */
    public void addModeSubscriber(AppModeListener sub) {
        appModeSubscribers.add(sub);
    }

    /**
     * Method to notify all the subscribers of application mode changes.
     */
    private void notifyModeSubscribers() {
        appModeSubscribers.forEach(AppModeListener::appModeChanged);
    }

    /**
     * Method to cut the current selected targets to the clipboard.
     *
     * @return : the list of current selected targets
     */
    public ArrayList<Target> cutToClipboard() {
        clipboard.addItems(selection);
        return selection;
    }

    /**
     * Method to copy the current selected targets to the clipboard.
     */
    public void copyToClipboard() {
        clipboard.addItems(selection);
    }

    /**
     * Method to paste the targets from the clipboard and set those targets as current selection.
     *
     * @return : Targets from the clipboard
     */
    public ArrayList<Target> pasteFromClipboard() {
        ArrayList<Target> pastedTargets = clipboard.getItems();
        selection = pastedTargets;
        return pastedTargets;
    }

    /**
     * Method to set a new selection of targets if it is not already selected.
     * @param selectedTargets : targets to be selected
     */
    public void addSelected(List<Target> selectedTargets) {
        System.out.println(selection);
        for(Target s : selectedTargets){
            // remove the selected target if it is already in selection
            if (selection.contains(s)) {
                selection.remove(s);
            } else {
                selection.add(s);
            }
        }
        notifySubscribers();
    }

    /**
     * Method to clear the selected targets.
     */
    public void clearSelection() {
        selection.clear();
        notifySubscribers();
    }

    /**
     * Method to determine if a target is selected
     */
    public boolean isSelected(Target b) {
        return selection.contains(b);
    }

    /**
     * Method to return all selected targets
     */
    public List<Target> getSelection() {
        return selection;
    }

    /**
     * Method to if a collection of targets are selected
     */
    public boolean allSelected(List<Target> hitList) {
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

    /**
     * Method to clear the lasso used for selection.
     */
    public void clearLasso(){
        points.clear();
        pathComplete = false;
        notifySubscribers();
    }

    /**
     * Method to set a path as complete use for lasso-selection
     */
    public void setPathComplete(){
        pathComplete = true;
        notifySubscribers();
    }

    /**
     * Method to add target commands to the undo stack
     */
    public void addToUndo(ArrayList<TargetCommand> commands){
        undoStack.push(commands);
    }

    /**
     * Method to peek the undo stack and return the target commands.
     */
    public ArrayList<TargetCommand> peekUndo(){
        return undoStack.peek();
    }

    /**
     * Method to pop the undo stack
     */
    public void popUndo(){
        undoStack.pop();
    }

    /**
     * Method to add target commands to the redo stack
     */
    public void addToRedo(ArrayList<TargetCommand> commands){
        redoStack.push(commands);
    }

    /**
     * Method to peek the redo stack and return the target commands.
     */
    public ArrayList<TargetCommand> peekRedo(){
        return redoStack.peek();
    }

    /**
     * Method to pop the redo stack
     */
    public void popRedo(){
        redoStack.pop();
    }

    /**
     * Method to return the undo stack
     */
    public Stack<ArrayList<TargetCommand>> getUndoStack(){
        return undoStack;
    }

    /**
     * Method to return the redo stack
     */
    public Stack<ArrayList<TargetCommand>> getRedoStack(){
        return redoStack;
    }

    /**
     * Method to set the report view
     */
    public void setReportView(ReportView newReportView){
        reportView = newReportView;
    }

    /**
     * Method to return the report view
     */
    public ReportView getReportView(){
        return reportView;
    }

    /**
     * Method to set the target trainer view
     */
    public void setTestView(TargetTrainerView newTestView){
        testView = newTestView;
    }

    /**
     * Method to return the target trainer view
     */
    public TargetTrainerView getTestView(){
        return testView;
    }

}

