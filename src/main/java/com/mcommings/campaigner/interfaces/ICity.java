package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.City;

import java.util.List;

public interface ICity {

    List<City> getCities();

    void saveCity(City city);

    void deleteCity(int cityId);

    void updateCity(int cityId, City city);
}
