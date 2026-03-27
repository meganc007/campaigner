package com.mcommings.campaigner.setup.items.factories;

import com.mcommings.campaigner.modules.items.dtos.damage_types.CreateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.UpdateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.ViewDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.CreateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.UpdateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.ViewDiceTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DamageType;
import com.mcommings.campaigner.modules.items.entities.DiceType;
import com.mcommings.campaigner.setup.items.builders.DamageTypeBuilder;
import com.mcommings.campaigner.setup.items.builders.DiceTypeBuilder;
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

    //DICE TYPES
    public static DiceType diceType() {
        return DiceTypeBuilder.aDiceType().build();
    }

    public static ViewDiceTypeDTO viewDiceTypeDTO() {
        ViewDiceTypeDTO dto = new ViewDiceTypeDTO();
        dto.setId(ItemsTestConstants.DICE_TYPE_ID);
        dto.setName(ItemsTestConstants.DICE_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.DICE_TYPE_DESCRIPTION);
        dto.setMaxRoll(ItemsTestConstants.DICE_TYPE_MAX_ROLL);
        return dto;
    }

    public static CreateDiceTypeDTO createDiceTypeDTO() {
        CreateDiceTypeDTO dto = new CreateDiceTypeDTO();
        dto.setName(ItemsTestConstants.DICE_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.DICE_TYPE_DESCRIPTION);
        dto.setMaxRoll(ItemsTestConstants.DICE_TYPE_MAX_ROLL);
        return dto;
    }

    public static UpdateDiceTypeDTO updateDiceTypeDTO() {
        UpdateDiceTypeDTO dto = new UpdateDiceTypeDTO();
        dto.setId(ItemsTestConstants.DICE_TYPE_ID);
        dto.setName(ItemsTestConstants.DICE_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.DICE_TYPE_DESCRIPTION);
        return dto;
    }
}
