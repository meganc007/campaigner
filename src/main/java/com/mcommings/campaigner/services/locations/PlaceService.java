package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.interfaces.locations.IPlace;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.locations.Place;
import com.mcommings.campaigner.repositories.locations.*;
import com.mcommings.campaigner.repositories.people.IEventPlacePersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_PLACE;

@Service
public class PlaceService implements IPlace {

    private final IPlaceRepository placeRepository;
    private final IPlaceTypesRepository placeTypesRepository;
    private final ITerrainRepository terrainRepository;
    private final ICountryRepository countryRepository;
    private final ICityRepository cityRepository;
    private final IRegionRepository regionRepository;
    private final IEventPlacePersonRepository eventPlacePersonRepository;


    @Autowired
    public PlaceService(IPlaceRepository placeRepository, IPlaceTypesRepository placeTypesRepository,
                        ITerrainRepository terrainRepository, ICountryRepository countryRepository,
                        ICityRepository cityRepository, IRegionRepository regionRepository,
                        IEventPlacePersonRepository eventPlacePersonRepository) {
        this.placeRepository = placeRepository;
        this.placeTypesRepository = placeTypesRepository;
        this.terrainRepository = terrainRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.regionRepository = regionRepository;
        this.eventPlacePersonRepository = eventPlacePersonRepository;
    }

    @Override
    public List<Place> getPlaces() {
        return placeRepository.findAll();
    }

    @Override
    @Transactional
    public void savePlace(Place place) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(place)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(placeRepository, place)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        if (hasForeignKeys(place) &&
                RepositoryHelper.foreignKeyIsNotValid(placeRepository, getListOfForeignKeyRepositories(), place)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        placeRepository.saveAndFlush(place);
    }

    @Override
    @Transactional
    public void deletePlace(int placeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(placeRepository, placeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWherePlaceIsAForeignKey(), FK_PLACE.columnName, placeId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        placeRepository.deleteById(placeId);
    }

    @Override
    @Transactional
    public void updatePlace(int placeId, Place place) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(placeRepository, placeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(place) &&
                RepositoryHelper.foreignKeyIsNotValid(placeRepository, getListOfForeignKeyRepositories(), place)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        Place placeToUpdate = RepositoryHelper.getById(placeRepository, placeId);
        placeToUpdate.setName(place.getName());
        placeToUpdate.setDescription(place.getDescription());
        placeToUpdate.setFk_place_type(place.getFk_place_type());
        placeToUpdate.setFk_terrain(place.getFk_terrain());
        placeToUpdate.setFk_country(place.getFk_country());
        placeToUpdate.setFk_city(place.getFk_city());
        placeToUpdate.setFk_region(place.getFk_region());
    }

    private List<CrudRepository> getReposWherePlaceIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(eventPlacePersonRepository));
    }

    private boolean hasForeignKeys(Place place) {
        return place.getFk_place_type() != null || place.getFk_terrain() != null || place.getFk_country() != null || place.getFk_city() != null || place.getFk_region() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(placeTypesRepository, terrainRepository, countryRepository, cityRepository,
                regionRepository));
    }

}
