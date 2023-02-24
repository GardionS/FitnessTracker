package com.gmail.gardion01.fitnesstracker.model;

public class User {
    private int id;
    private String userName;
    private String email;
    private String password;
    private int weight;
    private int age;
    private int exp;

    public User(int id, String userName, String email, int weight, int age, int exp) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.weight = weight;
        this.age = age;
        this.exp = exp;
    }

    public User() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
