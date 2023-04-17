package com.accenture.assignment.horsefeeder.DTO;

/**
 * Die Klasse, die angibt welches Pferd, zu welcher Uhrzeit gegessen hat, wieviel das Pferd übrig gelassen hat oder ob das Pferd sein Futter verpasst hat
 */
public class HistoryDto {
    private String horseName;
    private String horseGuid;
    private String time;
    private int amount;
    /**
     * 'done' : Pferd hat gegessen
     * 'missed' : Pferd hat das Futter verpasst
     */
    private String status;

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHorseGuid() {
        return horseGuid;
    }

    public void setHorseGuid(String horseGuid) {
        this.horseGuid = horseGuid;
    }
}
