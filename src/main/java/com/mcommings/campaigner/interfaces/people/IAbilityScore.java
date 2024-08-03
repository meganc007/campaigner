package com.mcommings.campaigner.interfaces.people;

import com.mcommings.campaigner.models.people.AbilityScore;

import java.util.List;
import java.util.UUID;

public interface IAbilityScore {

    List<AbilityScore> getAbilityScores();

    List<AbilityScore> getAbilityScoresByCampaignUUID(UUID uuid);

    void saveAbilityScore(AbilityScore abilityScore);

    void deleteAbilityScore(int id);

    void updateAbilityScore(int id, AbilityScore abilityScore);
}
