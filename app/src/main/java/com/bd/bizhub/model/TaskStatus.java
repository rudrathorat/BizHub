package com.bd.bizhub.model;

import org.jetbrains.annotations.NotNull;

public enum TaskStatus {
    Open("Open"),
    InProgress("In Progress"),
    Complete("Complete");


    private String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }
    @NotNull
    public final String getDisplayName() {
        return this.displayName;
    }


}