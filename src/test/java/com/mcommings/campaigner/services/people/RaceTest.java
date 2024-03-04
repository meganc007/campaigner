package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.people.Person;
import com.mcommings.campaigner.models.people.Race;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import com.mcommings.campaigner.repositories.people.IRaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_RACE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RaceTest {

    @Mock
    private IRaceRepository raceRepository;
    @Mock
    private IPersonRepository personRepository;

    @InjectMocks
    private RaceService raceService;

    @Test
    public void whenThereAreRaces_getRaces_ReturnsRaces() {
        List<Race> races = new ArrayList<>();
        races.add(new Race(1, "Race 1", "Description 1", false));
        races.add(new Race(2, "Race 2", "Description 2", false));
        when(raceRepository.findAll()).thenReturn(races);

        List<Race> result = raceService.getRaces();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(races, result);
    }

    @Test
    public void whenThereAreNoRaces_getRaces_ReturnsNothing() {
        List<Race> races = new ArrayList<>();
        when(raceRepository.findAll()).thenReturn(races);

        List<Race> result = raceService.getRaces();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(races, result);
    }

    @Test
    public void whenRaceIsValid_saveRace_SavesTheRace() {
        Race race = new Race(1, "Race 1", "Description 1", true);
        when(raceRepository.saveAndFlush(race)).thenReturn(race);

        assertDoesNotThrow(() -> raceService.saveRace(race));
        verify(raceRepository, times(1)).saveAndFlush(race);
    }

    @Test
    public void whenRaceNameIsInvalid_saveRace_ThrowsIllegalArgumentException() {
        Race raceWithEmptyName = new Race(1, "", "Description 1", true);
        Race raceWithNullName = new Race(2, null, "Description 2", true);

        assertThrows(IllegalArgumentException.class, () -> raceService.saveRace(raceWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> raceService.saveRace(raceWithNullName));
    }

    @Test
    public void whenRaceNameAlreadyExists_saveRace_ThrowsDataIntegrityViolationException() {
        Race race = new Race(1, "Race 1", "Description 1", true);
        Race raceWithDuplicatedName = new Race(2, "Race 1", "Description 2", true);
        when(raceRepository.saveAndFlush(race)).thenReturn(race);
        when(raceRepository.saveAndFlush(raceWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> raceService.saveRace(race));
        assertThrows(DataIntegrityViolationException.class, () -> raceService.saveRace(raceWithDuplicatedName));
    }

    @Test
    public void whenRaceIdExists_deleteRace_DeletesTheRace() {
        int raceId = 1;
        when(raceRepository.existsById(raceId)).thenReturn(true);
        assertDoesNotThrow(() -> raceService.deleteRace(raceId));
        verify(raceRepository, times(1)).deleteById(raceId);
    }

    @Test
    public void whenRaceIdDoesNotExist_deleteRace_ThrowsIllegalArgumentException() {
        int raceId = 9000;
        when(raceRepository.existsById(raceId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> raceService.deleteRace(raceId));
    }

    @Test
    public void whenRaceIdIsAForeignKey_deleteRace_ThrowsDataIntegrityViolationException() {
        int raceId = 1;
        Person person = new Person(1, "Jane", "Doe", 33, "The Nameless One",
                raceId, 2, 2, true, false, "Personality", "Description", "Notes", UUID.randomUUID());
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(personRepository));
        List<Person> people = new ArrayList<>(Arrays.asList(person));

        when(raceRepository.existsById(raceId)).thenReturn(true);
        when(personRepository.findByfk_race(raceId)).thenReturn(people);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_RACE.columnName, raceId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> raceService.deleteRace(raceId));
    }

    @Test
    public void whenRaceIdIsFound_updateRace_UpdatesTheRace() {
        int raceId = 1;
        Race race = new Race(raceId, "Old Race Name", "Old Description", false);
        Race raceToUpdate = new Race(raceId, "Updated Race Name", "Updated Description", true);

        when(raceRepository.existsById(raceId)).thenReturn(true);
        when(raceRepository.findById(raceId)).thenReturn(Optional.of(race));

        raceService.updateRace(raceId, raceToUpdate);

        verify(raceRepository).findById(raceId);

        Race result = raceRepository.findById(raceId).get();
        Assertions.assertEquals(raceToUpdate.getName(), result.getName());
        Assertions.assertEquals(raceToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(raceToUpdate.getIs_exotic(), result.getIs_exotic());
    }

    @Test
    public void whenRaceIdIsNotFound_updateRace_ThrowsIllegalArgumentException() {
        int raceId = 1;
        Race race = new Race(raceId, "Old Race Name", "Old Description", false);

        when(raceRepository.existsById(raceId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> raceService.updateRace(raceId, race));
    }

    @Test
    public void whenSomeRaceFieldsChanged_updateRace_OnlyUpdatesChangedFields() {
        int raceId = 1;
        Race race = new Race(raceId, "Name", "Description", false);

        String newDescription = "New Race description";

        Race raceToUpdate = new Race();
        raceToUpdate.setDescription(newDescription);

        when(raceRepository.existsById(raceId)).thenReturn(true);
        when(raceRepository.findById(raceId)).thenReturn(Optional.of(race));

        raceService.updateRace(raceId, raceToUpdate);

        verify(raceRepository).findById(raceId);

        Race result = raceRepository.findById(raceId).get();
        Assertions.assertEquals(race.getName(), result.getName());
        Assertions.assertEquals(newDescription, result.getDescription());
        Assertions.assertEquals(race.getIs_exotic(), result.getIs_exotic());
    }

}
