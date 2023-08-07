package com.mcommings.campaigner.models;

import com.mcommings.campaigner.models.repositories.IRaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RepositoryHelperTest {

    @Mock
    private IRaceRepository raceRepository;

    @Test
    public void whenAnIdIsNotFound_cannotFindId_ReturnsTrue() {
        int id = 1;
        Mockito.when(raceRepository.existsById(id)).thenReturn(false);
        boolean expected = true;
        boolean actual = RepositoryHelper.cannotFindId(raceRepository, id);

        Assertions.assertEquals(expected, actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void whenAnIdIsFound_cannotFindId_ReturnsFalse() {
        int id = 1;
        Mockito.when(raceRepository.existsById(id)).thenReturn(true);
        boolean expected = false;
        boolean actual = RepositoryHelper.cannotFindId(raceRepository, id);

        Assertions.assertEquals(expected, actual);
        Assertions.assertFalse(actual);
    }

    @Test
    public void whenIdIsValid_getById_FindsARecord() {
        int id = 1;
        String name = "Test Race";
        String description = "This is a test race.";
        boolean isExotic = true;

        Race testRace = new Race(id, name, description, isExotic);

        Mockito.when(raceRepository.findById(id)).thenReturn(Optional.of(testRace));
        Race foundRace = RepositoryHelper.getById(raceRepository, id);

        Assertions.assertEquals(testRace.getId(), foundRace.getId());
    }

    @Test
    public void whenIdIsInvalid_getById_DoesNotFindARecord() {
        int id = 1;
        String name = "Test Race";
        String description = "This is a test race.";
        boolean isExotic = true;

        int id2 = 2;

        Race testRace = new Race(id, name, description, isExotic);

        Mockito.when(raceRepository.findById(id)).thenReturn(Optional.of(testRace));
        Race foundRace = RepositoryHelper.getById(raceRepository, id);

        Assertions.assertNotEquals(id2, foundRace.getId());
    }

    @Test
    public void whenNameExists_nameAlreadyExists_ReturnsTrue() {
        int id = 1;
        String name = "Test Race";
        String description = "This is a test race.";
        boolean isExotic = true;

        Race testRace = new Race(id, name, description, isExotic);
        Mockito.when(raceRepository.findByName(name)).thenReturn(Optional.of(testRace));

        boolean expected = true;
        boolean actual = RepositoryHelper.nameAlreadyExists(raceRepository, testRace);

        Assertions.assertEquals(expected, actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void whenNameDoesNotExist_nameAlreadyExists_ReturnsFalse() {
        int id = 1;
        String name = "Test Race";
        String description = "This is a test race.";
        boolean isExotic = true;

        int id2 = 2;
        String name2 = "New Race";
        String description2 = "This is a test race.";
        boolean isExotic2 = true;

        Race testRace = new Race(id, name, description, isExotic);
        Race testRace2 = new Race(id2, name2, description2, isExotic2);
        Mockito.when(raceRepository.findByName(name)).thenReturn(Optional.of(testRace));

        boolean expected = false;
        boolean actual = RepositoryHelper.nameAlreadyExists(raceRepository, testRace2);

        Assertions.assertEquals(expected, actual);
        Assertions.assertFalse(actual);
    }

    @Test
    public void whenNameIsNull_nameIsNullOrEmpty_ReturnsTrue() {
        Race testRace = new Race();

        boolean expected = true;
        boolean actual = RepositoryHelper.nameIsNullOrEmpty(testRace);

        Assertions.assertEquals(expected, actual);
        Assertions.assertTrue(actual);
    }
    @Test
    public void whenNameIsEmpty_nameIsNullOrEmpty_ReturnsTrue() {
        int id = 1;
        String name = "";
        String description = "test";
        boolean isExotic = true;

        Race testRace = new Race(id, name, description, isExotic);

        boolean expected = true;
        boolean actual = RepositoryHelper.nameIsNullOrEmpty(testRace);

        Assertions.assertEquals(expected, actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void whenNameIsNotNullOrEmpty_nameIsNullOrEmpty_ReturnsFalse() {
        int id = 1;
        String name = "Test";
        String description = "test";
        boolean isExotic = true;

        Race testRace = new Race(id, name, description, isExotic);

        boolean expected = false;
        boolean actual = RepositoryHelper.nameIsNullOrEmpty(testRace);

        Assertions.assertEquals(expected, actual);
        Assertions.assertFalse(actual);
    }

}