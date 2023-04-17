package com.accenture.assignment.horsefeeder.DTO;

public class MissedAmountDto {
    private String guid;
    private Integer amount;

    public MissedAmountDto (String guid, Integer amount){
        this.guid = guid;
        this.amount=amount;
    }

    public MissedAmountDto(){

    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
