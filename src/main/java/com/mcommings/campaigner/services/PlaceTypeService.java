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

import static com.mcommings.campaigner.ErrorMessage.*;

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
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if(RepositoryHelper.nameAlreadyExists(placeTypesRepository, placeType)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        placeTypesRepository.saveAndFlush(placeType);
    }

    @Override
    @Transactional
    public void deletePlaceType(int placeTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(placeTypesRepository, placeTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        //TODO: check if foreign key

        placeTypesRepository.deleteById(placeTypeId);
    }

    @Override
    @Transactional
    public void updatePlaceType(int placeTypeId, PlaceType placeType) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(placeTypesRepository, placeTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        PlaceType placeTypeToUpdate = RepositoryHelper.getById(placeTypesRepository, placeTypeId);
        placeTypeToUpdate.setName(placeType.getName());
        placeTypeToUpdate.setDescription(placeType.getDescription());
    }
}
