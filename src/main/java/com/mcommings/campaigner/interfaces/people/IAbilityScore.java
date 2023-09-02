package com.mcommings.campaigner.interfaces.people;

import com.mcommings.campaigner.models.people.AbilityScore;

import java.util.List;

public interface IAbilityScore {

    List<AbilityScore> getAbilityScores();

    void saveAbilityScore(AbilityScore abilityScore);

    void deleteAbilityScore(int id);

    void updateAbilityScore(int id, AbilityScore abilityScore);
}
