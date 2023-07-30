package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IPlaceTypes;
import com.mcommings.campaigner.models.IPlaceTypesRepository;
import com.mcommings.campaigner.models.PlaceTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceTypesService implements IPlaceTypes {

    private final IPlaceTypesRepository placeTypesRepository;

    @Autowired
    public PlaceTypesService(IPlaceTypesRepository placeTypesRepository) {this.placeTypesRepository = placeTypesRepository;}

    @Override
    public List<PlaceTypes> getPlaceTypes() {
        return placeTypesRepository.findAll();
    }
}
