package com.accenture.assignment.horsefeeder.DTO;

public class MissedFeedingDto {
    private String horseGuid;
    private String horseName;
    private int amountFood;
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
