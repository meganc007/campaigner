package com.mcommings.campaigner.repositories.quests;

import com.mcommings.campaigner.entities.quests.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IOutcomeRepository extends JpaRepository<Outcome, Integer> {

    Optional<Outcome> findByDescription(String description);

    @Query("SELECT o FROM Outcome o WHERE o.fk_campaign_uuid = :uuid")
    List<Outcome> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT o FROM Outcome o WHERE o.description = :#{#outcome.description}")
    Optional<Outcome> outcomeExists(@Param("outcome") Outcome outcome);
}
