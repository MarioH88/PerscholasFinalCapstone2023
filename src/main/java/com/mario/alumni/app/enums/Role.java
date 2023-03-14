package com.mario.alumni.app.enums;

public enum Role {
    ADMIN("ROLE_ADMIN"), ORGANISER("ROLE_ORGANISER"), ALUMNI("ROLE_ALUMNI");

    private String value;
    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static Role getEnum(String role) {
        if(ADMIN.getValue().equals(role)) return ADMIN;
        else if(ORGANISER.getValue().equals(role)) return ORGANISER;
        else if(ALUMNI.getValue().equals(role)) return ALUMNI;
        else return null;
    }
}
