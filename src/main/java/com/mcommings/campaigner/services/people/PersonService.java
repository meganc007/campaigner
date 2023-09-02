package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.interfaces.people.IPerson;
import com.mcommings.campaigner.models.people.Person;
import com.mcommings.campaigner.repositories.IWealthRepository;
import com.mcommings.campaigner.repositories.people.IAbilityScoreRepository;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import com.mcommings.campaigner.repositories.people.IRaceRepository;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    }

    @Override
    @Transactional
    public void deletePerson(int personId) throws IllegalArgumentException, DataIntegrityViolationException {

    }

    @Override
    @Transactional
    public void updatePerson(int personId, Person person) throws IllegalArgumentException, DataIntegrityViolationException {

    }
}
