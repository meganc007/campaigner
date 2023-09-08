package com.mcommings.campaigner.repositories.quests;

import com.mcommings.campaigner.models.quests.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IObjectiveRepository extends JpaRepository<Objective, Integer> {

    Optional<Objective> findByDescription(String description);

    @Query("SELECT o FROM Objective o WHERE o.description = :#{#objective.description}")
    Optional<Objective> objectiveExists(@Param("objective") Objective objective);
}
