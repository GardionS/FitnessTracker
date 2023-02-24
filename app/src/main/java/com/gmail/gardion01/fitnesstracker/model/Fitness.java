package com.gmail.gardion01.fitnesstracker.model;

public class Fitness {
    private int id;
    private int userId;
    private int typeId;
    private String date;
    private int value;

    public Fitness() {
    }

    public Fitness(int userId, int typeId, String date) {
        this.userId = userId;
        this.date = date;
        this.typeId = typeId;
        this.value = 0;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
