package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.City;
import com.mcommings.campaigner.models.Country;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.ICityRepository;
import com.mcommings.campaigner.models.repositories.IContinentRepository;
import com.mcommings.campaigner.models.repositories.ICountryRepository;
import com.mcommings.campaigner.models.repositories.IGovernmentRepository;
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

import static com.mcommings.campaigner.enums.ForeignKey.FK_COUNTRY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CountryTest {
    
    @Mock
    private ICountryRepository countryRepository;
    @Mock
    private IContinentRepository continentRepository;
    @Mock
    private IGovernmentRepository governmentRepository;

    @Mock
    private ICityRepository cityRepository;

    @InjectMocks
    private CountryService countryService;

    @Test
    public void whenThereAreCountries_getCountries_ReturnsCountries() {
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1, "Country 1", "Description 1"));
        countries.add(new Country(2, "Country 2", "Description 2"));
        countries.add(new Country(3, "Country 3", "Description 3", 1, 3));
        when(countryRepository.findAll()).thenReturn(countries);

        List<Country> result = countryService.getCountries();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(countries, result);
    }

    @Test
    public void whenThereAreNoCountries_getCountries_ReturnsNothing() {
        List<Country> countries = new ArrayList<>();
        when(countryRepository.findAll()).thenReturn(countries);

        List<Country> result = countryService.getCountries();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(countries, result);
    }

    @Test
    public void whenCountryWithNoForeignKeysIsValid_saveCountry_SavesTheCountry() {
        Country country = new Country(1, "Country 1", "Description 1");
        when(countryRepository.saveAndFlush(country)).thenReturn(country);

        assertDoesNotThrow(() -> countryService.saveCountry(country));

        verify(countryRepository, times(1)).saveAndFlush(country);
    }
    @Test
    public void whenCountryWithForeignKeysIsValid_saveCountry_SavesTheCountry() {
        Country country = new Country(1, "Country 1", "Description 1", 1, 3);

        when(continentRepository.existsById(1)).thenReturn(true);
        when(governmentRepository.existsById(3)).thenReturn(true);
        when(countryRepository.saveAndFlush(country)).thenReturn(country);

        assertDoesNotThrow(() -> countryService.saveCountry(country));

        verify(countryRepository, times(1)).saveAndFlush(country);
    }

    @Test
    public void whenCountryNameIsInvalid_saveCountry_ThrowsIllegalArgumentException() {
        Country countryWithEmptyName = new Country(1, "", "Description 1");
        Country countryWithNullName = new Country(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> countryService.saveCountry(countryWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> countryService.saveCountry(countryWithNullName));
    }

    @Test
    public void whenCountryNameAlreadyExists_saveCountry_ThrowsDataIntegrityViolationException() {
        Country country = new Country(1, "Country 1", "Description 1", 1, 2);
        Country countryWithDuplicatedName = new Country(2, "Country 1", "Description 2", 3, 4);

        when(continentRepository.existsById(1)).thenReturn(true);
        when(continentRepository.existsById(3)).thenReturn(true);
        when(governmentRepository.existsById(2)).thenReturn(true);
        when(governmentRepository.existsById(4)).thenReturn(true);

        when(countryRepository.saveAndFlush(country)).thenReturn(country);
        when(countryRepository.saveAndFlush(countryWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> countryService.saveCountry(country));
        assertThrows(DataIntegrityViolationException.class, () -> countryService.saveCountry(countryWithDuplicatedName));
    }

    @Test
    public void whenCountryHasInvalidForeignKeys_saveCountry_ThrowsDataIntegrityViolationException() {
        Country country = new Country(1, "Country 1", "Description 1", 1, 2);

        when(continentRepository.existsById(1)).thenReturn(true);
        when(continentRepository.existsById(3)).thenReturn(false);
        when(countryRepository.saveAndFlush(country)).thenReturn(country);

        assertThrows(DataIntegrityViolationException.class, () -> countryService.saveCountry(country));

    }

    @Test
    public void whenCountryIdExists_deleteCountry_DeletesTheCountry() {
        int countryId = 1;
        when(countryRepository.existsById(countryId)).thenReturn(true);
        assertDoesNotThrow(() -> countryService.deleteCountry(countryId));
        verify(countryRepository, times(1)).deleteById(countryId);
    }

    @Test
    public void whenCountryIdDoesNotExist_deleteCountry_ThrowsIllegalArgumentException() {
        int countryId = 9000;
        when(countryRepository.existsById(countryId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> countryService.deleteCountry(countryId));
    }

    @Test
    public void whenCountryIdIsAForeignKey_deleteCountry_ThrowsDataIntegrityViolationException() {
        int countryId = 1;
        City city = new City(1, "Country", "Description", 1, countryId, 1, 1);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(cityRepository));
        List<City> cities = new ArrayList<>(Arrays.asList(city));

        when(countryRepository.existsById(countryId)).thenReturn(true);
        when(cityRepository.findByfk_country(countryId)).thenReturn(cities);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_COUNTRY.columnName, countryId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> countryService.deleteCountry(countryId));
    }

    @Test
    public void whenCountryIdWithNoFKIsFound_updateCountry_UpdatesTheCountry() {
        int countryId = 1;

        Country country = new Country(countryId, "Old Country Name", "Old Description");
        Country countryToUpdate = new Country(countryId, "Updated Country Name", "Updated Description");

        when(countryRepository.existsById(countryId)).thenReturn(true);
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));

        countryService.updateCountry(countryId, countryToUpdate);

        verify(countryRepository).findById(countryId);

        Country result = countryRepository.findById(countryId).get();
        Assertions.assertEquals(countryToUpdate.getName(), result.getName());
        Assertions.assertEquals(countryToUpdate.getDescription(), result.getDescription());
    }
    @Test
    public void whenCountryIdWithValidFKIsFound_updateCountry_UpdatesTheCountry() {
        int countryId = 2;

        Country country = new Country(countryId, "Test Country Name", "Test Description");
        Country countryToUpdate = new Country(countryId, "Updated Country Name", "Updated Description", 1, 2);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(continentRepository, governmentRepository));

        when(countryRepository.existsById(countryId)).thenReturn(true);
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));
        when(continentRepository.existsById(1)).thenReturn(true);
        when(governmentRepository.existsById(2)).thenReturn(true);

        countryService.updateCountry(countryId, countryToUpdate);

        verify(countryRepository).findById(countryId);

        Country result = countryRepository.findById(countryId).get();
        Assertions.assertEquals(countryToUpdate.getName(), result.getName());
        Assertions.assertEquals(countryToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(countryToUpdate.getFk_continent(), result.getFk_continent());
        Assertions.assertEquals(countryToUpdate.getFk_government(), result.getFk_government());
    }
    @Test
    public void whenCountryIdWithInvalidFKIsFound_updateCountry_ThrowsDataIntegrityViolationException() {
        int countryId = 1;

        Country country = new Country(countryId, "Test Country Name", "Test Description");
        Country countryToUpdate = new Country(countryId, "Updated Country Name", "Updated Description", 1, 2);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(continentRepository, governmentRepository));

        when(countryRepository.existsById(countryId)).thenReturn(true);
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));
        when(continentRepository.existsById(1)).thenReturn(true);
        when(governmentRepository.existsById(2)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> countryService.updateCountry(countryId, countryToUpdate));
    }

    @Test
    public void whenCountryIdIsNotFound_updateCountry_ThrowsIllegalArgumentException() {
        int countryId = 1;
        Country country = new Country(countryId, "Old Country Name", "Old Description");

        when(countryRepository.existsById(countryId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> countryService.updateCountry(countryId, country));
    }
}
