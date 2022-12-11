package com.example.fitnesstracker;

public class Fitness {
    private int id;
    private int userId;
    private String date;
    private int walk;
    private int running;

    public Fitness() {
    }

    public Fitness(int userId, String date) {
        this.userId = userId;
        this.date = date;
        this.walk = 0;
        this.running = 0;
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

    public int getWalk() {
        return walk;
    }

    public void setWalk(int walk) {
        this.walk = walk;
    }

    public int getRunning() {
        return running;
    }

    public void setRunning(int running) {
        this.running = running;
    }
}
