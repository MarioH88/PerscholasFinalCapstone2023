package com.mario.alumni;

import com.mario.alumni.app.dao.RoleDao;
import com.mario.alumni.app.enums.Role;
import com.mario.alumni.app.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void testCreateRole() {
        roleService.createRole(Role.ADMIN);
        verify(roleDao, times(1)).save(any());
    }
}