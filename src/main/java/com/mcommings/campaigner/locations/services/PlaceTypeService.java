package com.mcommings.campaigner.locations.services;

import com.mcommings.campaigner.common.entities.RepositoryHelper;
import com.mcommings.campaigner.locations.dtos.PlaceTypeDTO;
import com.mcommings.campaigner.locations.mappers.PlaceTypeMapper;
import com.mcommings.campaigner.locations.repositories.IPlaceTypesRepository;
import com.mcommings.campaigner.locations.services.interfaces.IPlaceType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class PlaceTypeService implements IPlaceType {

    private final IPlaceTypesRepository placeTypesRepository;
    private final PlaceTypeMapper placeTypeMapper;

    @Override
    public List<PlaceTypeDTO> getPlaceTypes() {

        return placeTypesRepository.findAll()
                .stream()
                .map(placeTypeMapper::mapToPlaceTypeDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PlaceTypeDTO> getPlaceType(int placeTypeId) {

        return placeTypesRepository.findById(placeTypeId)
                .map(placeTypeMapper::mapToPlaceTypeDto);
    }

    @Override
    public void savePlaceType(PlaceTypeDTO placeType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(placeType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(placeTypesRepository, placeType.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        placeTypeMapper.mapToPlaceTypeDto(
                placeTypesRepository.save(
                        placeTypeMapper.mapFromPlaceTypeDto(placeType)
                ));
    }

    @Override
    public void deletePlaceType(int placeTypeId) throws IllegalArgumentException {
        if (RepositoryHelper.cannotFindId(placeTypesRepository, placeTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        placeTypesRepository.deleteById(placeTypeId);
    }

    @Override
    public Optional<PlaceTypeDTO> updatePlaceType(int placeTypeId, PlaceTypeDTO placeType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(placeTypesRepository, placeTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(placeType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(placeTypesRepository, placeType.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return placeTypesRepository.findById(placeTypeId).map(foundPlaceType -> {
            if (placeType.getName() != null) foundPlaceType.setName(placeType.getName());
            if (placeType.getDescription() != null) foundPlaceType.setDescription(placeType.getDescription());

            return placeTypeMapper.mapToPlaceTypeDto(placeTypesRepository.save(foundPlaceType));
        });
    }
}
