package com.accenture.assignment.horsefeeder.DTO;

/**
 * Die Klasse, die angibt, wieviel ein Pferd nicht gegessen hat und wie oft ein Pferd nicht gegessen hat
 */
public class MissedFeedingDto {
    /**
     * Die GUID von dem Pferd um dass es sich handelt
     */
    private String horseGuid;
    private String horseName;
    /**
     * Wieviel Futter wurde verpasst
     */
    private int amountFood;
    /**
     * Wie oft wurde das Futter verpasst
     */
    private int amountMissed;

    public String getHorseGuid() {
        return horseGuid;
    }

    public void setHorseGuid(String horseGuid) {
        this.horseGuid = horseGuid;
    }

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public int getAmountFood() {
        return amountFood;
    }

    public void setAmountFood(int amountFood) {
        this.amountFood = amountFood;
    }

    public int getAmountMissed() {
        return amountMissed;
    }

    public void setAmountMissed(int amountMissed) {
        this.amountMissed = amountMissed;
    }
}
