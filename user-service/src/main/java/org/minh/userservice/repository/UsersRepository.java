package org.minh.userservice.repository;


import org.minh.userservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndPassword(String email, String password);
    Optional<Users> findByEmail(String email);
}