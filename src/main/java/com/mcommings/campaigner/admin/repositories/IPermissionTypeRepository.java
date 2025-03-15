package com.mcommings.campaigner.admin.repositories;

import com.mcommings.campaigner.admin.entities.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPermissionTypeRepository extends JpaRepository<PermissionType, Integer> {
    Optional<PermissionType> findByName(String name);

//    @Query("SELECT p FROM PermissionType p WHERE " +
//            "p.name = :#{#permission.name} AND " +
//            "p.description = :#{#permission.description}")
//    Optional<PermissionType> permissionTypeAlreadyExists(@Param("permissionType") PermissionType permissionType);
}
