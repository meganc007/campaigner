package com.mcommings.campaigner.modules.people.services.interfaces;

import com.mcommings.campaigner.modules.people.dtos.PersonDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPerson {

    List<PersonDTO> getPeople();

    Optional<PersonDTO> getPerson(int personId);

    List<PersonDTO> getPeopleByCampaignUUID(UUID uuid);

    List<PersonDTO> getPeopleByRace(int raceId);

    List<PersonDTO> getPeopleByAbilityScore(int abilityScoreId);

    List<PersonDTO> getPeopleByEnemyStatus(boolean isEnemy);

    List<PersonDTO> getPeopleByNPCStatus(boolean isNPC);

    void savePerson(PersonDTO person);

    void deletePerson(int personId);

    Optional<PersonDTO> updatePerson(int personId, PersonDTO person);
}
