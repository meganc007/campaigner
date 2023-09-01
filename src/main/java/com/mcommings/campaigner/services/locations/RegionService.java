package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.interfaces.locations.IRegion;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.locations.Region;
import com.mcommings.campaigner.repositories.IClimateRepository;
import com.mcommings.campaigner.repositories.locations.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_REGION;

@Service
public class RegionService implements IRegion {

    private final IRegionRepository regionRepository;
    private final ICountryRepository countryRepository;
    private final IClimateRepository climateRepository;
    private final ICityRepository cityRepository;
    private final ILandmarkRepository landmarkRepository;
    private final IPlaceRepository placeRepository;

    @Autowired
    public RegionService(IRegionRepository regionRepository, ICountryRepository countryRepository,
                         IClimateRepository climateRepository, ICityRepository cityRepository,
                         ILandmarkRepository landmarkRepository, IPlaceRepository placeRepository) {
        this.regionRepository = regionRepository;
        this.countryRepository = countryRepository;
        this.climateRepository = climateRepository;
        this.cityRepository = cityRepository;
        this.landmarkRepository = landmarkRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Region> getRegions() {
        return regionRepository.findAll();
    }

    @Override
    @Transactional
    public void saveRegion(Region region) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(region)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(regionRepository, region)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        if (hasForeignKeys(region) &&
                RepositoryHelper.foreignKeyIsNotValid(regionRepository, getListOfForeignKeyRepositories(), region)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        regionRepository.saveAndFlush(region);
    }

    @Override
    @Transactional
    public void deleteRegion(int regionId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(regionRepository, regionId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereRegionIsAForeignKey(), FK_REGION.columnName, regionId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }
        regionRepository.deleteById(regionId);
    }

    @Override
    @Transactional
    public void updateRegion(int regionId, Region region) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(regionRepository, regionId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(region) &&
                RepositoryHelper.foreignKeyIsNotValid(regionRepository, getListOfForeignKeyRepositories(), region)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        Region regionToUpdate = RepositoryHelper.getById(regionRepository, regionId);
        regionToUpdate.setName(region.getName());
        regionToUpdate.setDescription(region.getDescription());
        regionToUpdate.setFk_country(region.getFk_country());
        regionToUpdate.setFk_climate(region.getFk_climate());
    }

    private List<CrudRepository> getReposWhereRegionIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(cityRepository, landmarkRepository, placeRepository));
    }

    private boolean hasForeignKeys(Region region) {
        return region.getFk_country() != null ||
                region.getFk_climate() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(countryRepository, climateRepository));
    }
}
