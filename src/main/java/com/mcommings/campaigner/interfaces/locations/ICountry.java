package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.Country;

import java.util.List;

public interface ICountry {

    List<Country> getCountries();

    Country getCountry(int countryId);

    void saveCountry(Country country);

    void deleteCountry(int countryId);

    void updateCountry(int countryId, Country country);
}
