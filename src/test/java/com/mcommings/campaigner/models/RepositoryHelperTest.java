package com.mcommings.campaigner.models;

import com.mcommings.campaigner.models.repositories.IContinentRepository;
import com.mcommings.campaigner.models.repositories.ICountryRepository;
import com.mcommings.campaigner.models.repositories.IGovernmentRepository;
import com.mcommings.campaigner.models.repositories.IRaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class RepositoryHelperTest {

    @Mock
    private IRaceRepository raceRepository;
    @Mock
    private ICountryRepository countryRepository;
    @Mock
    private IContinentRepository continentRepository;
    @Mock
    private IGovernmentRepository governmentRepository;

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

    @Test
    public void whenThereIsAForeignKey_isForeignKey_ReturnsTrue() {
        Country country = new Country(1, "Country", "Description", 1, 1);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(continentRepository, governmentRepository));

        Mockito.when(countryRepository.findById(1)).thenReturn(Optional.of(country));
        Mockito.when(continentRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> RepositoryHelper.isForeignKey(countryRepository, repositories, 1));
        boolean actual = RepositoryHelper.isForeignKey(countryRepository, repositories, 1);
        Assertions.assertTrue(actual);
    }

    @Test
    public void whenThereIsNotAForeignKey_isForeignKey_ReturnsFalse() {
        Country country = new Country(1, "Country", "Description", 1, 1);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(continentRepository, governmentRepository));

        Mockito.when(countryRepository.findById(1)).thenReturn(Optional.of(country));
        Mockito.when(continentRepository.existsById(1)).thenReturn(false);
        Mockito.when(governmentRepository.existsById(1)).thenReturn(false);

        assertDoesNotThrow(() -> RepositoryHelper.isForeignKey(countryRepository, repositories, 1));
        boolean actual = RepositoryHelper.isForeignKey(countryRepository, repositories, 1);
        Assertions.assertFalse(actual);
    }

    @Test
    public void whenThereIsAnInvalidForeignKey_foreignKeyIsNotValid_ReturnsTrue() {
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(continentRepository, governmentRepository));
        Object requestItem = new Country(1, "Country", "Description", 1, 2);

        Mockito.when(continentRepository.existsById(1)).thenReturn(false);
        Mockito.when(governmentRepository.existsById(2)).thenReturn(false);
        Mockito.when(RepositoryHelper.foreignKeyIsNotValid(countryRepository, repositories, requestItem)).thenReturn(true);

        assertDoesNotThrow(() -> RepositoryHelper.foreignKeyIsNotValid(countryRepository, repositories, requestItem));
        boolean actual = RepositoryHelper.foreignKeyIsNotValid(countryRepository, repositories, requestItem);
        Assertions.assertTrue(actual);
    }

    @Test
    public void whenThereIsAValidForeignKey_foreignKeyIsNotValid_ReturnsFalse() {
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(continentRepository, governmentRepository));
        Object requestItem = new Country(1, "Country", "Description", 1, 2);
        Mockito.when(RepositoryHelper.foreignKeyIsNotValid(countryRepository, repositories, requestItem)).thenReturn(false);

        Mockito.when(continentRepository.existsById(1)).thenReturn(true);
        Mockito.when(governmentRepository.existsById(2)).thenReturn(true);

        assertDoesNotThrow(() -> RepositoryHelper.foreignKeyIsNotValid(countryRepository, repositories, requestItem));
        Assertions.assertFalse(RepositoryHelper.foreignKeyIsNotValid(countryRepository, repositories, requestItem));
    }

}
