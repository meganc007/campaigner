package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Race;
import com.mcommings.campaigner.models.repositories.IRaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RaceTest {

    @Mock
    private IRaceRepository raceRepository;

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
    public void whenRaceIsValid_saveRace_savesTheRace() {
        Race race = new Race(1, "Race 1", "Description 1", true);
        when(raceRepository.saveAndFlush(race)).thenReturn(race);

        assertDoesNotThrow(() -> raceService.saveRace(race));
        verify(raceRepository, times(1)).saveAndFlush(race);
    }

    @Test
    public void whenRaceNameIsInvalid_saveRace_ThrowsIllegalArgumentException() {
        Race race = new Race(1, "", "Description 1", true);
        when(raceRepository.saveAndFlush(race)).thenReturn(race);

        assertThrows(IllegalArgumentException.class, () -> raceService.saveRace(race));
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
    public void whenRaceIdExists_deleteRace_deletesTheRace() {
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

    //TODO: after this functionality is added, test that deleteRace doesn't delete
    // when id is a foreign key in another table


}
