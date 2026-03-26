package com.mcommings.campaigner.setup.items.factories;

import com.mcommings.campaigner.modules.items.dtos.damage_types.CreateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.UpdateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.ViewDamageTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DamageType;
import com.mcommings.campaigner.setup.items.builders.DamageTypeBuilder;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;

public class ItemsTestDataFactory {

    //DAMAGE TYPES
    public static DamageType damageType() {
        return DamageTypeBuilder.aDamageType().build();
    }

    public static ViewDamageTypeDTO viewDamageTypeDTO() {
        ViewDamageTypeDTO dto = new ViewDamageTypeDTO();
        dto.setId(ItemsTestConstants.DAMAGE_TYPE_ID);
        dto.setName(ItemsTestConstants.DAMAGE_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.DAMAGE_TYPE_DESCRIPTION);
        return dto;
    }

    public static CreateDamageTypeDTO createDamageTypeDTO() {
        CreateDamageTypeDTO dto = new CreateDamageTypeDTO();
        dto.setName(ItemsTestConstants.DAMAGE_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.DAMAGE_TYPE_DESCRIPTION);
        return dto;
    }

    public static UpdateDamageTypeDTO updateDamageTypeDTO() {
        UpdateDamageTypeDTO dto = new UpdateDamageTypeDTO();
        dto.setId(ItemsTestConstants.DAMAGE_TYPE_ID);
        dto.setName(ItemsTestConstants.DAMAGE_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.DAMAGE_TYPE_DESCRIPTION);
        return dto;
    }
}
