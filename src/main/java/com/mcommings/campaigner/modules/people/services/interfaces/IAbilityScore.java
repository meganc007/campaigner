package com.mcommings.campaigner.modules.people.services.interfaces;

import com.mcommings.campaigner.modules.people.dtos.AbilityScoreDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAbilityScore {

    List<AbilityScoreDTO> getAbilityScores();

    Optional<AbilityScoreDTO> getAbilityScore(int abilityScore);

    List<AbilityScoreDTO> getAbilityScoresByCampaignUUID(UUID uuid);

    void saveAbilityScore(AbilityScoreDTO abilityScore);

    void deleteAbilityScore(int id);

    Optional<AbilityScoreDTO> updateAbilityScore(int id, AbilityScoreDTO abilityScore);
}
