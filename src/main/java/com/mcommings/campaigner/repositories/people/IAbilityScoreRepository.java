package com.mcommings.campaigner.repositories.people;

import com.mcommings.campaigner.models.people.AbilityScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

}
