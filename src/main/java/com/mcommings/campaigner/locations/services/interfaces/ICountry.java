package com.mcommings.campaigner.locations.services.interfaces;

import com.mcommings.campaigner.locations.dtos.CountryDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICountry {

    List<CountryDTO> getCountries();

    Optional<CountryDTO> getCountry(int countryId);

    List<CountryDTO> getCountriesByCampaignUUID(UUID uuid);

    List<CountryDTO> getCountriesByContinentId(int continentId);

    void saveCountry(CountryDTO country);

    void deleteCountry(int countryId);

    Optional<CountryDTO> updateCountry(int countryId, CountryDTO country);
}
