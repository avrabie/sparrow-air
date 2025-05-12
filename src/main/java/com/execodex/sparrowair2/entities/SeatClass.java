package com.execodex.sparrowair2.entities;

public enum SeatClass {
    ECONOMY("Economy"),
    PREMIUM_ECONOMY("Premium Economy"),
    BUSINESS("Business"),
    FIRST_CLASS("First Class");

    private final String displayName;

    SeatClass(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
