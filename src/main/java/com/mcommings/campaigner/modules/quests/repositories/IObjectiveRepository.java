package com.mcommings.campaigner.modules.quests.repositories;

import com.mcommings.campaigner.modules.quests.entities.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IObjectiveRepository extends JpaRepository<Objective, Integer> {

    Optional<Objective> findByDescription(String description);

    @Query("SELECT o FROM Objective o WHERE o.description = :#{#objective.description}")
    Optional<Objective> objectiveExists(@Param("objective") Objective objective);

    @Query("SELECT o FROM Objective o WHERE o.fk_campaign_uuid = :uuid")
    List<Objective> findByfk_campaign_uuid(@Param("uuid") UUID uuid);
}
