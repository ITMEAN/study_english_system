package org.minh.gatewayservice.repository;


import org.minh.gatewayservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}