package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.modules.items.dtos.DamageTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DamageType;
import org.mapstruct.Mapper;

@Mapper
public interface DamageTypeMapper {
    DamageType mapFromDamageTypeDto(DamageTypeDTO dto);

    DamageTypeDTO mapToDamageTypeDto(DamageType damageType);
}
