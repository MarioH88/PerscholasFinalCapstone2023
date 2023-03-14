package com.mario.alumni.app.model;

import com.mario.alumni.app.entity.Alumni;
import com.mario.alumni.app.entity.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EventModel {
    private UUID eventId;
    private String eventName;
    private String eventType;
    private LocalDate eventDate;
    private UserModel organiser;
    private Set<AlumniModel> alumni = new HashSet<>();

    public EventModel() {
    }

    public EventModel(UUID eventId, String eventName, String eventType, LocalDate eventDate, UserModel organiser, Set<AlumniModel> alumni) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.organiser = organiser;
        this.alumni = alumni;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public UserModel getOrganiser() {
        return organiser;
    }

    public void setOrganiser(UserModel organiser) {
        this.organiser = organiser;
    }

    public Set<AlumniModel> getAlumni() {
        return alumni;
    }

    public void setAlumni(Set<AlumniModel> alumni) {
        this.alumni = alumni;
    }


}
