package com.mcommings.campaigner.modules.people.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.people.dtos.PersonDTO;
import com.mcommings.campaigner.modules.people.entities.Person;
import com.mcommings.campaigner.modules.people.mappers.PersonMapper;
import com.mcommings.campaigner.modules.people.repositories.IPersonRepository;
import com.mcommings.campaigner.modules.people.services.interfaces.IPerson;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class PersonService implements IPerson {

    private final IPersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public List<PersonDTO> getPeople() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::mapToPersonDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PersonDTO> getPerson(int personId) {
        return personRepository.findById(personId)
                .map(personMapper::mapToPersonDto);
    }

    @Override
    public List<PersonDTO> getPeopleByCampaignUUID(UUID uuid) {

        return personRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(personMapper::mapToPersonDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonDTO> getPeopleByRace(int raceId) {
        return personRepository.findByfk_race(raceId)
                .stream()
                .map(personMapper::mapToPersonDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonDTO> getPeopleByAbilityScore(int abilityScoreId) {
        return personRepository.findByfk_ability_score(abilityScoreId)
                .stream()
                .map(personMapper::mapToPersonDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonDTO> getPeopleByEnemyStatus(boolean isEnemy) {
        return personRepository.findByIsEnemy(isEnemy)
                .stream()
                .map(personMapper::mapToPersonDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonDTO> getPeopleByNPCStatus(boolean isNPC) {
        return personRepository.findByIsNPC(isNPC)
                .stream()
                .map(personMapper::mapToPersonDto)
                .collect(Collectors.toList());
    }

    @Override
    public void savePerson(PersonDTO person) throws IllegalArgumentException, DataIntegrityViolationException {
        if (nameIsNullOrEmpty(person.getFirstName())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (personAlreadyExists(personMapper.mapFromPersonDto(person))) {
            throw new DataIntegrityViolationException(PERSON_EXISTS.message);
        }
        personMapper.mapToPersonDto(
                personRepository.save(personMapper.mapFromPersonDto(person)
                ));
    }

    @Override
    public void deletePerson(int personId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(personRepository, personId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }

        personRepository.deleteById(personId);
    }

    @Override
    public Optional<PersonDTO> updatePerson(int personId, PersonDTO person) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(personRepository, personId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (nameIsNullOrEmpty(person.getFirstName())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (nameAlreadyExists(person)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return personRepository.findById(personId).map(foundPerson -> {
            if (person.getFirstName() != null) foundPerson.setFirstName(person.getFirstName());
            if (person.getLastName() != null) foundPerson.setLastName(person.getLastName());
            if (person.getAge() >= 0) foundPerson.setAge(person.getAge());
            if (person.getTitle() != null) foundPerson.setTitle(person.getTitle());
            if (person.getFk_race() != null) foundPerson.setFk_race(person.getFk_race());
            if (person.getFk_wealth() != null) foundPerson.setFk_wealth(person.getFk_wealth());
            if (person.getFk_ability_score() != null) foundPerson.setFk_ability_score(person.getFk_ability_score());
            if (person.getIsNPC() != null) foundPerson.setIsNPC(person.getIsNPC());
            if (person.getIsEnemy() != null) foundPerson.setIsEnemy(person.getIsEnemy());
            if (person.getPersonality() != null) foundPerson.setPersonality(person.getPersonality());
            if (person.getDescription() != null) foundPerson.setDescription(person.getDescription());
            if (person.getNotes() != null) foundPerson.setNotes(person.getNotes());

            return personMapper.mapToPersonDto(personRepository.save(foundPerson));
        });
    }

    private boolean nameIsNullOrEmpty(String name) {
        return isNull(name) || name.isEmpty();
    }

    private boolean nameAlreadyExists(PersonDTO person) {
        return personRepository
                .findByFirstNameAndLastName(person.getFirstName(), person.getLastName())
                .isPresent();
    }

    private boolean personAlreadyExists(Person person) {
        return personRepository.personExists(person).isPresent();
    }
}
