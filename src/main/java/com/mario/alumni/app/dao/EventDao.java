package com.mario.alumni.app.dao;

import com.mario.alumni.app.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventDao extends JpaRepository<Event, UUID> {

    List<Event> findEventByEventName(String keyword);

    @Query(value = "SELECT e FROM Event e JOIN e.alumni a WHERE a.alumniId = :alumniId")
    List<Event> findByAlumniId(UUID alumniId);

    void deleteById(UUID eventId);




}



