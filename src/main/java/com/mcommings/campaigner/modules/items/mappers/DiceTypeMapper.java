package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.items.dtos.dice_types.CreateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.UpdateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.ViewDiceTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DiceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface DiceTypeMapper {
    @Mapping(target = "id", ignore = true)
    DiceType toEntity(CreateDiceTypeDTO dto);

    ViewDiceTypeDTO toDto(DiceType DiceType);

    void updateDiceTypeFromDto(
            UpdateDiceTypeDTO dto,
            @MappingTarget DiceType entity
    );
}
