package com.conference.demo.models;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date startDate;
    private Time beginningTime;
    private Time endingTime;

    @OneToOne(mappedBy = "schedule")
    private Presentation presentation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    public Schedule() {

    }

    public Schedule(Date startDate, Time beginningTime, Time endingTime, Room room) {
        this.startDate = startDate;
        this.beginningTime = beginningTime;
        this.endingTime = endingTime;
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Time getBeginningTime() {
        return beginningTime;
    }

    public void setBeginningTime(Time startTime) {
        this.beginningTime = beginningTime;
    }

    public Time getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Time finishTime) {
        this.endingTime = endingTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

}
