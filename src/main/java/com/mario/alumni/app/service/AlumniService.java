package com.mario.alumni.app.service;



import com.mario.alumni.app.entity.Alumni;
import com.mario.alumni.app.entity.User;
import com.mario.alumni.app.model.AlumniModel;
import com.mario.alumni.app.model.EventModel;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface AlumniService {

    void removeAlumni(UUID alumniId);



    List<Alumni> findAlumniByName(String name);

    Alumni loadAlumniByEmail(String email);
    AlumniModel loadAlumniById(UUID alumniId);
    void deleteAlumniById(UUID alumniId);

    Alumni createAlumni(UUID alumniId, String firstName, String lastName, String email, String password, User user);
    Alumni createAlumniIfNotExists(UUID alumniId, String firstName, String lastName, String email, String password, User user);

    Alumni updateAlumni(Alumni alumni);

    AlumniModel updateAlumni(AlumniModel alumniModel);
    AlumniModel createAlumni(AlumniModel alumniModel);

    Set<AlumniModel> fetchAlumni();
//    Set<EventModel> registeredEvents(UUID alumniId);




}
