package com.mcommings.campaigner.people.services.interfaces;

import com.mcommings.campaigner.people.entities.GenericMonster;

import java.util.List;
import java.util.UUID;

public interface IGenericMonster {

    List<GenericMonster> getGenericMonsters();

    List<GenericMonster> getGenericMonstersByCampaignUUID(UUID uuid);

    void saveGenericMonster(GenericMonster genericMonster);

    void deleteGenericMonster(int id);

    void updateGenericMonster(int id, GenericMonster genericMonster);
}
