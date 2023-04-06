package com.example.app1.enums;

public enum Result {
    WIN("Win"),
    LOSE("Lose");


    private final String displayName;

    Result(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
