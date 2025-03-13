package com.mcommings.campaigner.locations.services.interfaces;

import com.mcommings.campaigner.locations.dtos.CityDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICity {

    List<CityDTO> getCities();

    Optional<CityDTO> getCity(int cityId);

    List<CityDTO> getCitiesByCampaignUUID(UUID uuid);

    List<CityDTO> getCitiesByCountryId(int countryId);

    List<CityDTO> getCitiesByRegionId(int regionId);

    void saveCity(CityDTO city);

    void deleteCity(int cityId);

    Optional<CityDTO> updateCity(int cityId, CityDTO city);
}
