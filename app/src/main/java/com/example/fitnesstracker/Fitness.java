package com.example.fitnesstracker;

public class Fitness {
    private int id;
    private int userId;
    private String date;
    private int walk;

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

    public int getWalk() {
        return walk;
    }

    public void setWalk(int walk) {
        this.walk = walk;
    }

}
