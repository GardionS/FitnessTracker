package com.gmail.gardion01.fitnesstracker.model;

public class User {
    private int id;
    private String userName;
    private String email;
    private String password;
    private int weight;
    private int age;
    private int exp;
    private boolean dailyQuest;//Since there only daily quest

    public User(int id, String userName, String email, int weight, int age, int exp, boolean dailyQuest) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.weight = weight;
        this.age = age;
        this.exp = exp;
        this.dailyQuest = dailyQuest;
    }

    public User() {
    }

    public boolean getDailyQuest() {
        return dailyQuest;
    }

    public void setDailyQuest(boolean dailyQuest) {
        this.dailyQuest = dailyQuest;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
    public int getExp(){
        return exp;
    }
}
