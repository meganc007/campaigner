package com.mcommings.campaigner.setup.items.factories;

import com.mcommings.campaigner.modules.items.dtos.damage_types.CreateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.UpdateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.ViewDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.CreateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.UpdateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.ViewDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.item_types.CreateItemTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.item_types.UpdateItemTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.item_types.ViewItemTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.CreateWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.UpdateWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.ViewWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapons.CreateWeaponDTO;
import com.mcommings.campaigner.modules.items.dtos.weapons.UpdateWeaponDTO;
import com.mcommings.campaigner.modules.items.dtos.weapons.ViewWeaponDTO;
import com.mcommings.campaigner.modules.items.entities.*;
import com.mcommings.campaigner.setup.items.builders.*;
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

    //WEAPON TYPES
    public static WeaponType weaponType() {
        return WeaponTypeBuilder.aWeaponType().build();
    }

    public static ViewWeaponTypeDTO viewWeaponTypeDTO() {
        ViewWeaponTypeDTO dto = new ViewWeaponTypeDTO();
        dto.setId(ItemsTestConstants.WEAPON_TYPE_ID);
        dto.setName(ItemsTestConstants.WEAPON_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.WEAPON_TYPE_DESCRIPTION);
        return dto;
    }

    public static CreateWeaponTypeDTO createWeaponTypeDTO() {
        CreateWeaponTypeDTO dto = new CreateWeaponTypeDTO();
        dto.setName(ItemsTestConstants.WEAPON_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.WEAPON_TYPE_DESCRIPTION);
        return dto;
    }

    public static UpdateWeaponTypeDTO updateWeaponTypeDTO() {
        UpdateWeaponTypeDTO dto = new UpdateWeaponTypeDTO();
        dto.setId(ItemsTestConstants.WEAPON_TYPE_ID);
        dto.setName(ItemsTestConstants.WEAPON_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.WEAPON_TYPE_DESCRIPTION);
        return dto;
    }

    //ITEM TYPES
    public static ItemType itemType() {
        return ItemTypeBuilder.aItemType().build();
    }

    public static ViewItemTypeDTO viewItemTypeDTO() {
        ViewItemTypeDTO dto = new ViewItemTypeDTO();
        dto.setId(ItemsTestConstants.ITEM_TYPE_ID);
        dto.setName(ItemsTestConstants.ITEM_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.ITEM_TYPE_DESCRIPTION);
        return dto;
    }

    public static CreateItemTypeDTO createItemTypeDTO() {
        CreateItemTypeDTO dto = new CreateItemTypeDTO();
        dto.setName(ItemsTestConstants.ITEM_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.ITEM_TYPE_DESCRIPTION);
        return dto;
    }

    public static UpdateItemTypeDTO updateItemTypeDTO() {
        UpdateItemTypeDTO dto = new UpdateItemTypeDTO();
        dto.setId(ItemsTestConstants.ITEM_TYPE_ID);
        dto.setName(ItemsTestConstants.ITEM_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.ITEM_TYPE_DESCRIPTION);
        return dto;
    }

    //WEAPON
    public static Weapon weapon() {
        return WeaponBuilder.aWeapon().build();
    }

    public static ViewWeaponDTO viewWeaponDTO() {
        ViewWeaponDTO dto = new ViewWeaponDTO();
        dto.setId(ItemsTestConstants.WEAPON_TYPE_ID);
        dto.setName(ItemsTestConstants.WEAPON_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.WEAPON_TYPE_DESCRIPTION);
        dto.setCampaignUuid(ItemsTestConstants.CAMPAIGN_UUID);
        dto.setRarity(ItemsTestConstants.WEAPON_RARITY);
        dto.setGoldValue(ItemsTestConstants.WEAPON_GOLD_VALUE);
        dto.setSilverValue(ItemsTestConstants.WEAPON_SILVER_VALUE);
        dto.setCopperValue(ItemsTestConstants.WEAPON_COPPER_VALUE);
        dto.setWeight(ItemsTestConstants.WEAPON_WEIGHT);
        dto.setWeaponTypeId(ItemsTestConstants.WEAPON_TYPE_ID);
        dto.setDamageTypeId(ItemsTestConstants.DAMAGE_TYPE_ID);
        dto.setDiceTypeId(ItemsTestConstants.DICE_TYPE_ID);
        dto.setNumberOfDice(ItemsTestConstants.WEAPON_NUMBER_OF_DICE);
        dto.setDamageModifier(ItemsTestConstants.WEAPON_DAMAGE_MODIFIER);
        dto.setIsMagical(ItemsTestConstants.WEAPON_IS_MAGICAL);
        dto.setIsCursed(ItemsTestConstants.WEAPON_IS_CURSED);
        dto.setNotes(ItemsTestConstants.WEAPON_NOTES);
        return dto;
    }

    public static CreateWeaponDTO createWeaponDTO() {
        CreateWeaponDTO dto = new CreateWeaponDTO();
        dto.setName(ItemsTestConstants.WEAPON_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.WEAPON_TYPE_DESCRIPTION);
        dto.setCampaignUuid(ItemsTestConstants.CAMPAIGN_UUID);
        dto.setRarity(ItemsTestConstants.WEAPON_RARITY);
        dto.setGoldValue(ItemsTestConstants.WEAPON_GOLD_VALUE);
        dto.setSilverValue(ItemsTestConstants.WEAPON_SILVER_VALUE);
        dto.setCopperValue(ItemsTestConstants.WEAPON_COPPER_VALUE);
        dto.setWeight(ItemsTestConstants.WEAPON_WEIGHT);
        dto.setWeaponTypeId(ItemsTestConstants.WEAPON_TYPE_ID);
        dto.setDamageTypeId(ItemsTestConstants.DAMAGE_TYPE_ID);
        dto.setDiceTypeId(ItemsTestConstants.DICE_TYPE_ID);
        dto.setNumberOfDice(ItemsTestConstants.WEAPON_NUMBER_OF_DICE);
        dto.setDamageModifier(ItemsTestConstants.WEAPON_DAMAGE_MODIFIER);
        dto.setIsMagical(ItemsTestConstants.WEAPON_IS_MAGICAL);
        dto.setIsCursed(ItemsTestConstants.WEAPON_IS_CURSED);
        dto.setNotes(ItemsTestConstants.WEAPON_NOTES);
        return dto;
    }

    public static UpdateWeaponDTO updateWeaponDTO() {
        UpdateWeaponDTO dto = new UpdateWeaponDTO();
        dto.setId(ItemsTestConstants.WEAPON_TYPE_ID);
        dto.setName(ItemsTestConstants.WEAPON_TYPE_NAME);
        dto.setDescription(ItemsTestConstants.WEAPON_TYPE_DESCRIPTION);
        dto.setCampaignUuid(ItemsTestConstants.CAMPAIGN_UUID);
        return dto;
    }
}
