package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.models.Event;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.locations.City;
import com.mcommings.campaigner.models.locations.Country;
import com.mcommings.campaigner.models.locations.Place;
import com.mcommings.campaigner.models.locations.Region;
import com.mcommings.campaigner.repositories.IEventRepository;
import com.mcommings.campaigner.repositories.IGovernmentRepository;
import com.mcommings.campaigner.repositories.locations.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

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
    private IRegionRepository regionRepository;
    @Mock
    private IEventRepository eventRepository;
    @Mock
    private ICityRepository cityRepository;
    @Mock
    private IPlaceRepository placeRepository;

    @InjectMocks
    private CountryService countryService;

    @Test
    public void whenThereAreCountries_getCountries_ReturnsCountries() {
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1, "Country 1", "Description 1", UUID.randomUUID()));
        countries.add(new Country(2, "Country 2", "Description 2", UUID.randomUUID()));
        countries.add(new Country(3, "Country 3", "Description 3", UUID.randomUUID(), 1, 3));
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
    public void whenThereIsACountry_getCountry_ReturnsCountry() {
        Country country = new Country(1, "Country 1", "Description 1", UUID.randomUUID());
        when(countryRepository.findById(1)).thenReturn(Optional.of(country));

        Country result = countryService.getCountry(1);

        Assertions.assertEquals(country, result);
    }

    @Test
    public void whenThereIsNotACountry_getCountry_ReturnsCountry() {
        when(countryRepository.findById(9000)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> countryService.getCountry(9000));
    }

    @Test
    public void whenCampaignUUIDIsValid_getCountriesByCampaignUUID_ReturnsCountries() {
        UUID campaign = UUID.randomUUID();
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1, "Country 1", "Description 1", campaign));
        countries.add(new Country(2, "Country 2", "Description 2", campaign));

        when(countryRepository.findByfk_campaign_uuid(campaign)).thenReturn(countries);

        List<Country> results = countryService.getCountriesByCampaignUUID(campaign);

        Assertions.assertEquals(2, results.size());
        Assertions.assertEquals(countries, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getCountriesByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<Country> countries = new ArrayList<>();
        when(countryRepository.findByfk_campaign_uuid(campaign)).thenReturn(countries);

        List<Country> result = countryService.getCountriesByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(countries, result);
    }

    @Test
    public void whenCountryWithNoForeignKeysIsValid_saveCountry_SavesTheCountry() {
        Country country = new Country(1, "Country 1", "Description 1", UUID.randomUUID());
        when(countryRepository.saveAndFlush(country)).thenReturn(country);

        assertDoesNotThrow(() -> countryService.saveCountry(country));

        verify(countryRepository, times(1)).saveAndFlush(country);
    }

    @Test
    public void whenCountryWithForeignKeysIsValid_saveCountry_SavesTheCountry() {
        Country country = new Country(1, "Country 1", "Description 1", UUID.randomUUID(), 1, 3);

        when(continentRepository.existsById(1)).thenReturn(true);
        when(governmentRepository.existsById(3)).thenReturn(true);
        when(countryRepository.saveAndFlush(country)).thenReturn(country);

        assertDoesNotThrow(() -> countryService.saveCountry(country));

        verify(countryRepository, times(1)).saveAndFlush(country);
    }

    @Test
    public void whenCountryNameIsInvalid_saveCountry_ThrowsIllegalArgumentException() {
        Country countryWithEmptyName = new Country(1, "", "Description 1", UUID.randomUUID());
        Country countryWithNullName = new Country(2, null, "Description 2", UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> countryService.saveCountry(countryWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> countryService.saveCountry(countryWithNullName));
    }

    @Test
    public void whenCountryNameAlreadyExists_saveCountry_ThrowsDataIntegrityViolationException() {
        UUID campaign_uuid = UUID.randomUUID();
        Country country = new Country(1, "Country 1", "Description 1", campaign_uuid, 1, 2);
        Country countryWithDuplicatedName = new Country(2, "Country 1", "Description 2", campaign_uuid, 3, 4);

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
        Country country = new Country(1, "Country 1", "Description 1", UUID.randomUUID(), 1, 2);

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
        City city = new City(1, "City", "Description", UUID.randomUUID(), 1, countryId, 1, 1, 1);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(cityRepository, regionRepository, eventRepository, placeRepository));
        List<City> cities = new ArrayList<>(Arrays.asList(city));

        Region region = new Region(1, "Region", "Description", UUID.randomUUID(), countryId, 1);
        List<Region> regions = new ArrayList<>(Arrays.asList(region));

        Event event = new Event(1, "Name", "Description", 1, 1, 1, 1,
                1, 1, countryId);
        List<Event> events = new ArrayList<>(Arrays.asList(event));

        Place place = new Place(1, "Place", "Description", UUID.randomUUID(), 1, 1, countryId, 1, 1);
        List<Place> places = new ArrayList<>(Arrays.asList(place));

        when(countryRepository.existsById(countryId)).thenReturn(true);
        when(regionRepository.existsById(countryId)).thenReturn(true);
        when(cityRepository.findByfk_country(countryId)).thenReturn(cities);
        when(regionRepository.findByfk_country(countryId)).thenReturn(regions);
        when(eventRepository.findByfk_country(countryId)).thenReturn(events);
        when(placeRepository.findByfk_country(countryId)).thenReturn(places);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_COUNTRY.columnName, countryId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> countryService.deleteCountry(countryId));
    }

    @Test
    public void whenCountryIdWithNoFKIsFound_updateCountry_UpdatesTheCountry() {
        int countryId = 1;

        Country country = new Country(countryId, "Old Country Name", "Old Description", UUID.randomUUID());
        Country countryToUpdate = new Country(countryId, "Updated Country Name", "Updated Description", UUID.randomUUID());

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
        UUID campaign_uuid = UUID.randomUUID();

        Country country = new Country(countryId, "Test Country Name", "Test Description", campaign_uuid);
        Country countryToUpdate = new Country(countryId, "Updated Country Name", "Updated Description", campaign_uuid, 1, 2);

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

        Country country = new Country(countryId, "Test Country Name", "Test Description", UUID.randomUUID());
        Country countryToUpdate = new Country(countryId, "Updated Country Name", "Updated Description", UUID.randomUUID(), 1, 2);

        when(countryRepository.existsById(countryId)).thenReturn(true);
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));
        when(continentRepository.existsById(1)).thenReturn(true);
        when(governmentRepository.existsById(2)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> countryService.updateCountry(countryId, countryToUpdate));
    }

    @Test
    public void whenCountryIdIsNotFound_updateCountry_ThrowsIllegalArgumentException() {
        int countryId = 1;
        Country country = new Country(countryId, "Old Country Name", "Old Description", UUID.randomUUID());

        when(countryRepository.existsById(countryId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> countryService.updateCountry(countryId, country));
    }

    @Test
    public void whenSomeCountryFieldsChanged_updateCountry_OnlyUpdatesChangedFields() {
        int countryId = 1;
        Country country = new Country(countryId, "Country 1", "Description 1", UUID.randomUUID(), 1, 2);

        String newDescription = "New Country description";
        int newContinent = 3;

        Country countryToUpdate = new Country();
        countryToUpdate.setDescription(newDescription);
        countryToUpdate.setFk_continent(newContinent);

        when(countryRepository.existsById(countryId)).thenReturn(true);
        when(continentRepository.existsById(newContinent)).thenReturn(true);
        when(governmentRepository.existsById(2)).thenReturn(true);
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));

        countryService.updateCountry(countryId, countryToUpdate);

        verify(countryRepository).findById(countryId);

        Country result = countryRepository.findById(countryId).get();
        Assertions.assertEquals(country.getName(), result.getName());
        Assertions.assertEquals(newDescription, result.getDescription());
        Assertions.assertEquals(newContinent, result.getFk_continent());
        Assertions.assertEquals(country.getFk_government(), result.getFk_government());
    }
}
