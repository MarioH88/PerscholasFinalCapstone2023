package com.mario.alumni.app.service;

import com.mario.alumni.app.entity.Role;

public interface RoleService {

    Role loadRoleByName(com.mario.alumni.app.enums.Role roleName);

    Role createRole(com.mario.alumni.app.enums.Role roleName);
    Role createRoleIfNotExists(com.mario.alumni.app.enums.Role roleName);
}
