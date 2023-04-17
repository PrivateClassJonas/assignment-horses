package com.accenture.assignment.horsefeeder.DTO;

public class MissedEligibleDto {
    private String amountTime;
    private String horseGuid;

    public String getAmountTime() {
        return amountTime;
    }

    public void setAmountTime(String amountTime) {
        this.amountTime = amountTime;
    }

    public String getHorseGuid() {
        return horseGuid;
    }

    public void setHorseGuid(String horseGuid) {
        this.horseGuid = horseGuid;
    }
}
