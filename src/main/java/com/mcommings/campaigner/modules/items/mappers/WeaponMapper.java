package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.items.dtos.weapons.CreateWeaponDTO;
import com.mcommings.campaigner.modules.items.dtos.weapons.UpdateWeaponDTO;
import com.mcommings.campaigner.modules.items.dtos.weapons.ViewWeaponDTO;
import com.mcommings.campaigner.modules.items.entities.Weapon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface WeaponMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    Weapon toEntity(CreateWeaponDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    @Mapping(source = "weaponType.id", target = "weaponTypeId")
    @Mapping(source = "damageType.id", target = "damageTypeId")
    @Mapping(source = "diceType.id", target = "diceTypeId")
    ViewWeaponDTO toDto(Weapon weapon);

    void updateWeaponFromDto(
            UpdateWeaponDTO dto,
            @MappingTarget Weapon entity
    );
}
