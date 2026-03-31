package com.mcommings.campaigner.setup.items.builders;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.items.entities.DamageType;
import com.mcommings.campaigner.modules.items.entities.DiceType;
import com.mcommings.campaigner.modules.items.entities.Weapon;
import com.mcommings.campaigner.modules.items.entities.WeaponType;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;

import java.util.UUID;

public class WeaponBuilder {

    private int id = ItemsTestConstants.WEAPON_ID;
    private String name = ItemsTestConstants.WEAPON_NAME;
    private String description = ItemsTestConstants.WEAPON_DESCRIPTION;
    private UUID campaignUuid = ItemsTestConstants.CAMPAIGN_UUID;
    private String rarity = ItemsTestConstants.WEAPON_RARITY;
    private Integer goldValue = ItemsTestConstants.WEAPON_GOLD_VALUE;
    private Integer silverValue = ItemsTestConstants.WEAPON_SILVER_VALUE;
    private Integer copperValue = ItemsTestConstants.WEAPON_COPPER_VALUE;
    private float weight = ItemsTestConstants.WEAPON_WEIGHT;
    private Integer weaponTypeId = ItemsTestConstants.WEAPON_TYPE_ID;
    private Integer damageTypeId = ItemsTestConstants.DAMAGE_TYPE_ID;
    private Integer diceTypeId = ItemsTestConstants.DICE_TYPE_ID;
    private Integer numberOfDice = ItemsTestConstants.WEAPON_NUMBER_OF_DICE;
    private Integer damageModifier = ItemsTestConstants.WEAPON_DAMAGE_MODIFIER;
    private Boolean isMagical = ItemsTestConstants.WEAPON_IS_MAGICAL;
    private Boolean isCursed = ItemsTestConstants.WEAPON_IS_CURSED;
    private String notes = ItemsTestConstants.WEAPON_NOTES;

    public static WeaponBuilder aWeapon() {
        return new WeaponBuilder();
    }

    public WeaponBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public WeaponBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public WeaponBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public WeaponBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public WeaponBuilder withRarity(String rarity) {
        this.rarity = rarity;
        return this;
    }

    public WeaponBuilder withGoldValue(int goldValue) {
        this.goldValue = goldValue;
        return this;
    }

    public WeaponBuilder withSilverValue(int silverValue) {
        this.silverValue = silverValue;
        return this;
    }

    public WeaponBuilder withCopperValue(int copperValue) {
        this.copperValue = copperValue;
        return this;
    }

    public WeaponBuilder withWeight(float weight) {
        this.weight = weight;
        return this;
    }

    public WeaponBuilder withNumberOfDice(int numberOfDice) {
        this.numberOfDice = numberOfDice;
        return this;
    }

    public WeaponBuilder withDamageModifier(int damageModifier) {
        this.damageModifier = damageModifier;
        return this;
    }

    public WeaponBuilder withIsMagical(boolean isMagical) {
        this.isMagical = isMagical;
        return this;
    }

    public WeaponBuilder withIsCursed(boolean isCursed) {
        this.isCursed = isCursed;
        return this;
    }

    public WeaponBuilder withNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public Weapon build() {
        Weapon weapon = new Weapon();
        weapon.setId(id);
        weapon.setName(name);
        weapon.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        weapon.setCampaign(campaign);

        weapon.setRarity(rarity);
        weapon.setGoldValue(goldValue);
        weapon.setSilverValue(silverValue);
        weapon.setCopperValue(copperValue);
        weapon.setWeight(weight);

        WeaponType weaponType = new WeaponType();
        weaponType.setId(weaponTypeId);
        weapon.setWeaponType(weaponType);

        DamageType damageType = new DamageType();
        damageType.setId(damageTypeId);
        weapon.setDamageType(damageType);

        DiceType diceType = new DiceType();
        diceType.setId(diceTypeId);
        weapon.setDiceType(diceType);

        weapon.setNumberOfDice(numberOfDice);
        weapon.setDamageModifier(damageModifier);
        weapon.setIsMagical(isMagical);
        weapon.setIsCursed(isCursed);
        weapon.setNotes(notes);

        return weapon;
    }
}
