package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IPlaceType;
import com.mcommings.campaigner.models.PlaceType;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.IPlaceTypesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public void savePlaceType(PlaceType placeType) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.nameIsNullOrEmpty(placeType)) {
            throw new IllegalArgumentException("Place Type name cannot be null or empty.");
        }
        if(RepositoryHelper.nameAlreadyExists(placeTypesRepository, placeType)) {
            throw new DataIntegrityViolationException("Place Type already exists.");
        }
        placeTypesRepository.saveAndFlush(placeType);
    }

    @Override
    @Transactional
    public void deletePlaceType(int placeTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(placeTypesRepository, placeTypeId)) {
            throw new IllegalArgumentException("Unable to delete; This place type was not found.");
        }
        //TODO: check if foreign key

        placeTypesRepository.deleteById(placeTypeId);
    }

    @Override
    @Transactional
    public void updatePlaceType(int placeTypeId, PlaceType placeType) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(placeTypesRepository, placeTypeId)) {
            throw new IllegalArgumentException("Unable to update; This place type was not found.");
        }
        PlaceType placeTypeToUpdate = RepositoryHelper.getById(placeTypesRepository, placeTypeId);
        placeTypeToUpdate.setName(placeType.getName());
        placeTypeToUpdate.setDescription(placeType.getDescription());
    }
}
