package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IPlace;
import com.mcommings.campaigner.models.Place;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class PlaceService implements IPlace {

    private final IPlaceRepository placeRepository;
    private final IPlaceTypesRepository placeTypesRepository;
    private final ITerrainRepository terrainRepository;
    private final ICountryRepository countryRepository;
    private final ICityRepository cityRepository;


    @Autowired
    public PlaceService(IPlaceRepository placeRepository, IPlaceTypesRepository placeTypesRepository,
                        ITerrainRepository terrainRepository, ICountryRepository countryRepository,
                        ICityRepository cityRepository) {
        this.placeRepository = placeRepository;
        this.placeTypesRepository = placeTypesRepository;
        this.terrainRepository = terrainRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
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
//        TODO: add this check when tables that use Place as a fk are added
//        if (RepositoryHelper.isForeignKey(getReposWherePlaceIsAForeignKey(), FK_PLACE.columnName, placeId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }

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
    }

//    TODO: add this when tables that use Place as fk are added
//    private List<CrudRepository> getReposWherePlaceIsAForeignKey() {
//        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList());
//        return repositories;
//    }

    private boolean hasForeignKeys(Place place) {
        return place.getFk_place_type() != null || place.getFk_terrain() != null || place.getFk_country() != null || place.getFk_city() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(placeTypesRepository, terrainRepository, countryRepository, cityRepository));
        return repositories;
    }

}
