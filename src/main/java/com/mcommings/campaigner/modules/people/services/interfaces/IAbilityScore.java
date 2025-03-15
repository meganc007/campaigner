package com.mcommings.campaigner.modules.people.services.interfaces;

import com.mcommings.campaigner.modules.people.entities.AbilityScore;

import java.util.List;
import java.util.UUID;

public interface IAbilityScore {

    List<AbilityScore> getAbilityScores();

    List<AbilityScore> getAbilityScoresByCampaignUUID(UUID uuid);

    void saveAbilityScore(AbilityScore abilityScore);

    void deleteAbilityScore(int id);

    void updateAbilityScore(int id, AbilityScore abilityScore);
}
