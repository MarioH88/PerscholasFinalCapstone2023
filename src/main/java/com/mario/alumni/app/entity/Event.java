package com.mario.alumni.app.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "event")
public class Event {


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "event_id", columnDefinition = "VARCHAR(36)", updatable = false)
    private UUID eventId;

    @Basic
    @Column(name = "event_name", nullable = false, length = 45)
    private String eventName;

    @Basic
    @Column(name = "event_type", nullable = false, length = 64)
    private String eventType;

    @Basic
    @Column(name = "event_date", nullable = false)
    @DateTimeFormat(pattern = "MM-dd-yy")
    private LocalDate eventDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User organiser;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "signedup_event",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "alumni_id"))
    private Set<Alumni> alumni = new HashSet<>();

    public Event() {

    }

    public Event(UUID eventId, String eventName, LocalDate eventDate, String eventType, User organiser) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.organiser = organiser;

    }

    public Event(String eventName, String eventType, LocalDate eventDate, User organiser) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.organiser = organiser;
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

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }


    public LocalDate getEventDate() {
        return eventDate;
    }


    public User getOrganiser() {
        return organiser;
    }

    public void setOrganiser(User organiser) {
        this.organiser = organiser;
    }

    public Set<Alumni> getAlumni() {
        return alumni;
    }

    public void setAlumni(Set<Alumni> alumni) {
        this.alumni = alumni;
    }


    public void assignAlumniToEvent(Alumni alumni) {
        this.alumni.add(alumni);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventId, event.eventId) && Objects.equals(eventName, event.eventName) && Objects.equals(eventDate, event.eventDate) && Objects.equals(eventType, event.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, eventName, eventDate, eventType);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventName='" + eventName + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventDate=" + eventDate +
                '}';
    }
}