package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.interfaces.locations.ICountry;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.locations.Country;
import com.mcommings.campaigner.repositories.ICampaignRepository;
import com.mcommings.campaigner.repositories.IEventRepository;
import com.mcommings.campaigner.repositories.IGovernmentRepository;
import com.mcommings.campaigner.repositories.locations.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.*;

@Service
public class CountryService implements ICountry {

    private final ICountryRepository countryRepository;
    private final ICampaignRepository campaignRepository;
    private final IContinentRepository continentRepository;
    private final IGovernmentRepository governmentRepository;
    private final IRegionRepository regionRepository;
    private final ICityRepository cityRepository;
    private final IEventRepository eventRepository;
    private final IPlaceRepository placeRepository;

    @Autowired
    public CountryService(ICountryRepository countryRepository, ICampaignRepository campaignRepository,
                          IContinentRepository continentRepository, IGovernmentRepository governmentRepository,
                          ICityRepository cityRepository, IRegionRepository regionRepository, IEventRepository eventRepository,
                          IPlaceRepository placeRepository) {
        this.countryRepository = countryRepository;
        this.campaignRepository = campaignRepository;
        this.continentRepository = continentRepository;
        this.governmentRepository = governmentRepository;
        this.cityRepository = cityRepository;
        this.regionRepository = regionRepository;
        this.eventRepository = eventRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Country> getCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Country getCountry(int countryId) {
        return RepositoryHelper.getById(countryRepository, countryId);
    }

    @Override
    public List<Country> getCountriesByCampaignUUID(UUID uuid) {
        return countryRepository.findByfk_campaign_uuid(uuid);
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
                RepositoryHelper.foreignKeyIsNotValid(buildReposAndColumnsHashMap(country), country)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }
        Country countryToUpdate = RepositoryHelper.getById(countryRepository, countryId);
        if (country.getName() != null) countryToUpdate.setName(country.getName());
        if (country.getDescription() != null) countryToUpdate.setDescription(country.getDescription());
        if (country.getFk_campaign_uuid() != null) countryToUpdate.setFk_campaign_uuid(country.getFk_campaign_uuid());
        if (country.getFk_continent() != null) countryToUpdate.setFk_continent(country.getFk_continent());
        if (country.getFk_government() != null) countryToUpdate.setFk_government(country.getFk_government());
    }

    private List<CrudRepository> getReposWhereCountryIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(cityRepository, regionRepository, eventRepository, placeRepository));
    }

    private boolean hasForeignKeys(Country country) {
        return country.getFk_campaign_uuid() != null && (country.getFk_continent() != null || country.getFk_government() != null);
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(continentRepository, governmentRepository));
    }

    private HashMap<CrudRepository, String> buildReposAndColumnsHashMap(Country country) {
        HashMap<CrudRepository, String> reposAndColumns = new HashMap<>();

        if (country.getFk_continent() != null) {
            reposAndColumns.put(continentRepository, FK_CONTINENT.columnName);
        }
        if (country.getFk_government() != null) {
            reposAndColumns.put(governmentRepository, FK_GOVERNMENT.columnName);
        }
        return reposAndColumns;
    }

}
