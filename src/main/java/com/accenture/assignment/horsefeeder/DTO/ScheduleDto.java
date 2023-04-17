package com.accenture.assignment.horsefeeder.DTO;

import com.accenture.assignment.horsefeeder.Entities.Food;

import java.util.Set;

/**
 * Die Plan Klasse, die für die Verwendung von Plan Objekten in Abfragen und Service Methoden verwendet wird
 */
public class ScheduleDto {
    /**
     * Von wann das Pferd essen darf
     */
    private String start;
    /**
     * Bis wann das Pferd essen darf
     */
    private String end;
    private String horseName;
    /**
     * Welches Pferd essen darf
     */
    private String horseGuid;
    private String foodName;
    /**
     * Was das Pferd essen darf
     */
    private Long foodId;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public String getHorseGuid() {
        return horseGuid;
    }

    public void setHorseGuid(String horseGuid) {
        this.horseGuid = horseGuid;
    }

   public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }


}
