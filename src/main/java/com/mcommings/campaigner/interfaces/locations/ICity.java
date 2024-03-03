package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.City;

import java.util.List;
import java.util.UUID;

public interface ICity {

    List<City> getCities();

    City getCity(int cityId);

    List<City> getCitiesByCampaignUUID(UUID uuid);

    void saveCity(City city);

    void deleteCity(int cityId);

    void updateCity(int cityId, City city);
}
