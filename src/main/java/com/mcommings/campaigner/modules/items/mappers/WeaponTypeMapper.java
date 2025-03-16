package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.modules.items.dtos.WeaponTypeDTO;
import com.mcommings.campaigner.modules.items.entities.WeaponType;
import org.mapstruct.Mapper;

@Mapper
public interface WeaponTypeMapper {
    WeaponType mapFromWeaponTypeDto(WeaponTypeDTO dto);

    WeaponTypeDTO mapToWeaponTypeDto(WeaponType weaponType);
}
