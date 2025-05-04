package com.mcommings.campaigner.modules.common.mappers;

import com.mcommings.campaigner.modules.common.dtos.GovernmentDTO;
import com.mcommings.campaigner.modules.common.entities.Government;
import org.mapstruct.Mapper;

@Mapper
public interface GovernmentMapper {
    Government mapFromGovernmentDto(GovernmentDTO dto);

    GovernmentDTO mapToGovernmentDto(Government government);
}
