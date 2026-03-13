package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.locations.dtos.place_types.CreatePlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.place_types.UpdatePlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.place_types.ViewPlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.entities.PlaceType;
import com.mcommings.campaigner.modules.locations.mappers.PlaceTypeMapper;
import com.mcommings.campaigner.modules.locations.repositories.IPlaceTypesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceTypeService extends BaseService<
        PlaceType,
        Integer,
        ViewPlaceTypeDTO,
        CreatePlaceTypeDTO,
        UpdatePlaceTypeDTO> {

    private final IPlaceTypesRepository placeTypesRepository;
    private final PlaceTypeMapper placeTypeMapper;

    @Override
    protected JpaRepository<PlaceType, Integer> getRepository() {
        return placeTypesRepository;
    }

    @Override
    protected ViewPlaceTypeDTO toViewDto(PlaceType entity) {
        return placeTypeMapper.toDto(entity);
    }

    @Override
    protected PlaceType toEntity(CreatePlaceTypeDTO dto) {
        return placeTypeMapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(UpdatePlaceTypeDTO dto, PlaceType entity) {
        placeTypeMapper.updatePlaceTypeFromDto(dto, entity);
    }

    @Override
    protected Integer getId(UpdatePlaceTypeDTO dto) {
        return dto.getId();
    }
}
