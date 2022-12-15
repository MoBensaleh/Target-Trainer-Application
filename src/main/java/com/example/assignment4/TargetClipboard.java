package com.example.assignment4;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Class to represent a clipboard to allow cut, copy and paste of blobs in the application.
 */
public class TargetClipboard {
    // List of targets that are copied or cut
    ArrayList<Target> clipboard;

    /**
     * Default constructor for this class. Initializes the clipboard ArrayList.
     */
    public TargetClipboard() {
        clipboard = new ArrayList<>();
    }

    /**
     * Method to add targets to the clipboard.
     *
     * @param selectedTargets : list of targets to be added to the clipboard
     */
    public void addItems(ArrayList<Target> selectedTargets) {
        // deep copy each blobs in the list and add it to the clipboard
        clipboard = selectedTargets.stream().map(Target::duplicate).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Method to get targets from the clipboard.
     *
     * @return : list of targets from the clipboard
     */
    public ArrayList<Target> getItems() {
        // return a deep copy of each blob from the clipboard
        return clipboard.stream().map(Target::duplicate).collect(Collectors.toCollection(ArrayList::new));
    }
}
