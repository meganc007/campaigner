package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.modules.items.dtos.WeaponDTO;
import com.mcommings.campaigner.modules.items.entities.Weapon;
import org.mapstruct.Mapper;

@Mapper
public interface WeaponMapper {
    Weapon mapFromWeaponDto(WeaponDTO dto);

    WeaponDTO mapToWeaponDto(Weapon weapon);
}
