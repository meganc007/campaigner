package com.mcommings.campaigner.interfaces.people;

import com.mcommings.campaigner.entities.people.NamedMonster;

import java.util.List;
import java.util.UUID;

public interface INamedMonster {

    List<NamedMonster> getNamedMonsters();

    List<NamedMonster> getNamedMonstersByCampaignUUID(UUID uuid);

    List<NamedMonster> getNamedMonstersByGenericMonster(int id);

    void saveNamedMonster(NamedMonster namedMonster);

    void deleteNamedMonster(int id);

    void updateNamedMonster(int id, NamedMonster namedMonster);
}
