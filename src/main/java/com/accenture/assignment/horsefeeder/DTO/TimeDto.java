package com.accenture.assignment.horsefeeder.DTO;

/**
 * Die Zeit Klasse für die Verwendung von Uhrzeiten in Abfragen und Service Methoden
 */
public class TimeDto {
    /**
     * Die gewünschte Uhrzeit
     */
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
