package com.accenture.assignment.horsefeeder.DTO;

/**
 * Die Klasse die angibt, wie lange ein bestimmtes Pferd (dass nicht gegessen hat), nicht gegessen hat
 */
public class MissedEligibleDto {
    /**
     * Wie lange das Pferd nicht gegessen hat (Stunden)
     */
    private String amountTime;
    /**
     * GUID von dem Pferd, um dass es sich handelt
     */
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
