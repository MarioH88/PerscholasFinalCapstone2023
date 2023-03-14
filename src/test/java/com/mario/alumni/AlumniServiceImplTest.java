package com.mario.alumni;

import com.mario.alumni.app.dao.AlumniDao;
import com.mario.alumni.app.entity.Alumni;
import com.mario.alumni.app.service.UserService;
import com.mario.alumni.app.service.impl.AlumniServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)

class AlumniServiceImplTest {

    @InjectMocks
    private AlumniServiceImpl alumniService;

    @Mock
    private AlumniDao alumniDao;

    @Mock
    private UserService userService;

    @Test
    void testupdateAlumni() {
    Alumni alumni = new Alumni();
alumni.setAlumniId(UUID.randomUUID());
alumniService.updateAlumni(alumni);
verify (alumniDao).save(alumni);

}


}