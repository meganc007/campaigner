package com.mcommings.campaigner.admin.repositories;

import com.mcommings.campaigner.admin.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);
}
