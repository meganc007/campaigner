package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IPlaceType;
import com.mcommings.campaigner.models.repositories.IPlaceTypesRepository;
import com.mcommings.campaigner.models.PlaceType;
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
}
