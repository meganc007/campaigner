package com.mcommings.campaigner.modules.calendar.repositories;

import com.mcommings.campaigner.modules.calendar.entities.Sun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ISunRepository extends JpaRepository<Sun, Integer> {
    Optional<Sun> findByName(String name);

    @Query("SELECT s FROM Sun s WHERE s.fk_campaign_uuid = :uuid")
    List<Sun> findByfk_campaign_uuid(@Param("uuid") UUID uuid);
}
