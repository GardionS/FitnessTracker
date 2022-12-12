package com.gmail.gardion01.fitnesstracker.model;

public class QuestType {
    private int id;
    private int fitnessTypeId;
    private String questName;
    private int questTargetValue;

    public QuestType() {
    }

    public QuestType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFitnessTypeId() {
        return fitnessTypeId;
    }

    public void setFitnessTypeId(int fitnessTypeId) {
        this.fitnessTypeId = fitnessTypeId;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public int getQuestTargetValue() {
        return questTargetValue;
    }

    public void setQuestTargetValue(int questTargetValue) {
        this.questTargetValue = questTargetValue;
    }
}
