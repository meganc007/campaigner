package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IPlace;
import com.mcommings.campaigner.models.Place;
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

    }

    @Override
    @Transactional
    public void deletePlace(int placeId) throws IllegalArgumentException, DataIntegrityViolationException {

    }

    @Override
    @Transactional
    public void updatePlace(int placeId, Place place) throws IllegalArgumentException, DataIntegrityViolationException {

    }

    private boolean hasForeignKeys(Place place) {
        return place.getFk_place_type() != null || place.getFk_terrain() != null || place.getFk_country() != null || place.getFk_city() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(placeTypesRepository, terrainRepository, countryRepository, cityRepository));
        return repositories;
    }

}
