package com.mario.alumni.app.service.impl;

import com.mario.alumni.app.dao.AlumniDao;
import com.mario.alumni.app.dao.RoleDao;
import com.mario.alumni.app.dao.UserDao;
import com.mario.alumni.app.entity.Alumni;
import com.mario.alumni.app.entity.User;
import com.mario.alumni.app.enums.Role;
import com.mario.alumni.app.exception.InvalidRoleChangeException;
import com.mario.alumni.app.model.UserModel;
import com.mario.alumni.app.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final AlumniDao alumniDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, AlumniDao alumniDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.alumniDao = alumniDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User loadUserByEmail(String email) {
        // Implementation for loadUserByEmail
        return userDao.findByEmail(email);
    }

    @Override
    public UserModel loadUserModelByUserId(Long id) {
        User user = userDao.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with Id: " + id));
        return new UserModel(user.getUserId(), user.getEmail(),
                user.getRoles().stream()
                        .map(com.mario.alumni.app.entity.Role::getName)
                        .anyMatch(r->Role.ADMIN.getValue().equals(r)),
                user.getRoles().stream()
                        .map(com.mario.alumni.app.entity.Role::getName)
                        .anyMatch(r->Role.ORGANISER.getValue().equals(r)),
                user.getRoles().stream()
                        .map(com.mario.alumni.app.entity.Role::getName)
                        .anyMatch(r->Role.ALUMNI.getValue().equals(r)));
    }

    @Override
    public User loadUserByUserId(Long id) {
        return userDao.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with Id: " + id));
    }

    @Override
    public List<UserModel> listAllUsers() {
        return userDao
                .findAll().stream()
                .map(user -> new UserModel(user.getUserId(), user.getEmail(),
                        user.getRoles().stream()
                                .map(com.mario.alumni.app.entity.Role::getName)
                                .anyMatch(r->Role.ADMIN.getValue().equals(r)),
                        user.getRoles().stream()
                                .map(com.mario.alumni.app.entity.Role::getName)
                                .anyMatch(r->Role.ORGANISER.getValue().equals(r)),
                        user.getRoles().stream()
                                .map(com.mario.alumni.app.entity.Role::getName)
                                .anyMatch(r->Role.ALUMNI.getValue().equals(r))))
                .collect(Collectors.toList());
    }

    @Override
    public User createUser(String email, String password) {
        // Implementation for createUser
        if(Objects.nonNull(userDao.findByEmail(email)))
            throw new IllegalArgumentException("User Already Exists with email : " + email);
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userDao.save(user);
        return user;
    }

    @Override
    public User createUserIfNotExists(String email, String password, Set<Role> roles) {
        User user = userDao.findByEmail(email);
        if(Objects.nonNull(user)) return user;
        user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        if(roles.contains(Role.ALUMNI) && (roles.contains(Role.ORGANISER) || roles.contains(Role.ADMIN))) {
            throw new InvalidRoleChangeException("Invalid role combination [ADMIN + ALUMNI | ORGANISER + ALUMNI ] ");
        }
        Set<com.mario.alumni.app.entity.Role> newRoles = roles.stream().map(role -> roleDao.findByName(role.getValue())).collect(Collectors.toSet());
        user.setRoles(newRoles);

        userDao.save(user);

        if(roles.contains(Role.ALUMNI)) {
            alumniDao.save(new Alumni("Dummy Name", "Dummy Last Name", user));
        }
        return user;
    }

    @Override
    public User createUser(String email, String password, Set<Role> roles) {
        // Implementation for createUser
        if(Objects.nonNull(userDao.findByEmail(email)))
            throw new IllegalArgumentException("User Already Exists with email : " + email);
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        if(roles.contains(Role.ALUMNI) && (roles.contains(Role.ORGANISER) || roles.contains(Role.ADMIN))) {
            throw new InvalidRoleChangeException("Invalid role combination [ADMIN + ALUMNI | ORGANISER + ALUMNI ] ");
        }
        Set<com.mario.alumni.app.entity.Role> newRoles = roles.stream().map(role -> roleDao.findByName(role.getValue())).collect(Collectors.toSet());
        user.setRoles(newRoles);

        userDao.save(user);

        if(roles.contains(Role.ALUMNI)) {
            alumniDao.save(new Alumni("Dummy Name", "Dummy Last Name", user));
        }
        return user;
    }

    @Override
    public User updateUserByAdmin(String email, Set<Role> roles) {
        User user = userDao.findByEmail(email);
        if(roles.contains(Role.ALUMNI) && (roles.contains(Role.ORGANISER) || roles.contains(Role.ADMIN))) {
            throw new InvalidRoleChangeException("Invalid role combination [ADMIN + ALUMNI | ORGANISER + ALUMNI ] ");
        }
        Set<com.mario.alumni.app.entity.Role> newRoles = roles.stream().map(role -> roleDao.findByName(role.getValue())).collect(Collectors.toSet());
        user.setRoles(newRoles);

        return userDao.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
         userDao.deleteById(id);
    }


    public void assignRoleToUser(String email, Role role) {
        // Implementation for assignRoleToUser
        User user = userDao.findByEmail(email);
        Set<String> superiorRoles = new HashSet<String>(Arrays.asList(Role.ADMIN.getValue(), Role.ORGANISER.getValue()));
        Stream<String> userRolesString = user.getRoles().stream().map(com.mario.alumni.app.entity.Role::getName);
        if(Role.ALUMNI.equals(role) && (userRolesString.anyMatch(superiorRoles::contains))){
            throw new InvalidRoleChangeException("Invalid role combination [ADMIN + ALUMNI | ORGANISER + ALUMNI ] ");
        } else if(superiorRoles.contains(role.getValue())
                && user.getRoles().stream().map(com.mario.alumni.app.entity.Role::getName).anyMatch(r-> Role.ALUMNI.getValue().equals(r))) {
            throw new InvalidRoleChangeException("An alumni can't be organiser or Admin");
        }
        user.addRole(roleDao.findByName(role.getValue()));
        userDao.save(user);
    }


    @Override
    public boolean doesCurrentUserHasRole(Role role) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return currentUser.getRoles().stream().anyMatch(r -> r.getName().equals(role.getValue()));
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.loadUserByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User Not Found");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
            authorities.add(authority);
        });
        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        return userDetails;
    }
}


