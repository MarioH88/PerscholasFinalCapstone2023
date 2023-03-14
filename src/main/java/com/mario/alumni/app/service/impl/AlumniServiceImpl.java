package com.mario.alumni.app.service.impl;

import com.mario.alumni.app.dao.AlumniDao;
import com.mario.alumni.app.entity.Alumni;
import com.mario.alumni.app.entity.User;
import com.mario.alumni.app.enums.Role;
import com.mario.alumni.app.model.AlumniModel;
import com.mario.alumni.app.model.EventModel;
import com.mario.alumni.app.service.AlumniService;
import com.mario.alumni.app.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlumniServiceImpl implements AlumniService {

    private final AlumniDao alumniDao;

    private final UserService userService;


    public AlumniServiceImpl(AlumniDao alumniDao, UserService userService) {
        this.alumniDao = alumniDao;
        this.userService = userService;
    }


    @Override
    public void removeAlumni(UUID alumniId) {
        Alumni alumni = alumniDao.findById(alumniId)
                .orElseThrow(() -> new EntityNotFoundException("Alumni with id " + alumniId + " not found"));
        alumniDao.delete(alumni);
    }


    @Override
    public List<Alumni> findAlumniByName(String name) {
        return alumniDao.findAlumniByName(name);
    }

    @Override
    public Alumni loadAlumniByEmail(String email) {
        return Optional.ofNullable(alumniDao.findAlumniByEmail(email)).orElseThrow(() -> new EntityNotFoundException("Alumni with email " + email + " not found"));
    }

    @Override
    public AlumniModel loadAlumniById(UUID id) {
        Alumni alumni = alumniDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Alumni with id " + id + " not found"));
        return new AlumniModel(alumni.getAlumniId(), alumni.getFirstName(), alumni.getLastName(), alumni.getUser().getEmail(), alumni.getEvents().size());
    }

    @Override
    public void deleteAlumniById(UUID alumniId) {
        alumniDao.deleteById(alumniId);
    }

    public void setAlumniId(UUID alumniId) {
        UUID localAlumniId = alumniId;
        // Do something with localAlumniId
    }


    @Override
    public Alumni createAlumni(UUID alumniId, String firstName, String lastName, String email, String password, User user_) {
        User user = userService.createUser(email, password);
        userService.assignRoleToUser(user.getEmail(), Role.ALUMNI);
        return alumniDao.save(new Alumni( firstName, lastName, user));
    }

    @Override
    public Alumni createAlumniIfNotExists(UUID alumniId, String firstName, String lastName, String email, String password, User user) {
        Optional<Alumni> alumni = Optional.ofNullable(alumniDao.findAlumniByEmail(email));
        if(alumni.isPresent()) return alumni.get();
        User currentUser = userService.loadUserByEmail(user.getEmail());
        return alumniDao.save(new Alumni( alumniId, firstName, lastName, currentUser));
    }

    @Override
    public Alumni updateAlumni(Alumni alumni) {
        return alumniDao.save(alumni);
    }

    @Override
    public AlumniModel updateAlumni(AlumniModel alumniModel) {
        Alumni alumni = alumniDao.findById(alumniModel.getAlumniId()).orElseThrow(() -> new EntityNotFoundException("Alumni with id " + alumniModel.getAlumniId() + " not found"));
        alumni.setFirstName(alumniModel.getFirstName());
        alumni.setLastName(alumniModel.getLastName());
        alumniDao.save(alumni);
        return alumniModel;
    }

    @Override
    public AlumniModel createAlumni(AlumniModel alumniModel) {

        User user = userService.loadUserByEmail(alumniModel.getEmail());
        if(user == null)
            throw new IllegalArgumentException("User is not registered in system");
        if(user.getRoles().stream().map(com.mario.alumni.app.entity.Role::getName).anyMatch(role -> Role.ADMIN.getValue().equals(role))
                || user.getRoles().stream().map(com.mario.alumni.app.entity.Role::getName).anyMatch(role -> Role.ORGANISER.getValue().equals(role))) {
            throw new IllegalArgumentException("Admin or Organiser cannot be alumni. Request Admin to change your role");
        }
        Alumni currentAlumni = alumniDao.findAlumniByEmail(alumniModel.getEmail());
        if(Objects.nonNull(currentAlumni))
            throw new IllegalArgumentException("Your are already registered as alumni");
        Alumni alumni = new Alumni(alumniModel.getFirstName(), alumniModel.getLastName(), user);
        alumni.setFirstName(alumniModel.getFirstName());
        alumni.setLastName(alumniModel.getLastName());
        alumniDao.save(alumni);
        return alumniModel;
    }

    @Override
    public Set<AlumniModel> fetchAlumni() {
        return alumniDao.findAll()
                .stream()
                .map(a -> new AlumniModel(a.getAlumniId(), a.getFirstName(), a.getLastName(), a.getUser().getEmail(), a.getEvents().size()))
                .collect(Collectors.toSet());

    }

}