package com.mario.alumni;

import com.mario.alumni.app.entity.Alumni;
import com.mario.alumni.app.entity.Event;
import com.mario.alumni.app.entity.User;
import com.mario.alumni.app.enums.Role;
import com.mario.alumni.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Component
public class DbSetup {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private EventService eventService;
    @Autowired
    private AlumniService alumniService;

    public static final String ADMIN = "ADMIN";
    public static final String COMMITTEE = "COMMITTEE";
    public static final String ALUMNI = "ALUMNI";

    @PostConstruct
    public void init() {

        roleService.createRoleIfNotExists(Role.ADMIN);
        roleService.createRoleIfNotExists(Role.ORGANISER);
        roleService.createRoleIfNotExists(Role.ALUMNI);

        User user1 =
                userService
                        .createUserIfNotExists("user1@gmail.com", "pass1", new HashSet<>(List.of(Role.ADMIN, Role.ORGANISER)));
        User user2 =
                userService
                        .createUserIfNotExists("user2@gmail.com", "pass2", new HashSet<>(List.of(Role.ALUMNI)));
        User user3 =
                userService
                        .createUserIfNotExists("user3@gmail.com", "pass2", new HashSet<>(List.of(Role.ALUMNI)));


        Event event1 = eventService.createEventIfNotExists( "eventName1", LocalDate.now(), "eventType1", user1.getUserId());
        Event event2 = eventService.createEventIfNotExists("eventName2", LocalDate.now(), "eventType2", user1.getUserId());

        Alumni alumni1 = alumniService.loadAlumniByEmail("user2@gmail.com");
        Alumni alumni2 = alumniService.loadAlumniByEmail("user3@gmail.com");

        eventService.joinEvent(event1.getEventId(), alumni1.getAlumniId());
        eventService.joinEvent(event2.getEventId(), alumni2.getAlumniId());
    }

}
