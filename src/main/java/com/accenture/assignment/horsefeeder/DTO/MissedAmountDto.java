package com.accenture.assignment.horsefeeder.DTO;

/**
 * Die Klasse, die angibt wie viel Futter ein gewissen Pferd schon verpasst hat
 */
public class MissedAmountDto {
    /**
     * GUID von dem Pferd
     */
    private String guid;
    /**
     * Wieviel Futter das pferd verpasst hat
     */
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
