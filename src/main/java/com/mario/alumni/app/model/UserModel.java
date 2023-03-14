package com.mario.alumni.app.model;

public class UserModel {
    private Long userId;
    private String email;
    private Boolean isAdmin;
    private Boolean isOrganiser;
    private Boolean isAlumni;
    private String password;

    public UserModel() {
    }

    public UserModel(Long userId, String email, Boolean isAdmin, Boolean isOrganiser, Boolean isAlumni) {
        this.userId = userId;
        this.email = email;
        this.isAdmin = isAdmin;
        this.isOrganiser = isOrganiser;
        this.isAlumni = isAlumni;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getOrganiser() {
        return isOrganiser;
    }

    public void setOrganiser(Boolean organiser) {
        isOrganiser = organiser;
    }

    public Boolean getAlumni() {
        return isAlumni;
    }

    public void setAlumni(Boolean alumni) {
        isAlumni = alumni;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", isOrganiser=" + isOrganiser +
                ", isAlumni=" + isAlumni +
                '}';
    }
}
