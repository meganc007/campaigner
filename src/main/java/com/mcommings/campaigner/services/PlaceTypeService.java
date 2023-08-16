package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IPlaceType;
import com.mcommings.campaigner.models.PlaceType;
import com.mcommings.campaigner.models.repositories.IPlaceTypesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceTypeService implements IPlaceType {

    private final IPlaceTypesRepository placeTypesRepository;

    @Autowired
    public PlaceTypeService(IPlaceTypesRepository placeTypesRepository) {this.placeTypesRepository = placeTypesRepository;}

    @Override
    public List<PlaceType> getPlaceTypes() {
        return placeTypesRepository.findAll();
    }

    @Override
    @Transactional
    public void savePlaceType(PlaceType placeType) {

    }

    @Override
    @Transactional
    public void deletePlaceType(int placeTypeId) {

    }

    @Override
    @Transactional
    public void updatePlaceType(int placeTypeId, PlaceType placeType) {

    }
}
