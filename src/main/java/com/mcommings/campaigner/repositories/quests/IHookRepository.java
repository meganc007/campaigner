package com.mcommings.campaigner.repositories.quests;

import com.mcommings.campaigner.models.quests.Hook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IHookRepository extends JpaRepository<Hook, Integer> {

    Optional<Hook> findByDescription(String description);

    @Query("SELECT h FROM Hook h WHERE h.description = :#{#hook.description}")
    Optional<Hook> hookExists(@Param("hook") Hook hook);
}
