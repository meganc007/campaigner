package com.mcommings.campaigner.modules.common.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.common.dtos.climate.CreateClimateDTO;
import com.mcommings.campaigner.modules.common.dtos.climate.UpdateClimateDTO;
import com.mcommings.campaigner.modules.common.dtos.climate.ViewClimateDTO;
import com.mcommings.campaigner.modules.common.entities.Climate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface ClimateMapper {

    @Mapping(target = "id", ignore = true)
    Climate toEntity(CreateClimateDTO dto);

    ViewClimateDTO toDto(Climate climate);

    void updateClimateFromDto(
            UpdateClimateDTO dto,
            @MappingTarget Climate entity
    );
}
