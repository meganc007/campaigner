package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.interfaces.locations.IPlaceType;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.locations.PlaceType;
import com.mcommings.campaigner.repositories.locations.IPlaceRepository;
import com.mcommings.campaigner.repositories.locations.IPlaceTypesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_PLACE_TYPE;

@Service
public class PlaceTypeService implements IPlaceType {

    private final IPlaceTypesRepository placeTypesRepository;
    private final IPlaceRepository placeRepository;

    @Autowired
    public PlaceTypeService(IPlaceTypesRepository placeTypesRepository, IPlaceRepository placeRepository) {
        this.placeTypesRepository = placeTypesRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<PlaceType> getPlaceTypes() {
        return placeTypesRepository.findAll();
    }

    @Override
    @Transactional
    public void savePlaceType(PlaceType placeType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(placeType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(placeTypesRepository, placeType)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        placeTypesRepository.saveAndFlush(placeType);
    }

    @Override
    @Transactional
    public void deletePlaceType(int placeTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(placeTypesRepository, placeTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWherePlaceTypeIsAForeignKey(), FK_PLACE_TYPE.columnName, placeTypeId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        placeTypesRepository.deleteById(placeTypeId);
    }

    @Override
    @Transactional
    public void updatePlaceType(int placeTypeId, PlaceType placeType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(placeTypesRepository, placeTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        PlaceType placeTypeToUpdate = RepositoryHelper.getById(placeTypesRepository, placeTypeId);
        if (placeType.getName() != null) placeTypeToUpdate.setName(placeType.getName());
        if (placeType.getDescription() != null) placeTypeToUpdate.setDescription(placeType.getDescription());
    }

    private List<CrudRepository> getReposWherePlaceTypeIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(placeRepository));
    }
}
