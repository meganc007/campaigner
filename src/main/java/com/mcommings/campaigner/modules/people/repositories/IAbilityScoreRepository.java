package com.mcommings.campaigner.modules.people.repositories;

import com.mcommings.campaigner.modules.people.entities.AbilityScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IAbilityScoreRepository extends JpaRepository<AbilityScore, Integer> {

    @Query("SELECT a FROM AbilityScore a WHERE " +
            "a.strength = :#{#abilityScore.strength} AND " +
            "a.dexterity = :#{#abilityScore.dexterity} AND " +
            "a.constitution = :#{#abilityScore.constitution} AND " +
            "a.intelligence = :#{#abilityScore.intelligence} AND " +
            "a.wisdom = :#{#abilityScore.wisdom} AND " +
            "a.charisma = :#{#abilityScore.charisma}")
    Optional<AbilityScore> abilityScoreExists(@Param("abilityScore") AbilityScore abilityScore);

    @Query("SELECT a FROM AbilityScore a WHERE a.fk_campaign_uuid = :uuid")
    List<AbilityScore> findByfk_campaign_uuid(@Param("uuid") UUID uuid);
}
