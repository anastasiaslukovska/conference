package com.conference.demo.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Presentation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "presenters", joinColumns = @JoinColumn(name = "presentation_id"), inverseJoinColumns = @JoinColumn(name = "presenters_id"))
    private Set<User> presenters = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "reg_listeners", joinColumns = @JoinColumn(name = "presentation_id"), inverseJoinColumns = @JoinColumn(name = "listener_id"))
    private Set<User> listeners = new HashSet<>();

    public Presentation() {

    }

    public Presentation(String title, String description, Set<User> presenters, Schedule schedule) {
        this.title = title;
        this.description = description;
        this.presenters = presenters;
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getPresentersList() {
        List<User> usersList = new ArrayList<>(presenters);
        return usersList;
    }

    public void setPresenters(Set<User> presenters) {
        this.presenters = presenters;
    }

    public Set<User> getPresenters() {
        return presenters;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Set<User> getListeners() {
        return listeners;
    }

    public void setListeners(Set<User> listeners) {
        this.listeners = listeners;
    }

    public List<User> getListenersList() {
        List<User> listenersList = new ArrayList<>(listeners);
        return listenersList;
    }
}
