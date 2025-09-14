package com.company.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.blog.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
