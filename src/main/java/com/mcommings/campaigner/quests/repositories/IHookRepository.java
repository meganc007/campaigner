package com.mcommings.campaigner.quests.repositories;

import com.mcommings.campaigner.quests.entities.Hook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IHookRepository extends JpaRepository<Hook, Integer> {

    Optional<Hook> findByDescription(String description);

    @Query("SELECT h FROM Hook h WHERE h.description = :#{#hook.description}")
    Optional<Hook> hookExists(@Param("hook") Hook hook);

    @Query("SELECT h FROM Hook h WHERE h.fk_campaign_uuid = :uuid")
    List<Hook> findByfk_campaign_uuid(@Param("uuid") UUID uuid);
}
