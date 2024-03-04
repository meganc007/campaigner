package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.Country;

import java.util.List;
import java.util.UUID;

public interface ICountry {

    List<Country> getCountries();

    Country getCountry(int countryId);

    List<Country> getCountriesByCampaignUUID(UUID uuid);

    List<Country> getCountriesByContinentId(int continentId);

    void saveCountry(Country country);

    void deleteCountry(int countryId);

    void updateCountry(int countryId, Country country);
}
