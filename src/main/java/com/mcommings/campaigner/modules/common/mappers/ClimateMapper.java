package com.mcommings.campaigner.modules.common.mappers;

import com.mcommings.campaigner.modules.common.dtos.ClimateDTO;
import com.mcommings.campaigner.modules.common.entities.Climate;
import org.mapstruct.Mapper;

@Mapper
public interface ClimateMapper {
    Climate mapFromClimateDto(ClimateDTO dto);

    ClimateDTO mapToClimateDto(Climate climate);
}
