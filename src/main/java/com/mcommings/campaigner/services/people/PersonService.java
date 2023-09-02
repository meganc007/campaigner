package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.interfaces.people.IPerson;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.people.Person;
import com.mcommings.campaigner.repositories.IWealthRepository;
import com.mcommings.campaigner.repositories.people.IAbilityScoreRepository;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import com.mcommings.campaigner.repositories.people.IRaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static java.util.Objects.isNull;

@Service
public class PersonService implements IPerson {

    private final IPersonRepository personRepository;
    private final IRaceRepository raceRepository;
    private final IWealthRepository wealthRepository;
    private final IAbilityScoreRepository abilityScoreRepository;

    @Autowired
    public PersonService(IPersonRepository personRepository, IRaceRepository raceRepository,
                         IWealthRepository wealthRepository, IAbilityScoreRepository abilityScoreRepository) {
        this.personRepository = personRepository;
        this.raceRepository = raceRepository;
        this.wealthRepository = wealthRepository;
        this.abilityScoreRepository = abilityScoreRepository;
    }

    @Override
    public List<Person> getPeople() {
        return personRepository.findAll();
    }

    @Override
    @Transactional
    public void savePerson(Person person) throws IllegalArgumentException, DataIntegrityViolationException {
        if (nameIsNullOrEmpty(person.getFirstName()) || nameIsNullOrEmpty(person.getLastName())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (personAlreadyExists(person)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        if (hasForeignKeys(person) &&
                RepositoryHelper.foreignKeyIsNotValid(personRepository, getListOfForeignKeyRepositories(), person)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        personRepository.saveAndFlush(person);
    }

    @Override
    @Transactional
    public void deletePerson(int personId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(personRepository, personId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        //TODO: fk check when Person is a fk
        personRepository.deleteById(personId);
    }

    @Override
    @Transactional
    public void updatePerson(int personId, Person person) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(personRepository, personId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(person) &&
                RepositoryHelper.foreignKeyIsNotValid(personRepository, getListOfForeignKeyRepositories(), person)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        Person personToUpdate = RepositoryHelper.getById(personRepository, personId);
        personToUpdate.setFirstName(person.getFirstName());
        personToUpdate.setLastName(person.getLastName());
        personToUpdate.setAge(person.getAge());
        personToUpdate.setTitle(person.getTitle());
        personToUpdate.setFk_race(person.getFk_race());
        personToUpdate.setFk_wealth(person.getFk_wealth());
        personToUpdate.setFk_ability_score(person.getFk_ability_score());
        personToUpdate.setIsNPC(person.getIsNPC());
        personToUpdate.setIsEnemy(person.getIsEnemy());
        personToUpdate.setPersonality(person.getPersonality());
        personToUpdate.setDescription(person.getDescription());
        personToUpdate.setNotes(person.getNotes());
    }

    private boolean nameIsNullOrEmpty(String name) {
        return isNull(name) || name.isEmpty();
    }

    private boolean personAlreadyExists(Person person) {
        return personRepository.personExists(person).isPresent();
    }

    private boolean hasForeignKeys(Person person) {
        return person.getFk_race() != null ||
                person.getFk_wealth() != null ||
                person.getFk_ability_score() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(raceRepository, wealthRepository, abilityScoreRepository));
    }
}
