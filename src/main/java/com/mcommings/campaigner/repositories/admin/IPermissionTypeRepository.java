package com.mcommings.campaigner.repositories.admin;

import com.mcommings.campaigner.models.admin.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPermissionTypeRepository extends JpaRepository<PermissionType, Integer> {
    Optional<PermissionType> findByName(String name);
}
