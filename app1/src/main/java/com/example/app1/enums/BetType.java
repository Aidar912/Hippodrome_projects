package com.example.app1.enums;

public enum BetType {
    WINNER("Winner"),
    LAST("Last");


    private final String displayName;

    BetType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}