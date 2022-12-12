package com.gmail.gardion01.fitnesstracker.model;

public class Quest {
    private int id;
    private int userId;
    private int questType;
    private String date;
    private int value;

    public Quest(int userId, int questType, String date) {
        this.userId = userId;
        this.questType = questType;
        this.date = date;
    }

    public Quest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuestType() {
        return questType;
    }

    public void setQuestType(int questType) {
        this.questType = questType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
