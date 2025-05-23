package com.mcommings.campaigner.modules.people.services.interfaces;

import com.mcommings.campaigner.modules.people.dtos.NamedMonsterDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface INamedMonster {

    List<NamedMonsterDTO> getNamedMonsters();

    Optional<NamedMonsterDTO> getNamedMonster(int namedMonsterId);

    List<NamedMonsterDTO> getNamedMonstersByCampaignUUID(UUID uuid);

    List<NamedMonsterDTO> getNamedMonstersByAbilityScore(int abilityScoreId);

    List<NamedMonsterDTO> getNamedMonstersByGenericMonster(int id);

    List<NamedMonsterDTO> getNamedMonstersByEnemyStatus(boolean isEnemy);

    void saveNamedMonster(NamedMonsterDTO namedMonster);

    void deleteNamedMonster(int id);

    Optional<NamedMonsterDTO> updateNamedMonster(int id, NamedMonsterDTO namedMonster);
}
