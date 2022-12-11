package com.example.fitnesstracker;

public enum QuestType {
    DAILY_QUEST(1);

    private int value;

    QuestType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
