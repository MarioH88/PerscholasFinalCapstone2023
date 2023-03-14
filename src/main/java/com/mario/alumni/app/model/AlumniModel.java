package com.mario.alumni.app.model;

import java.util.UUID;

public class AlumniModel {
    private UUID alumniId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer numberOfEventsAttended;

    public AlumniModel() {
    }

    public AlumniModel(UUID alumniId, String firstName, String lastName, String email, Integer numberOfEventsAttended) {
        this.alumniId = alumniId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.numberOfEventsAttended = numberOfEventsAttended;
    }

    public UUID getAlumniId() {
        return alumniId;
    }

    public void setAlumniId(UUID alumniId) {
        this.alumniId = alumniId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNumberOfEventsAttended() {
        return numberOfEventsAttended;
    }

    public void setNumberOfEventsAttended(Integer numberOfEventsAttended) {
        this.numberOfEventsAttended = numberOfEventsAttended;
    }

    @Override
    public String toString() {
        return "AlumniModel{" +
                "alumniId=" + alumniId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailId='" + email + '\'' +
                ", numberOfEventsAttended=" + numberOfEventsAttended +
                '}';
    }

}
