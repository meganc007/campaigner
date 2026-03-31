package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.items.dtos.damage_types.CreateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.UpdateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.ViewDamageTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DamageType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface DamageTypeMapper {

    @Mapping(target = "id", ignore = true)
    DamageType toEntity(CreateDamageTypeDTO dto);

    ViewDamageTypeDTO toDto(DamageType damageType);

    void updateDamageTypeFromDto(
            UpdateDamageTypeDTO dto,
            @MappingTarget DamageType entity
    );
}
