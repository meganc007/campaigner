package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.City;

import java.util.List;
import java.util.UUID;

public interface ICity {

    List<City> getCities();

    City getCity(int cityId);

    List<City> getCitiesByCampaignUUID(UUID uuid);

    List<City> getCitiesByCountryId(int countryId);

    List<City> getCitiesByRegionId(int regionId);

    void saveCity(City city);

    void deleteCity(int cityId);

    void updateCity(int cityId, City city);
}
