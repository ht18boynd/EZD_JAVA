package com.ezd.repository;

//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezd.models.ERole;
import com.ezd.models.Role;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(ERole name);
}
