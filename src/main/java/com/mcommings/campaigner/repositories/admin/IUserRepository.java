package com.mcommings.campaigner.repositories.admin;

import com.mcommings.campaigner.entities.admin.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByFirstName(String name);

    void deleteByUuid(UUID uuid);

    Boolean existsByUuid(UUID uuid);

    @Query("SELECT u FROM User u WHERE u.fk_role = :id")
    List<User> findByfk_role(@Param("id") Integer id);

    @Query("SELECT u FROM User u WHERE " +
            "u.firstName = :#{#user.firstName} AND " +
            "u.lastName = :#{#user.lastName} AND " +
            "u.email = :#{#user.email} AND " +
            "u.uuid = :#{#user.uuid}")
    Optional<User> userExists(@Param("user") User user);
}
