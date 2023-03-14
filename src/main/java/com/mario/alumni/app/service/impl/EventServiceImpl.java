package com.mario.alumni.app.service.impl;

import com.mario.alumni.app.dao.AlumniDao;
import com.mario.alumni.app.dao.EventDao;
import com.mario.alumni.app.entity.Alumni;
import com.mario.alumni.app.entity.Event;
import com.mario.alumni.app.entity.User;
import com.mario.alumni.app.enums.Role;
import com.mario.alumni.app.model.AlumniModel;
import com.mario.alumni.app.model.EventModel;
import com.mario.alumni.app.model.UserModel;
import com.mario.alumni.app.service.EventService;
import com.mario.alumni.app.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventDao eventDao;
    private final UserService UserService;
    private final AlumniDao alumniDao;

    public EventServiceImpl(EventDao eventDao, UserService UserService, AlumniDao alumniDao) {
        this.eventDao = eventDao;
        this.UserService = UserService;
        this.alumniDao = alumniDao;
    }


    @Override
    public Event loadEventById(UUID eventId) {
        return eventDao.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event with id" + eventId + " Not Found"));
    }

    @Override
    public Event createOrUpdateEvent(Event event) {
        return eventDao.save(event);
    }


    @Override
    public List<Event> findEventByEventName(String keyword) {
        return eventDao.findEventByEventName(keyword);
    }

    @Override
    public List<Event> fetchEventForAlumni(UUID alumniId) {
        return eventDao.findByAlumniId(alumniId);
    }


    @Override
    public void removeEvent(UUID eventId) { eventDao.deleteById(eventId); }

    @Override
    public Event createEvent(UUID eventId, String eventName, LocalDate eventDate, String eventType, Long userId) {
        User organiser = UserService.loadUserByUserId(userId);
        Event event = new Event(eventId, eventName, eventDate, eventType, organiser);
        return eventDao.save(event);
    }

    @Override
    public Event createEventIfNotExists(String eventName, LocalDate eventDate, String eventType, Long organiserId) {
        List<Event> events = eventDao.findEventByEventName(eventName);
        if(!CollectionUtils.isEmpty(events))return events.get(0);
        User organiser = UserService.loadUserByUserId(organiserId);
        Event event = new Event(eventName, eventType, eventDate, organiser);
        return eventDao.save(event);

    }

    @Override
    public List<EventModel> fetchAll() {
        return eventDao.findAll().stream().map(this::convertToEventModel).collect(Collectors.toList());
    }

    private EventModel convertToEventModel(Event event) {
        EventModel eventModel = new EventModel();
        eventModel.setEventId(event.getEventId());
        eventModel.setEventName(event.getEventName());
        eventModel.setEventType(event.getEventType());
        eventModel.setEventDate(event.getEventDate());
        User organiser = event.getOrganiser();
        UserModel organiserModel = new UserModel(organiser.getUserId(), organiser.getEmail(),
                organiser.getRoles().stream()
                        .map(com.mario.alumni.app.entity.Role::getName)
                        .anyMatch(r-> Role.ADMIN.getValue().equals(r)),
                organiser.getRoles().stream()
                        .map(com.mario.alumni.app.entity.Role::getName)
                        .anyMatch(r->Role.ORGANISER.getValue().equals(r)),
                organiser.getRoles().stream()
                        .map(com.mario.alumni.app.entity.Role::getName)
                        .anyMatch(r->Role.ALUMNI.getValue().equals(r)));
        eventModel.setOrganiser(organiserModel);
        eventModel.setAlumni(event.getAlumni().stream().map(a -> new AlumniModel(a.getAlumniId(), a.getFirstName(), a.getLastName(), a.getUser().getEmail(), a.getEvents().size())).collect(Collectors.toSet()));
        return eventModel;
    }

    private Event convertToEvent(EventModel eventModel) {
        Event event = new Event();
        event.setEventName(eventModel.getEventName());
        event.setEventType(eventModel.getEventType());
        event.setEventDate(eventModel.getEventDate());
        User organiser =  UserService.loadUserByEmail(eventModel.getOrganiser().getEmail());
        event.setOrganiser(organiser);
        return event;
    }

    @Override
    public void joinEvent(UUID eventId, UUID alumniId) {
        Event event = eventDao.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event with id " + eventId + " not found"));
        if(event.getAlumni().stream().map(Alumni::getAlumniId).noneMatch(id -> id==alumniId)) {
            Alumni alumni = alumniDao.findById(alumniId).orElseThrow(() -> new EntityNotFoundException("Alumni with id " + alumniId + " not found"));
            event.assignAlumniToEvent(alumni);
            eventDao.save(event);
        }
    }

    @Override
    public void deRegisterAlumniFromEvent(UUID eventId, UUID alumniId) {
        Event event = eventDao.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event with id " + eventId + " not found"));
        Set<Alumni> alumnis = event.getAlumni();
        alumnis.removeIf(alumni -> alumni.getAlumniId().equals(alumniId));
        event.setAlumni(alumnis);
        eventDao.save(event);
    }

    @Override
    public EventModel createEvent(EventModel eventModel) {
        return convertToEventModel(eventDao.save(convertToEvent(eventModel)));
    }

    @Override
    public EventModel loadEventModelById(UUID eventId) {
        return convertToEventModel(eventDao.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event with id" + eventId + " Not Found")));
    }

    @Override
    public EventModel updateEvent(EventModel eventModel) {
        Event event = loadEventById(eventModel.getEventId());
        event.setEventName(eventModel.getEventName());
        event.setEventType(eventModel.getEventType());
        event.setEventDate(eventModel.getEventDate());
        User organiser =  UserService.loadUserByEmail(eventModel.getOrganiser().getEmail());
        event.setOrganiser(organiser);
        return convertToEventModel(eventDao.save(event));
    }

    @Override
    public List<AlumniModel> loadAlumniByEventID(UUID eventId) {
        return eventDao.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event with id" + eventId + " Not Found"))
                .getAlumni().stream()
                .map(alumni -> new AlumniModel(alumni.getAlumniId(), alumni.getFirstName(), alumni.getLastName(), alumni.getUser().getEmail(), alumni.getEvents().size()))
                .collect(Collectors.toList());
    }
}



