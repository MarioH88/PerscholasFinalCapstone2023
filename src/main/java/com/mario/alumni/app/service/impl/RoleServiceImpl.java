package com.mario.alumni.app.service.impl;

import com.mario.alumni.app.dao.RoleDao;
import com.mario.alumni.app.entity.Role;
import com.mario.alumni.app.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role loadRoleByName(com.mario.alumni.app.enums.Role role) {
        return roleDao.findByName(role.getValue());
    }

    @Override
    public Role createRole(com.mario.alumni.app.enums.Role role) {
        return roleDao.save(new Role(role.getValue()));
    }

    @Override
    public Role createRoleIfNotExists(com.mario.alumni.app.enums.Role roleName) {
        Role role = loadRoleByName(roleName);
        if(Objects.nonNull(role)) return role;

        return createRole(roleName);
    }
}
