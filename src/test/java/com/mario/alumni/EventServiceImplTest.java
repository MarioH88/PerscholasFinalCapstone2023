package com.mario.alumni;

import com.mario.alumni.app.dao.AlumniDao;
import com.mario.alumni.app.dao.EventDao;
import com.mario.alumni.app.entity.Alumni;
import com.mario.alumni.app.entity.Event;
import com.mario.alumni.app.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {


    @Mock
    private EventDao eventDao;
    @Mock
    private AlumniDao alumniDao;

    @InjectMocks

    private EventServiceImpl eventServiceImpl;




    @Test
    void testassignAlumniToEvent() {
        Alumni alumni = new Alumni();
        alumni.setAlumniId(UUID.randomUUID());
        Event event = new Event();
        UUID uuid = UUID.randomUUID();

        event.setEventId(uuid);

        when(alumniDao.findById(any())).thenReturn(Optional.of(alumni));
        when(eventDao.findById(any())).thenReturn(Optional.of(event));

        eventServiceImpl.joinEvent(uuid, uuid);
    }



}


