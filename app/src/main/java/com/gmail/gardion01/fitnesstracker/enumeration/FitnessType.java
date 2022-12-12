package com.gmail.gardion01.fitnesstracker.enumeration;

public enum FitnessType {
    WALKING(0),
    RUNNING(1);
    private int value;

    FitnessType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
