package com.mcommings.campaigner.modules.admin.repositories;

import com.mcommings.campaigner.modules.admin.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Integer> {

    @Query("SELECT p FROM Permission p WHERE p.fk_permission_type = :id")
    List<Permission> findByfk_permission_type(@Param("id") Integer id);

    @Query("SELECT p FROM Permission p WHERE p.fk_campaign_uuid = :uuid")
    List<Permission> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT p FROM Permission p WHERE p.fk_user_uuid = :uuid")
    List<Permission> findByfk_user_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT p FROM Permission p WHERE " +
            "p.fk_permission_type = :#{#permission.fk_permission_type} AND " +
            "p.fk_campaign_uuid = :#{#permission.fk_campaign_uuid} AND " +
            "p.fk_user_uuid = :#{#permission.fk_user_uuid}")
    Optional<Permission> permissionAlreadyExists(@Param("permission") Permission permission);
}
