package com.mario.alumni.app.service;


import com.mario.alumni.app.entity.User;
import com.mario.alumni.app.enums.Role;
import com.mario.alumni.app.model.UserModel;

import java.util.List;
import java.util.Set;

public interface UserService {



    User loadUserByEmail(String email);
    UserModel loadUserModelByUserId(Long id);
    User loadUserByUserId(Long id);

    List<UserModel> listAllUsers();

    User createUser(String email, String password);
    User createUserIfNotExists(String email, String password, Set<Role> roles);

    User createUser(String email, String password, Set<Role> roles);

    User updateUserByAdmin(String email, Set<Role> roles);

    void assignRoleToUser(String email, Role roleName);

    void deleteUserById(Long userId);

    boolean doesCurrentUserHasRole(Role roleName);

}
