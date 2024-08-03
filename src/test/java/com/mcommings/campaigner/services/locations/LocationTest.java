package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.models.locations.*;
import com.mcommings.campaigner.repositories.locations.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
public class LocationTest {
    @Mock
    private IContinentRepository continentRepository;
    @Mock
    private ICountryRepository countryRepository;
    @Mock
    private IRegionRepository regionRepository;
    @Mock
    private ICityRepository cityRepository;
    @Mock
    private IPlaceRepository placeRepository;
    @Mock
    private ILandmarkRepository landmarkRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    public void whenThereAreLocations_getLocation_ReturnsLocation() {
        UUID uuid = UUID.randomUUID();
        List<Continent> continents = new ArrayList<>();
        continents.add(new Continent(1, "Continent 1", "Description 1", uuid));

        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1, "Country 1", "Description 1", uuid));

        List<Region> regions = new ArrayList<>();
        regions.add(new Region(1, "Region 1", "Description 1", uuid));

        List<City> cities = new ArrayList<>();
        cities.add(new City(1, "City 1", "Description 1", uuid));

        List<Place> places = new ArrayList<>();
        places.add(new Place(1, "Place 1", "Description 1", uuid));

        List<Landmark> landmarks = new ArrayList<>();
        landmarks.add(new Landmark(1, "Landmark 1", "Description 1", uuid));

        when(continentRepository.findByfk_campaign_uuid(uuid)).thenReturn(continents);
        when(countryRepository.findByfk_campaign_uuid(uuid)).thenReturn(countries);
        when(regionRepository.findByfk_campaign_uuid(uuid)).thenReturn(regions);
        when(cityRepository.findByfk_campaign_uuid(uuid)).thenReturn(cities);
        when(placeRepository.findByfk_campaign_uuid(uuid)).thenReturn(places);
        when(landmarkRepository.findByfk_campaign_uuid(uuid)).thenReturn(landmarks);

        Location result = locationService.getLocation(uuid);
        Assertions.assertEquals(continents, result.getContinents());
        Assertions.assertEquals(countries, result.getCountries());
        Assertions.assertEquals(regions, result.getRegions());
        Assertions.assertEquals(cities, result.getCities());
        Assertions.assertEquals(places, result.getPlaces());
        Assertions.assertEquals(landmarks, result.getLandmarks());
    }

    @Test
    public void whenThereAreNoLocations_getLocation_ReturnsNothing() {
        UUID uuid = UUID.randomUUID();
        List<Continent> continents = new ArrayList<>();
        List<Country> countries = new ArrayList<>();
        List<Region> regions = new ArrayList<>();
        List<City> cities = new ArrayList<>();
        List<Place> places = new ArrayList<>();
        List<Landmark> landmarks = new ArrayList<>();

        when(continentRepository.findByfk_campaign_uuid(uuid)).thenReturn(continents);
        when(countryRepository.findByfk_campaign_uuid(uuid)).thenReturn(countries);
        when(regionRepository.findByfk_campaign_uuid(uuid)).thenReturn(regions);
        when(cityRepository.findByfk_campaign_uuid(uuid)).thenReturn(cities);
        when(placeRepository.findByfk_campaign_uuid(uuid)).thenReturn(places);
        when(landmarkRepository.findByfk_campaign_uuid(uuid)).thenReturn(landmarks);

        Location result = locationService.getLocation(uuid);

        Assertions.assertEquals(uuid, result.getCampaignUuid());
        Assertions.assertEquals(0, result.getContinents().size());
        Assertions.assertEquals(0, result.getCountries().size());
        Assertions.assertEquals(0, result.getRegions().size());
        Assertions.assertEquals(0, result.getCities().size());
        Assertions.assertEquals(0, result.getPlaces().size());
        Assertions.assertEquals(0, result.getLandmarks().size());
    }

}
