package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.entities.Event;
import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.entities.locations.City;
import com.mcommings.campaigner.entities.locations.Place;
import com.mcommings.campaigner.repositories.IEventRepository;
import com.mcommings.campaigner.repositories.IGovernmentRepository;
import com.mcommings.campaigner.repositories.IWealthRepository;
import com.mcommings.campaigner.repositories.locations.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_CITY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@SpringBootTest
public class CityTest {
    
    @Mock
    private ICityRepository cityRepository;
    @Mock
    private IWealthRepository wealthRepository;
    @Mock
    private ICountryRepository countryRepository;
    @Mock
    private ISettlementTypeRepository settlementTypeRepository;
    @Mock
    private IPlaceRepository placeRepository;
    @Mock
    private IGovernmentRepository governmentRepository;
    @Mock
    private IRegionRepository regionRepository;
    @Mock
    private IEventRepository eventRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    public void whenThereAreCities_getCities_ReturnsCities() {
        List<City> cities = new ArrayList<>();
        cities.add(new City(1, "City 1", "Description 1", UUID.randomUUID()));
        cities.add(new City(2, "City 2", "Description 2", UUID.randomUUID()));
        cities.add(new City(3, "City 3", "Description 3", UUID.randomUUID(), 1, 2, 3, 4, 5));
        when(cityRepository.findAll()).thenReturn(cities);

        List<City> result = cityService.getCities();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(cities, result);
    }

