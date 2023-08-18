package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Country;
import com.mcommings.campaigner.models.repositories.ICountryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CountryTest {
    
    @Mock
    private ICountryRepository countryRepository;

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
    public void whenCountryIsValid_saveCountry_SavesTheCountry() {
        Country country = new Country(1, "Country 1", "Description 1");
        Country countryWithFK = new Country(2, "Country 2", "Description 2", 1, 3);
        when(countryRepository.saveAndFlush(country)).thenReturn(country);
        when(countryRepository.saveAndFlush(countryWithFK)).thenReturn(countryWithFK);

        assertDoesNotThrow(() -> countryService.saveCountry(country));
        assertDoesNotThrow(() -> countryService.saveCountry(countryWithFK));

        verify(countryRepository, times(1)).saveAndFlush(country);
        verify(countryRepository, times(1)).saveAndFlush(countryWithFK);
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
        when(countryRepository.saveAndFlush(country)).thenReturn(country);
        when(countryRepository.saveAndFlush(countryWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> countryService.saveCountry(country));
        assertThrows(DataIntegrityViolationException.class, () -> countryService.saveCountry(countryWithDuplicatedName));
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

    //TODO: test that deleteCountry doesn't delete when it's a foreign key

    @Test
    public void whenCountryIdIsFound_updateCountry_UpdatesTheCountry() {
        int countryId1 = 1;
        int countryId2 = 2;

        Country country = new Country(countryId1, "Old Country Name", "Old Description");
        Country countryToUpdateNoFK = new Country(countryId1, "Updated Country Name", "Updated Description");

        Country country2 = new Country(countryId2, "Test Country Name", "Test Description");
        Country countryToUpdateWithFK = new Country(countryId2, "Updated Country Name", "Updated Description", 1, 2);

        when(countryRepository.existsById(countryId1)).thenReturn(true);
        when(countryRepository.findById(countryId1)).thenReturn(Optional.of(country));

        when(countryRepository.existsById(countryId2)).thenReturn(true);
        when(countryRepository.findById(countryId2)).thenReturn(Optional.of(country2));

        countryService.updateCountry(countryId1, countryToUpdateNoFK);
        countryService.updateCountry(countryId2, countryToUpdateWithFK);

        verify(countryRepository).findById(countryId1);
        verify(countryRepository).findById(countryId2);

        Country result1 = countryRepository.findById(countryId1).get();
        Assertions.assertEquals(countryToUpdateNoFK.getName(), result1.getName());
        Assertions.assertEquals(countryToUpdateNoFK.getDescription(), result1.getDescription());

        Country result2 = countryRepository.findById(countryId2).get();
        Assertions.assertEquals(countryToUpdateWithFK.getName(), result2.getName());
        Assertions.assertEquals(countryToUpdateWithFK.getDescription(), result2.getDescription());
    }

    @Test
    public void whenCountryIdIsNotFound_updateCountry_ThrowsIllegalArgumentException() {
        int countryId = 1;
        Country country = new Country(countryId, "Old Country Name", "Old Description");

        when(countryRepository.existsById(countryId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> countryService.updateCountry(countryId, country));
    }
}
