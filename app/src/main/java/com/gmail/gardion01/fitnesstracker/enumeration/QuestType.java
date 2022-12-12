package com.gmail.gardion01.fitnesstracker.enumeration;

public enum QuestType { //Enum for the different type of quest
    DAILY_QUEST(1);

    private int value;

    QuestType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
