package com.academy.workSearch.model.enums;

public enum ProjectStatus {
    LOOKING_FOR_WORKER ("Looking for worker"),
    DEVELOPING ("Developing"),
    TESTING ("Testing"),
    CLOSED ("Closed");

    private String name;

    ProjectStatus(String name) {
        this.name = name;
    }
}
