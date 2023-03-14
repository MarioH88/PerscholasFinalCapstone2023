package com.mario.alumni.app.service;

import com.mario.alumni.app.entity.Event;
import com.mario.alumni.app.model.AlumniModel;
import com.mario.alumni.app.model.EventModel;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventService {

    Event loadEventById(UUID eventId);
    Event createEvent(UUID eventId, String eventName, LocalDate eventDate, String eventType, Long organiserId);

    Event createEventIfNotExists(String eventName, LocalDate eventDate, String eventType, Long organiserId);
    Event createOrUpdateEvent(Event event);

    List<Event> findEventByEventName(String keyword);
    List<AlumniModel> loadAlumniByEventID(UUID eventId);

    void removeEvent(UUID eventId);

    List<Event> fetchEventForAlumni(UUID alumniId);

  void joinEvent(UUID eventId, UUID alumniId);
  void deRegisterAlumniFromEvent(UUID eventId, UUID alumniId);

    List<EventModel> fetchAll();

    EventModel createEvent(EventModel eventModel);

    EventModel loadEventModelById(UUID eventId);
    EventModel updateEvent(EventModel eventModel);


}
