package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.models.people.Person;
import com.mcommings.campaigner.repositories.IWealthRepository;
import com.mcommings.campaigner.repositories.people.IAbilityScoreRepository;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import com.mcommings.campaigner.repositories.people.IRaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonTest {

    @Mock
    private IPersonRepository personRepository;
    @Mock
    private IRaceRepository raceRepository;
    @Mock
    private IWealthRepository wealthRepository;
    @Mock
    private IAbilityScoreRepository abilityScoreRepository;

    @InjectMocks
    private PersonService personService;
    
    @Test
    public void whenThereArePeople_getPeople_ReturnsPeople() {
        List<Person> people = new ArrayList<>();
        people.add(new Person(1, "First Name 1", "Last Name 1", 1, "Title", true, false, "Personality", "Description", "Notes"));
        people.add(new Person(2, "First Name 2", "Last Name 2", 4, "Title", true, false, "Personality", "Description", "Notes"));
        people.add(new Person(3, "First Name 3", "Last Name 3", 233, "Title", 2, 4, 1, true, false, "Personality", "Description", "Notes"));
        when(personRepository.findAll()).thenReturn(people);

        List<Person> result = personService.getPeople();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(people, result);
    }

    @Test
    public void whenThereAreNoPeople_getPeople_ReturnsNothing() {
        List<Person> people = new ArrayList<>();
        when(personRepository.findAll()).thenReturn(people);

        List<Person> result = personService.getPeople();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(people, result);
    }
}
