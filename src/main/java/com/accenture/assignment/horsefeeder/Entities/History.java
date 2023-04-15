package com.accenture.assignment.horsefeeder.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "feeding_history")
public class History {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feeding_time")
    private String time;

    @Column(name = "amount")
    private int amount;
    @Column(name = "status")
    private String status;
    @ManyToOne
    @JoinColumn(name = "horse_id")
    private Horse horse;
    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Horse getHorse() {
        return horse;
    }

    public void setHorse(Horse horse) {
        this.horse = horse;
    }

    public Food getFood() {
        return food;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
