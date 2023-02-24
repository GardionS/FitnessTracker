package com.gmail.gardion01.fitnesstracker.model;

public class FitnessType {
    private int id;
    private String name;
    private int targetValue;

    public FitnessType() {
    }

    public FitnessType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(int targetValue) {
        this.targetValue = targetValue;
    }
}
