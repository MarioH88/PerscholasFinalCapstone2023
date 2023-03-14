package com.mario.alumni;

import com.mario.alumni.app.dao.RoleDao;
import com.mario.alumni.app.dao.UserDao;
import com.mario.alumni.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class UserModelServiceImplTest {
    @Mock
    private UserDao userDao;

    @Mock
    private User mockedUser;

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void testLoadUserByEmail() {
        userService.loadUserByEmail("email@gmail.com");
        verify(userDao, times(1)).findByEmail(any());
    }

}