package com.example.assignment4.models;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Class to represent a clipboard to allow cut, copy and paste of blobs in the application.
 */
public class TargetClipboard {
    // List of blobs that are copied or cut
    ArrayList<Blob> clipboard;

    /**
     * Default constructor for this class. Initializes the clipboard ArrayList.
     */
    public TargetClipboard() {
        clipboard = new ArrayList<>();
    }

    /**
     * Method to add blobs to the clipboard.
     *
     * @param selectedBlobs : list of blobs to be added to the clipboard
     */
    public void addItems(ArrayList<Blob> selectedBlobs) {
        // deep copy each blobs in the list and add it to the clipboard
        clipboard = selectedBlobs.stream().map(Blob::duplicate).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Method to get blobs from the clipboard.
     *
     * @return : list of blobs from the clipboard
     */
    public ArrayList<Blob> getItems() {
        // return a deep copy of each blob from the clipboard
        return clipboard.stream().map(Blob::duplicate).collect(Collectors.toCollection(ArrayList::new));
    }
}
