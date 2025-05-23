package com.mcommings.campaigner.modules.quests.repositories;

import com.mcommings.campaigner.modules.quests.entities.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IOutcomeRepository extends JpaRepository<Outcome, Integer> {

    @Query("SELECT o FROM Outcome o WHERE LOWER(o.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Outcome> searchByDescription(@Param("keyword") String keyword);

    @Query("SELECT o FROM Outcome o WHERE o.fk_campaign_uuid = :uuid")
    List<Outcome> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT o FROM Outcome o WHERE o.description = :#{#outcome.description}")
    Optional<Outcome> outcomeExists(@Param("outcome") Outcome outcome);
}
