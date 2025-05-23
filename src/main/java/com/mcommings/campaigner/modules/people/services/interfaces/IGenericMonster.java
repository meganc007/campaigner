package com.mcommings.campaigner.modules.people.services.interfaces;

import com.mcommings.campaigner.modules.people.dtos.GenericMonsterDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IGenericMonster {

    List<GenericMonsterDTO> getGenericMonsters();

    Optional<GenericMonsterDTO> getGenericMonster(int genericMonsterId);

    List<GenericMonsterDTO> getGenericMonstersByAbilityScore(int abilityScoreId);

    List<GenericMonsterDTO> getGenericMonstersByCampaignUUID(UUID uuid);

    void saveGenericMonster(GenericMonsterDTO genericMonster);

    void deleteGenericMonster(int id);

    Optional<GenericMonsterDTO> updateGenericMonster(int id, GenericMonsterDTO genericMonster);
}