    @Test
    public void whenThereAreNoCities_getCities_ReturnsNothing() {
        List<City> cities = new ArrayList<>();
        when(cityRepository.findAll()).thenReturn(cities);

        List<City> result = cityService.getCities();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(cities, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getCitiesByCampaignUUID_ReturnsCities() {
        UUID campaign = UUID.randomUUID();
        List<City> cities = new ArrayList<>();
        cities.add(new City(1, "City 1", "Description 1", campaign));
        cities.add(new City(2, "City 2", "Description 2", campaign));
        cities.add(new City(3, "City 3", "Description 3", campaign, 1, 2, 3, 4, 5));

        when(cityRepository.findByfk_campaign_uuid(campaign)).thenReturn(cities);

        List<City> results = cityService.getCitiesByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(cities, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getCitiesByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<City> cities = new ArrayList<>();
        when(cityRepository.findByfk_campaign_uuid(campaign)).thenReturn(cities);

        List<City> result = cityService.getCitiesByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(cities, result);
    }

    @Test
    public void whenCityWithNoForeignKeysIsValid_saveCity_SavesTheCity() {
        City city = new City(1, "City 1", "Description 1", UUID.randomUUID());
        when(cityRepository.saveAndFlush(city)).thenReturn(city);

        assertDoesNotThrow(() -> cityService.saveCity(city));

        verify(cityRepository, times(1)).saveAndFlush(city);
    }

    @Test
    public void whenCityWithForeignKeysIsValid_saveCity_SavesTheCity() {
        City city = new City(1, "City 1", "Description 1", UUID.randomUUID(), 1, 2, 3, 4, 5);

        when(wealthRepository.existsById(1)).thenReturn(true);
        when(countryRepository.existsById(2)).thenReturn(true);
        when(settlementTypeRepository.existsById(3)).thenReturn(true);
        when(governmentRepository.existsById(4)).thenReturn(true);
        when(regionRepository.existsById(5)).thenReturn(true);
        when(cityRepository.saveAndFlush(city)).thenReturn(city);

        assertDoesNotThrow(() -> cityService.saveCity(city));

        verify(cityRepository, times(1)).saveAndFlush(city);
    }

    @Test
    public void whenCityNameIsInvalid_saveCity_ThrowsIllegalArgumentException() {
        City cityWithEmptyName = new City(1, "", "Description 1", UUID.randomUUID());
        City cityWithNullName = new City(2, null, "Description 2", UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> cityService.saveCity(cityWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> cityService.saveCity(cityWithNullName));
    }

    @Test
    public void whenCityNameAlreadyExists_saveCity_ThrowsDataIntegrityViolationException() {
        City city = new City(1, "City 1", "Description 1", UUID.randomUUID(), 1, 2, 3, 4, 5);
        City cityWithDuplicatedName = new City(2, "City 1", "Description 2", UUID.randomUUID(), 5, 6, 7, 8, 9);

        when(cityRepository.existsById(1)).thenReturn(true);
        when(wealthRepository.existsById(1)).thenReturn(true);
        when(countryRepository.existsById(2)).thenReturn(true);
        when(settlementTypeRepository.existsById(3)).thenReturn(true);
        when(governmentRepository.existsById(4)).thenReturn(true);
        when(regionRepository.existsById(5)).thenReturn(true);

        when(cityRepository.existsById(2)).thenReturn(true);
        when(wealthRepository.existsById(5)).thenReturn(true);
        when(countryRepository.existsById(6)).thenReturn(true);
        when(settlementTypeRepository.existsById(7)).thenReturn(true);
        when(governmentRepository.existsById(8)).thenReturn(true);
        when(regionRepository.existsById(9)).thenReturn(true);

        when(cityRepository.saveAndFlush(city)).thenReturn(city);
        when(cityRepository.saveAndFlush(cityWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> cityService.saveCity(city));
        assertThrows(DataIntegrityViolationException.class, () -> cityService.saveCity(cityWithDuplicatedName));
    }

    @Test
    public void whenCityHasInvalidForeignKeys_saveCity_ThrowsDataIntegrityViolationException() {
        City city = new City(1, "City 1", "Description 1", UUID.randomUUID(), 1, 2, 3, 4, 5);

        when(wealthRepository.existsById(1)).thenReturn(true);
        when(countryRepository.existsById(3)).thenReturn(false);
        when(settlementTypeRepository.existsById(3)).thenReturn(false);
        when(governmentRepository.existsById(3)).thenReturn(true);
        when(regionRepository.existsById(3)).thenReturn(false);
        when(cityRepository.saveAndFlush(city)).thenReturn(city);

        assertThrows(DataIntegrityViolationException.class, () -> cityService.saveCity(city));

    }

    @Test
    public void whenCityIdExists_deleteCity_DeletesTheCity() {
        int cityId = 1;
        when(cityRepository.existsById(cityId)).thenReturn(true);
        assertDoesNotThrow(() -> cityService.deleteCity(cityId));
        verify(cityRepository, times(1)).deleteById(cityId);
    }

    @Test
    public void whenCityIdDoesNotExist_deleteCity_ThrowsIllegalArgumentException() {
        int cityId = 9000;
        when(cityRepository.existsById(cityId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> cityService.deleteCity(cityId));
    }

    @Test
    public void whenCityIdIsAForeignKey_deleteCity_ThrowsDataIntegrityViolationException() {
        int cityId = 1;
        Place place = new Place(1, "Place", "Description", UUID.randomUUID(), 1, cityId, 1, 1, 1);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(placeRepository, eventRepository));
        List<Place> places = new ArrayList<>(Arrays.asList(place));

        Event event = new Event(1, "Event", "Description", 1, 1, 1, 1, cityId, 1, 1, UUID.randomUUID());
        List<Event> events = new ArrayList<>(Arrays.asList(event));

        when(cityRepository.existsById(cityId)).thenReturn(true);
        when(placeRepository.findByfk_city(cityId)).thenReturn(places);
        when(eventRepository.findByfk_city(cityId)).thenReturn(events);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_CITY.columnName, cityId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> cityService.deleteCity(cityId));
    }

    @Test
    public void whenCityIdWithNoFKIsFound_updateCity_UpdatesTheCity() {
        int cityId1 = 1;
        UUID campaign = UUID.randomUUID();

        City city = new City(cityId1, "Old City Name", "Old Description", campaign);
        City cityToUpdateNoFK = new City(cityId1, "Updated City Name", "Updated Description", campaign);

        when(cityRepository.existsById(cityId1)).thenReturn(true);
        when(cityRepository.findById(cityId1)).thenReturn(Optional.of(city));

        cityService.updateCity(cityId1, cityToUpdateNoFK);

        verify(cityRepository).findById(cityId1);

        City result1 = cityRepository.findById(cityId1).get();
        Assertions.assertEquals(cityToUpdateNoFK.getName(), result1.getName());
        Assertions.assertEquals(cityToUpdateNoFK.getDescription(), result1.getDescription());
    }
    @Test
    public void whenCityIdWithValidFKIsFound_updateCity_UpdatesTheCity() {
        int cityId = 2;
        UUID campaign = UUID.randomUUID();

        City city = new City(cityId, "Test City Name", "Test Description", campaign);
        City cityToUpdate = new City(cityId, "Updated City Name", "Updated Description", campaign, 1, 2, 3, 4, 5);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(wealthRepository, countryRepository,
                settlementTypeRepository, governmentRepository, regionRepository));

        when(cityRepository.existsById(cityId)).thenReturn(true);
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));
        when(wealthRepository.existsById(1)).thenReturn(true);
        when(countryRepository.existsById(2)).thenReturn(true);
        when(settlementTypeRepository.existsById(3)).thenReturn(true);
        when(governmentRepository.existsById(4)).thenReturn(true);
        when(regionRepository.existsById(5)).thenReturn(true);

        cityService.updateCity(cityId, cityToUpdate);

        verify(cityRepository).findById(cityId);

        City result = cityRepository.findById(cityId).get();
        Assertions.assertEquals(cityToUpdate.getName(), result.getName());
        Assertions.assertEquals(cityToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(cityToUpdate.getFk_wealth(), result.getFk_wealth());
        Assertions.assertEquals(cityToUpdate.getFk_country(), result.getFk_country());
        Assertions.assertEquals(cityToUpdate.getFk_settlement(), result.getFk_settlement());
        Assertions.assertEquals(cityToUpdate.getFk_government(), result.getFk_government());
        Assertions.assertEquals(cityToUpdate.getFk_region(), result.getFk_region());
    }

    @Test
    public void whenCityIdWithInvalidFKIsFound_updateCity_ThrowsDataIntegrityViolationException() {
        int cityId = 2;
        UUID campaign = UUID.randomUUID();

        City city = new City(cityId, "Test City Name", "Test Description", campaign);
        City cityToUpdate = new City(cityId, "Updated City Name", "Updated Description", campaign, 1, 2, 3, 4, 5);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(wealthRepository, countryRepository, settlementTypeRepository, governmentRepository));

        when(cityRepository.existsById(cityId)).thenReturn(true);
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));
        when(wealthRepository.existsById(1)).thenReturn(true);
        when(countryRepository.existsById(2)).thenReturn(false);
        when(settlementTypeRepository.existsById(3)).thenReturn(true);
        when(governmentRepository.existsById(4)).thenReturn(false);
        when(governmentRepository.existsById(5)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> cityService.updateCity(cityId, cityToUpdate));
    }

    @Test
    public void whenCityIdIsNotFound_updateCity_ThrowsIllegalArgumentException() {
        int cityId = 1;
        City city = new City(cityId, "Old City Name", "Old Description", UUID.randomUUID());

        when(cityRepository.existsById(cityId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> cityService.updateCity(cityId, city));
    }
}
