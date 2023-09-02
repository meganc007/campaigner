package com.mcommings.campaigner.interfaces.people;

import com.mcommings.campaigner.models.people.Person;

import java.util.List;

public interface IPerson {

    List<Person> getPeople();

    void savePerson(Person person);

    void deletePerson(int personId);

    void updatePerson(int personId, Person person);
}
