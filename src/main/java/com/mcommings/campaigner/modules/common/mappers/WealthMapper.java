package com.mcommings.campaigner.modules.common.mappers;

import com.mcommings.campaigner.modules.common.dtos.wealth.CreateWealthDTO;
import com.mcommings.campaigner.modules.common.dtos.wealth.UpdateWealthDTO;
import com.mcommings.campaigner.modules.common.dtos.wealth.ViewWealthDTO;
import com.mcommings.campaigner.modules.common.entities.Wealth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface WealthMapper {
    @Mapping(target = "id", ignore = true)
    Wealth toEntity(CreateWealthDTO dto);

    ViewWealthDTO toDto(Wealth wealth);

    void updateWealthFromDto(
            UpdateWealthDTO dto,
            @MappingTarget Wealth entity
    );
}
