package com.mario.alumni.app.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "alumni")
public class Alumni {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "alumni_id", columnDefinition = "VARCHAR(36)", updatable = false)
    private UUID alumniId;

    @Basic
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @ManyToMany(mappedBy = "alumni", fetch = FetchType.LAZY)
    private Set<Event> events;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    public Alumni(String firstName, String lastName, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.user = user;
    }

    public Alumni(UUID alumniId, String firstName, String lastName, User user) {
        this.alumniId = alumniId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.user = user;
    }

    public void assignAlumniToEvent(Event event) {
        this.events.add(event);
        event.getAlumni().add(this);
    }

    public void setAlumniId(UUID alumniId) {
        this.alumniId = alumniId;
    }

    public Alumni() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public UUID getAlumniId() {
        return alumniId;
    }

}