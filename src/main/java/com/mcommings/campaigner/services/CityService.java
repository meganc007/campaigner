package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.ICity;
import com.mcommings.campaigner.models.City;
import com.mcommings.campaigner.models.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CityService implements ICity {

    private final ICityRepository cityRepository;
    private final IWealthRepository wealthRepository;
    private final ICountryRepository countryRepository;
    private final ISettlementTypeRepository settlementTypeRepository;
    private final IGovernmentRepository governmentRepository;

    @Autowired
    public CityService(ICityRepository cityRepository, IWealthRepository wealthRepository,
                       ICountryRepository countryRepository, ISettlementTypeRepository settlementTypeRepository,
                       IGovernmentRepository governmentRepository) {
        this.cityRepository = cityRepository;
        this.wealthRepository = wealthRepository;
        this.countryRepository = countryRepository;
        this.settlementTypeRepository = settlementTypeRepository;
        this.governmentRepository = governmentRepository;
    }

    @Override
    public List<City> getCities() {
        return cityRepository.findAll();
    }

    @Override
    @Transactional
    public void saveCity(City city) throws IllegalArgumentException, DataIntegrityViolationException {

    }

    @Override
    @Transactional
    public void deleteCity(int cityId) throws IllegalArgumentException, DataIntegrityViolationException{

    }

    @Override
    @Transactional
    public void updateCity(int cityId, City city) throws IllegalArgumentException, DataIntegrityViolationException {

    }

    private boolean hasForeignKeys(City city) {
        return city.getFk_wealth() != null ||
                city.getFk_country() != null  ||
                city.getFk_settlement() != null ||
                city.getFk_government() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(wealthRepository, countryRepository,
                settlementTypeRepository, governmentRepository));
        return repositories;
    }
}
