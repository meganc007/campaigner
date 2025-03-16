package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.modules.items.dtos.DiceTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DiceType;
import org.mapstruct.Mapper;

@Mapper
public interface DiceTypeMapper {
    DiceType mapFromDiceTypeDto(DiceTypeDTO dto);

    DiceTypeDTO mapToDiceTypeDto(DiceType diceType);
}
