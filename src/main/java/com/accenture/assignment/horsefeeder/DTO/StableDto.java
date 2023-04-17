package com.accenture.assignment.horsefeeder.DTO;

/**
 * Die Stall Klasse für die Verwendung von Stall Objekten in Abfragen oder Service Methoden
 */
public class StableDto {
    /**
     * Name des Stalls
     */
  private  String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
