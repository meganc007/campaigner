package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Continent;
import com.mcommings.campaigner.models.Country;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.IContinentRepository;
import com.mcommings.campaigner.models.repositories.ICountryRepository;
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

import static com.mcommings.campaigner.enums.ForeignKey.FK_CONTINENT;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ContinentTest {
    @Mock
    private IContinentRepository continentRepository;

    @Mock
    private ICountryRepository countryRepository;

    @InjectMocks
    private ContinentService continentService;

    @Test
    public void whenThereAreContinents_getContinents_ReturnsContinents() {
        List<Continent> continents = new ArrayList<>();
        continents.add(new Continent(1, "Continent 1", "Description 1"));
        continents.add(new Continent(2, "Continent 2", "Description 2"));
        when(continentRepository.findAll()).thenReturn(continents);

        List<Continent> result = continentService.getContinents();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(continents, result);
    }

    @Test
    public void whenThereAreNoContinents_getContinents_ReturnsNothing() {
        List<Continent> continents = new ArrayList<>();
        when(continentRepository.findAll()).thenReturn(continents);

        List<Continent> result = continentService.getContinents();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(continents, result);
    }

    @Test
    public void whenContinentIsValid_saveContinent_SavesTheContinent() {
        Continent continent = new Continent(1, "Continent 1", "Description 1");
        when(continentRepository.saveAndFlush(continent)).thenReturn(continent);

        assertDoesNotThrow(() -> continentService.saveContinent(continent));
        verify(continentRepository, times(1)).saveAndFlush(continent);
    }

    @Test
    public void whenContinentNameIsInvalid_saveContinent_ThrowsIllegalArgumentException() {
        Continent continentWithEmptyName = new Continent(1, "", "Description 1");
        Continent continentWithNullName = new Continent(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> continentService.saveContinent(continentWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> continentService.saveContinent(continentWithNullName));
    }

    @Test
    public void whenContinentNameAlreadyExists_saveContinent_ThrowsDataIntegrityViolationException() {
        Continent continent = new Continent(1, "Continent 1", "Description 1");
        Continent continentWithDuplicatedName = new Continent(2, "Continent 1", "Description 2");
        when(continentRepository.saveAndFlush(continent)).thenReturn(continent);
        when(continentRepository.saveAndFlush(continentWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> continentService.saveContinent(continent));
        assertThrows(DataIntegrityViolationException.class, () -> continentService.saveContinent(continentWithDuplicatedName));
    }

    @Test
    public void whenContinentIdExists_deleteContinent_DeletesTheContinent() {
        int continentId = 1;
        when(continentRepository.existsById(continentId)).thenReturn(true);
        assertDoesNotThrow(() -> continentService.deleteContinent(continentId));
        verify(continentRepository, times(1)).deleteById(continentId);
    }

    @Test
    public void whenContinentIdDoesNotExist_deleteContinent_ThrowsIllegalArgumentException() {
        int continentId = 9000;
        when(continentRepository.existsById(continentId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> continentService.deleteContinent(continentId));
    }

    @Test
    public void whenContinentIdIsAForeignKeyForAnotherTable_deleteContinent_ThrowsDataIntegrityViolationException() {
        int continentId = 1;
        Country country = new Country(1, "Country", "Description", continentId, 1);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(countryRepository));
        List<Country> countries = new ArrayList<>(Arrays.asList(country));

        when(continentRepository.existsById(continentId)).thenReturn(true);
        when(countryRepository.findByfk_continent(continentId)).thenReturn(countries);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_CONTINENT.columnName, continentId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> continentService.deleteContinent(continentId));
    }

    @Test
    public void whenContinentIdIsFound_updateContinent_UpdatesTheContinent() {
        int continentId = 1;
        Continent continent = new Continent(continentId, "Old Continent Name", "Old Description");
        Continent continentToUpdate = new Continent(continentId, "Updated Continent Name", "Updated Description");

        when(continentRepository.existsById(continentId)).thenReturn(true);
        when(continentRepository.findById(continentId)).thenReturn(Optional.of(continent));

        continentService.updateContinent(continentId, continentToUpdate);

        verify(continentRepository).findById(continentId);

        Continent result = continentRepository.findById(continentId).get();
        Assertions.assertEquals(continentToUpdate.getName(), result.getName());
        Assertions.assertEquals(continentToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenContinentIdIsNotFound_updateContinent_ThrowsIllegalArgumentException() {
        int continentId = 1;
        Continent continent = new Continent(continentId, "Old Continent Name", "Old Description");

        when(continentRepository.existsById(continentId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> continentService.updateContinent(continentId, continent));
    }
}
