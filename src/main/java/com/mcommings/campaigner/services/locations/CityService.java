package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.entities.locations.City;
import com.mcommings.campaigner.interfaces.locations.ICity;
import com.mcommings.campaigner.repositories.IEventRepository;
import com.mcommings.campaigner.repositories.IGovernmentRepository;
import com.mcommings.campaigner.repositories.IWealthRepository;
import com.mcommings.campaigner.repositories.locations.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_CITY;

@Service
public class CityService implements ICity {

    private final ICityRepository cityRepository;
    private final IWealthRepository wealthRepository;
    private final ICountryRepository countryRepository;
    private final ISettlementTypeRepository settlementTypeRepository;
    private final IGovernmentRepository governmentRepository;
    private final IRegionRepository regionRepository;
    private final IPlaceRepository placeRepository;
    private final IEventRepository eventRepository;

    @Autowired
    public CityService(ICityRepository cityRepository, IWealthRepository wealthRepository,
                       ICountryRepository countryRepository, ISettlementTypeRepository settlementTypeRepository,
                       IGovernmentRepository governmentRepository, IRegionRepository regionRepository,
                       IPlaceRepository placeRepository, IEventRepository eventRepository) {
        this.cityRepository = cityRepository;
        this.wealthRepository = wealthRepository;
        this.countryRepository = countryRepository;
        this.settlementTypeRepository = settlementTypeRepository;
        this.governmentRepository = governmentRepository;
        this.regionRepository = regionRepository;
        this.placeRepository = placeRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<City> getCities() {
        return cityRepository.findAll();
    }

    @Override
    public City getCity(int cityId) {
        return RepositoryHelper.getById(cityRepository, cityId);
    }

    @Override
    public List<City> getCitiesByCampaignUUID(UUID uuid) {
        return cityRepository.findByfk_campaign_uuid(uuid);
    }

    @Override
    public List<City> getCitiesByCountryId(int countryId) {
        return cityRepository.findByfk_country(countryId);
    }

    @Override
    public List<City> getCitiesByRegionId(int regionId) {
        return cityRepository.findByfk_region(regionId);
    }

    @Override
    @Transactional
    public void saveCity(City city) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(city)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(cityRepository, city)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        if (hasForeignKeys(city) &&
                RepositoryHelper.foreignKeyIsNotValid(cityRepository, getListOfForeignKeyRepositories(), city)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        cityRepository.saveAndFlush(city);
    }

    @Override
    @Transactional
    public void deleteCity(int cityId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(cityRepository, cityId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereCityIsAForeignKey(), FK_CITY.columnName, cityId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }
        cityRepository.deleteById(cityId);
    }

    @Override
    @Transactional
    public void updateCity(int cityId, City city) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(cityRepository, cityId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(city) &&
                RepositoryHelper.foreignKeyIsNotValid(cityRepository, getListOfForeignKeyRepositories(), city)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        City cityToUpdate = RepositoryHelper.getById(cityRepository, cityId);
        cityToUpdate.setName(city.getName());
        cityToUpdate.setDescription(city.getDescription());
        cityToUpdate.setFk_wealth(city.getFk_wealth());
        cityToUpdate.setFk_country(city.getFk_country());
        cityToUpdate.setFk_settlement(city.getFk_settlement());
        cityToUpdate.setFk_government(city.getFk_government());
        cityToUpdate.setFk_region(city.getFk_region());
    }

    private List<CrudRepository> getReposWhereCityIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(placeRepository, eventRepository));
    }

    private boolean hasForeignKeys(City city) {
        return city.getFk_wealth() != null ||
                city.getFk_country() != null ||
                city.getFk_settlement() != null ||
                city.getFk_government() != null ||
                city.getFk_region() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(wealthRepository, countryRepository, settlementTypeRepository,
                governmentRepository, regionRepository));
    }
}
