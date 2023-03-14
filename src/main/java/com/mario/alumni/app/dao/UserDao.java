package com.mario.alumni.app.dao;


import com.mario.alumni.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
