package com.mario.alumni.app.dao;


import com.mario.alumni.app.entity.Alumni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Repository
public interface AlumniDao extends JpaRepository<Alumni, UUID> {

    @Query(value = "select a from Alumni as a where a.firstName like %:name% or a.lastName like %:name%")
    List<Alumni> findAlumniByName(@Param("name") String name);

    @Query(value = "select a.* from alumni a JOIN users u ON a.user_id = u.user_id where u.email=:email", nativeQuery = true)
    Alumni findAlumniByEmail(String email);

}