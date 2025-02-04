package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.entities.people.Person;
import com.mcommings.campaigner.interfaces.people.IPerson;
import com.mcommings.campaigner.repositories.IWealthRepository;
import com.mcommings.campaigner.repositories.people.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.*;
import static java.util.Objects.isNull;

@Service
public class PersonService implements IPerson {

    private final IPersonRepository personRepository;
    private final IRaceRepository raceRepository;
    private final IWealthRepository wealthRepository;
    private final IAbilityScoreRepository abilityScoreRepository;
    private final IJobAssignmentRepository jobAssignmentRepository;
    private final IEventPlacePersonRepository eventPlacePersonRepository;

    @Autowired
    public PersonService(IPersonRepository personRepository, IRaceRepository raceRepository,
                         IWealthRepository wealthRepository, IAbilityScoreRepository abilityScoreRepository,
                         IJobAssignmentRepository jobAssignmentRepository, IEventPlacePersonRepository eventPlacePersonRepository) {
        this.personRepository = personRepository;
        this.raceRepository = raceRepository;
        this.wealthRepository = wealthRepository;
        this.abilityScoreRepository = abilityScoreRepository;
        this.jobAssignmentRepository = jobAssignmentRepository;
        this.eventPlacePersonRepository = eventPlacePersonRepository;
    }

    @Override
    public List<Person> getPeople() {
        return personRepository.findAll();
    }

    @Override
    public List<Person> getPeopleByCampaignUUID(UUID uuid) {
        return personRepository.findByfk_campaign_uuid(uuid);
    }

    @Override
    @Transactional
    public void savePerson(Person person) throws IllegalArgumentException, DataIntegrityViolationException {
        if (nameIsNullOrEmpty(person.getFirstName()) || nameIsNullOrEmpty(person.getLastName())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (personAlreadyExists(person)) {
            throw new DataIntegrityViolationException(PERSON_EXISTS.message);
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
        if (RepositoryHelper.isForeignKey(getReposWherePersonIsAForeignKey(), FK_PERSON.columnName, personId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }
        personRepository.deleteById(personId);
    }

    @Override
    @Transactional
    public void updatePerson(int personId, Person person) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(personRepository, personId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(person) &&
                RepositoryHelper.foreignKeyIsNotValid(buildReposAndColumnsHashMap(person), person)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }
        Person personToUpdate = RepositoryHelper.getById(personRepository, personId);
        if (person.getFirstName() != null) personToUpdate.setFirstName(person.getFirstName());
        if (person.getLastName() != null) personToUpdate.setLastName(person.getLastName());
        if (person.getAge() >= 0) personToUpdate.setAge(person.getAge());
        if (person.getTitle() != null) personToUpdate.setTitle(person.getTitle());
        if (person.getFk_race() != null) personToUpdate.setFk_race(person.getFk_race());
        if (person.getFk_wealth() != null) personToUpdate.setFk_wealth(person.getFk_wealth());
        if (person.getFk_ability_score() != null) personToUpdate.setFk_ability_score(person.getFk_ability_score());
        if (person.getIsNPC() != null) personToUpdate.setIsNPC(person.getIsNPC());
        if (person.getIsEnemy() != null) personToUpdate.setIsEnemy(person.getIsEnemy());
        if (person.getPersonality() != null) personToUpdate.setPersonality(person.getPersonality());
        if (person.getDescription() != null) personToUpdate.setDescription(person.getDescription());
        if (person.getNotes() != null) personToUpdate.setNotes(person.getNotes());
    }

    private boolean nameIsNullOrEmpty(String name) {
        return isNull(name) || name.isEmpty();
    }

    private boolean personAlreadyExists(Person person) {
        return personRepository.personExists(person).isPresent();
    }

    private List<CrudRepository> getReposWherePersonIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(jobAssignmentRepository, eventPlacePersonRepository));
    }

    private boolean hasForeignKeys(Person person) {
        return person.getFk_race() != null ||
                person.getFk_wealth() != null ||
                person.getFk_ability_score() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(raceRepository, wealthRepository, abilityScoreRepository));
    }

    private HashMap<CrudRepository, String> buildReposAndColumnsHashMap(Person person) {
        HashMap<CrudRepository, String> reposAndColumns = new HashMap<>();

        if (person.getFk_race() != null) {
            reposAndColumns.put(raceRepository, FK_RACE.columnName);
        }
        if (person.getFk_wealth() != null) {
            reposAndColumns.put(wealthRepository, FK_WEALTH.columnName);
        }
        if (person.getFk_ability_score() != null) {
            reposAndColumns.put(abilityScoreRepository, FK_ABILITY_SCORE.columnName);
        }
        return reposAndColumns;
    }
}
