package com.ashish.Authservice.repository;

import com.ashish.Authservice.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Roles,Integer> {
   Set< Roles> findByName(String name);
}
