package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Country;

import java.util.List;

public interface ICountry {

    List<Country> getCountries();

    void saveCountry(Country country);

    void deleteCountry(int countryId);

    void updateCountry(int countryId, Country country);
}
