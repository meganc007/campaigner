package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.interfaces.locations.ICountry;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.locations.Country;
import com.mcommings.campaigner.repositories.IGovernmentRepository;
import com.mcommings.campaigner.repositories.locations.ICityRepository;
import com.mcommings.campaigner.repositories.locations.IContinentRepository;
import com.mcommings.campaigner.repositories.locations.ICountryRepository;
import com.mcommings.campaigner.repositories.locations.IRegionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_COUNTRY;

@Service
public class CountryService implements ICountry {

    private final ICountryRepository countryRepository;
    private final IContinentRepository continentRepository;
    private final IGovernmentRepository governmentRepository;
    private final IRegionRepository regionRepository;

    private final ICityRepository cityRepository;

    @Autowired
    public CountryService(ICountryRepository countryRepository, IContinentRepository continentRepository,
                          IGovernmentRepository governmentRepository, ICityRepository cityRepository,
                          IRegionRepository regionRepository) {
        this.countryRepository = countryRepository;
        this.continentRepository = continentRepository;
        this.governmentRepository = governmentRepository;
        this.cityRepository = cityRepository;
        this.regionRepository = regionRepository;
    }

    @Override
    public List<Country> getCountries() {
        return countryRepository.findAll();
    }

    @Override
    @Transactional
    public void saveCountry(Country country) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(country)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(countryRepository, country)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        if (hasForeignKeys(country) &&
                RepositoryHelper.foreignKeyIsNotValid(countryRepository, getListOfForeignKeyRepositories(), country)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        countryRepository.saveAndFlush(country);
    }

    @Override
    @Transactional
    public void deleteCountry(int countryId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(countryRepository, countryId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereCountryIsAForeignKey(), FK_COUNTRY.columnName, countryId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        countryRepository.deleteById(countryId);
    }

    @Override
    @Transactional
    public void updateCountry(int countryId, Country country) throws IllegalArgumentException,
            DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(countryRepository, countryId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(country) &&
                RepositoryHelper.foreignKeyIsNotValid(countryRepository, getListOfForeignKeyRepositories(), country)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        Country countryToUpdate = RepositoryHelper.getById(countryRepository, countryId);
        countryToUpdate.setName(country.getName());
        countryToUpdate.setDescription(country.getDescription());
        countryToUpdate.setFk_continent(country.getFk_continent());
        countryToUpdate.setFk_government(country.getFk_government());
    }

    private List<CrudRepository> getReposWhereCountryIsAForeignKey() {
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(cityRepository, regionRepository));
        return repositories;
    }

    private boolean hasForeignKeys(Country country) {
        return country.getFk_continent() != null || country.getFk_government() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(continentRepository, governmentRepository));
        return repositories;
    }

}
