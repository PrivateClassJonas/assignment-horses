package com.accenture.assignment.horsefeeder.Entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "feeding_schedule")
public class Schedule {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feeding_start")
    private String start;

    @Column(name = "feeding_end")
    private String end;

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

    public Horse getHorse() {
        return horse;
    }

    public void setHorse(Horse horse) {
        this.horse = horse;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

}
