package com.mcommings.campaigner.interfaces.people;

import com.mcommings.campaigner.entities.people.Person;

import java.util.List;
import java.util.UUID;

public interface IPerson {

    List<Person> getPeople();

    List<Person> getPeopleByCampaignUUID(UUID uuid);

    void savePerson(Person person);

    void deletePerson(int personId);

    void updatePerson(int personId, Person person);
}
