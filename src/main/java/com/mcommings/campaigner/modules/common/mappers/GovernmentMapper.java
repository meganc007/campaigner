package com.mcommings.campaigner.modules.common.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.common.dtos.government.CreateGovernmentDTO;
import com.mcommings.campaigner.modules.common.dtos.government.UpdateGovernmentDTO;
import com.mcommings.campaigner.modules.common.dtos.government.ViewGovernmentDTO;
import com.mcommings.campaigner.modules.common.entities.Government;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface GovernmentMapper {
    @Mapping(target = "id", ignore = true)
    Government toEntity(CreateGovernmentDTO dto);

    ViewGovernmentDTO toDto(Government government);

    void updateGovernmentFromDto(
            UpdateGovernmentDTO dto,
            @MappingTarget Government entity
    );
}
