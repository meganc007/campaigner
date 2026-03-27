package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.CreateWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.UpdateWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.ViewWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.entities.WeaponType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface WeaponTypeMapper {

    @Mapping(target = "id", ignore = true)
    WeaponType toEntity(CreateWeaponTypeDTO dto);

    ViewWeaponTypeDTO toDto(WeaponType WeaponType);

    void updateWeaponTypeFromDto(
            UpdateWeaponTypeDTO dto,
            @MappingTarget WeaponType entity
    );
}
