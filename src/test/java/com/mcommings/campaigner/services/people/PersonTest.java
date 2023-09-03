package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.people.JobAssignment;
import com.mcommings.campaigner.models.people.Person;
import com.mcommings.campaigner.repositories.IWealthRepository;
import com.mcommings.campaigner.repositories.people.IAbilityScoreRepository;
import com.mcommings.campaigner.repositories.people.IJobAssignmentRepository;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import com.mcommings.campaigner.repositories.people.IRaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mcommings.campaigner.enums.ForeignKey.FK_PERSON;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    @Mock
    private IJobAssignmentRepository jobAssignmentRepository;

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

    @Test
    public void whenPersonWithNoForeignKeysIsValid_savePerson_SavesThePerson() {
        Person person = new Person(1, "First Name", "Last Name", 1, "Title", true, true, "Personality", "Description", "Notes");
        when(personRepository.saveAndFlush(person)).thenReturn(person);

        assertDoesNotThrow(() -> personService.savePerson(person));

        verify(personRepository, times(1)).saveAndFlush(person);
    }

    @Test
    public void whenPersonWithForeignKeysIsValid_savePerson_SavesThePerson() {
        Person person = new Person(1, "First Name", "Last Name", 1, "Title",
                4, 2, 3, true, true, "Personality", "Description", "Notes");

        when(raceRepository.existsById(4)).thenReturn(true);
        when(wealthRepository.existsById(2)).thenReturn(true);
        when(abilityScoreRepository.existsById(3)).thenReturn(true);
        when(personRepository.saveAndFlush(person)).thenReturn(person);

        assertDoesNotThrow(() -> personService.savePerson(person));

        verify(personRepository, times(1)).saveAndFlush(person);
    }

    @Test
    public void whenPersonNamesInvalid_savePerson_ThrowsIllegalArgumentException() {
        Person personWithEmptyFirstName = new Person(1, "", "Last Name", 1, "Title",
                4, 2, 3, true, true, "Personality", "Description", "Notes");
        Person personWithEmptyLastName = new Person(1, "First Name", "", 1, "Title",
                4, 2, 3, true, true, "Personality", "Description", "Notes");
        Person personWithNullFirstName = new Person(1, null, "Last Name", 1, "Title",
                4, 2, 3, true, true, "Personality", "Description", "Notes");
        Person personWithNullLastName = new Person(1, "First Name", null, 1, "Title",
                4, 2, 3, true, true, "Personality", "Description", "Notes");

        assertThrows(IllegalArgumentException.class, () -> personService.savePerson(personWithEmptyFirstName));
        assertThrows(IllegalArgumentException.class, () -> personService.savePerson(personWithEmptyLastName));
        assertThrows(IllegalArgumentException.class, () -> personService.savePerson(personWithNullFirstName));
        assertThrows(IllegalArgumentException.class, () -> personService.savePerson(personWithNullLastName));
    }

    @Test
    public void whenPersonAlreadyExists_savePerson_ThrowsDataIntegrityViolationException() {
        Person person = new Person(1, "First Name", "Last Name", 1, "Title",
                4, 2, 3, true, true, "Personality", "Description", "Notes");
        Person doppelganger = new Person(2, "First Name", "Last Name", 1, "Title",
                4, 2, 3, true, true, "Personality", "Description", "Notes");

        when(personRepository.existsById(1)).thenReturn(true);
        when(raceRepository.existsById(4)).thenReturn(true);
        when(wealthRepository.existsById(2)).thenReturn(true);
        when(abilityScoreRepository.existsById(3)).thenReturn(true);

        when(personRepository.saveAndFlush(person)).thenReturn(person);
        when(personRepository.saveAndFlush(doppelganger)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> personService.savePerson(person));
        assertThrows(DataIntegrityViolationException.class, () -> personService.savePerson(doppelganger));
    }

    @Test
    public void whenPersonIdExists_deletePerson_DeletesThePerson() {
        int personId = 1;
        when(personRepository.existsById(personId)).thenReturn(true);
        assertDoesNotThrow(() -> personService.deletePerson(personId));
        verify(personRepository, times(1)).deleteById(personId);
    }

    @Test
    public void whenPersonIdDoesNotExist_deletePerson_ThrowsIllegalArgumentException() {
        int personId = 9000;
        when(personRepository.existsById(personId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> personService.deletePerson(personId));
    }

    @Test
    public void whenPersonIdIsAForeignKey_deletePerson_ThrowsDataIntegrityViolationException() {
        int personId = 1;
        Person person = new Person(personId, "First Name", "Last Name", 1, "Title",
                true, false, "Personality", "Description", "Notes");
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(jobAssignmentRepository));
        List<Person> people = new ArrayList<>(Arrays.asList(person));

        JobAssignment jobAssignment = new JobAssignment(1, personId, 1);
        List<JobAssignment> jobAssignments = new ArrayList<>(Arrays.asList(jobAssignment));

        when(personRepository.existsById(personId)).thenReturn(true);
        when(jobAssignmentRepository.findByfk_person(personId)).thenReturn(jobAssignments);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_PERSON.columnName, personId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> personService.deletePerson(personId));
    }

    @Test
    public void whenPersonIdWithNoFKIsFound_updatePerson_UpdatesThePerson() {
        int personId = 1;

        Person person = new Person(personId, "First Name", "Last Name", 1, "Title",
                true, false, "Personality", "Description", "Notes");
        Person personToUpdate = new Person(personId, "Jane", "Doe", 33, "The Nameless",
                true, true, "Personality", "Description", "Notes");

        when(personRepository.existsById(personId)).thenReturn(true);
        when(personRepository.findById(personId)).thenReturn(Optional.of(person));

        personService.updatePerson(personId, personToUpdate);

        verify(personRepository).findById(personId);

        Person result = personRepository.findById(personId).get();
        Assertions.assertEquals(personToUpdate.getFirstName(), result.getFirstName());
        Assertions.assertEquals(personToUpdate.getLastName(), result.getLastName());
        Assertions.assertEquals(personToUpdate.getAge(), result.getAge());
        Assertions.assertEquals(personToUpdate.getTitle(), result.getTitle());
        Assertions.assertEquals(personToUpdate.getIsNPC(), result.getIsNPC());
        Assertions.assertEquals(personToUpdate.getIsEnemy(), result.getIsEnemy());
        Assertions.assertEquals(personToUpdate.getPersonality(), result.getPersonality());
        Assertions.assertEquals(personToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(personToUpdate.getNotes(), result.getNotes());
    }

    @Test
    public void whenPersonIdWithValidFKIsFound_updatePerson_UpdatesThePerson() {
        int personId = 1;

        Person person = new Person(personId, "First Name", "Last Name", 1, "Title",
                1, 2, 3, true, false, "Personality", "Description", "Notes");
        Person personToUpdate = new Person(personId, "Jane", "Doe", 33, "The Nameless",
                4, 5, 6, true, true, "Personality", "Description", "Notes");

        when(personRepository.existsById(personId)).thenReturn(true);
        when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        when(raceRepository.existsById(1)).thenReturn(true);
        when(wealthRepository.existsById(2)).thenReturn(true);
        when(abilityScoreRepository.existsById(3)).thenReturn(true);

        when(raceRepository.existsById(4)).thenReturn(true);
        when(wealthRepository.existsById(5)).thenReturn(true);
        when(abilityScoreRepository.existsById(6)).thenReturn(true);

        personService.updatePerson(personId, personToUpdate);

        verify(personRepository).findById(personId);

        Person result = personRepository.findById(personId).get();
        Assertions.assertEquals(personToUpdate.getFirstName(), result.getFirstName());
        Assertions.assertEquals(personToUpdate.getLastName(), result.getLastName());
        Assertions.assertEquals(personToUpdate.getAge(), result.getAge());
        Assertions.assertEquals(personToUpdate.getTitle(), result.getTitle());
        Assertions.assertEquals(personToUpdate.getFk_race(), result.getFk_race());
        Assertions.assertEquals(personToUpdate.getFk_wealth(), result.getFk_wealth());
        Assertions.assertEquals(personToUpdate.getFk_ability_score(), result.getFk_ability_score());
        Assertions.assertEquals(personToUpdate.getIsNPC(), result.getIsNPC());
        Assertions.assertEquals(personToUpdate.getIsEnemy(), result.getIsEnemy());
        Assertions.assertEquals(personToUpdate.getPersonality(), result.getPersonality());
        Assertions.assertEquals(personToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(personToUpdate.getNotes(), result.getNotes());
    }

    @Test
    public void whenPersonIdWithInvalidFKIsFound_updatePerson_ThrowsDataIntegrityViolationException() {
        int personId = 1;

        Person person = new Person(personId, "First Name", "Last Name", 1, "Title",
                1, 2, 3, true, false, "Personality", "Description", "Notes");
        Person personToUpdate = new Person(personId, "Jane", "Doe", 33, "The Nameless",
                4, 5, 6, true, true, "Personality", "Description", "Notes");

        when(personRepository.existsById(personId)).thenReturn(true);
        when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        when(raceRepository.existsById(1)).thenReturn(true);
        when(wealthRepository.existsById(2)).thenReturn(false);
        when(abilityScoreRepository.existsById(3)).thenReturn(true);

        when(raceRepository.existsById(4)).thenReturn(false);
        when(wealthRepository.existsById(5)).thenReturn(true);
        when(abilityScoreRepository.existsById(6)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> personService.updatePerson(personId, personToUpdate));
    }

    @Test
    public void whenPersonIdIsNotFound_updatePerson_ThrowsIllegalArgumentException() {
        int personId = 1;
        Person person = new Person(personId, "First Name", "Last Name", 1, "Title",
                1, 2, 3, true, false, "Personality", "Description", "Notes");


        when(personRepository.existsById(personId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> personService.updatePerson(personId, person));
    }

}
