package com.accenture.assignment.horsefeeder.DTO;

public class ScheduleDto {

    private String start;
    private String end;
    private String horseName;
    private String horseGUID;
    private String foodName;
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

    public String getHorseGUID() {
        return horseGUID;
    }

    public void setHorseGUID(String horseGUID) {
        this.horseGUID = horseGUID;
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
