package com.mcommings.campaigner.setup.items.fixtures;

import java.util.UUID;

public class ItemsTestConstants {

    private ItemsTestConstants() {
    }

    //SHARED
    public static final UUID CAMPAIGN_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final int PERSON_ID = 1;

    //DAMAGE TYPES
    public static final int DAMAGE_TYPE_ID = 1;
    public static final String DAMAGE_TYPE_NAME = "bludgeoning";
    public static final String DAMAGE_TYPE_DESCRIPTION = "weapon goes bonk";

    //DICE TYPES
    public static final int DICE_TYPE_ID = 1;
    public static final String DICE_TYPE_NAME = "D20";
    public static final String DICE_TYPE_DESCRIPTION = "A 20-sided die";
    public static final int DICE_TYPE_MAX_ROLL = 20;

    //WEAPON TYPES
    public static final int WEAPON_TYPE_ID = 1;
    public static final String WEAPON_TYPE_NAME = "Dagger";
    public static final String WEAPON_TYPE_DESCRIPTION = "Simple melee weapon";

    //ITEM TYPES
    public static final int ITEM_TYPE_ID = 1;
    public static final String ITEM_TYPE_NAME = "Armor";
    public static final String ITEM_TYPE_DESCRIPTION = "Protects your body";

    //WEAPON
    public static final int WEAPON_ID = 1;
    public static final String WEAPON_NAME = "Dagger";
    public static final String WEAPON_DESCRIPTION = "Simple melee weapon";
    public static final String WEAPON_RARITY = "common";
    public static final Integer WEAPON_GOLD_VALUE = 2;
    public static final Integer WEAPON_SILVER_VALUE = 0;
    public static final Integer WEAPON_COPPER_VALUE = 0;
    public static final Float WEAPON_WEIGHT = 1.0f;
    public static final Integer WEAPON_NUMBER_OF_DICE = 1;
    public static final Integer WEAPON_DAMAGE_MODIFIER = 0;
    public static final Boolean WEAPON_IS_MAGICAL = false;
    public static final Boolean WEAPON_IS_CURSED = false;
    public static final String WEAPON_NOTES = "stick 'em with the pointy end";

    //ITEM
    public static final int ITEM_ID = 1;
    public static final String ITEM_NAME = "Health potion";
    public static final String ITEM_DESCRIPTION = "Restores health";
    public static final String ITEM_RARITY = "uncommon";
    public static final Integer ITEM_GOLD_VALUE = 20;
    public static final Integer ITEM_SILVER_VALUE = 0;
    public static final Integer ITEM_COPPER_VALUE = 0;
    public static final Float ITEM_WEIGHT = 0.5f;
    public static final Boolean ITEM_IS_MAGICAL = true;
    public static final Boolean ITEM_IS_CURSED = false;
    public static final String ITEM_NOTES = "must drink all of it to get the benefits";

    //ITEM
    public static final int INVENTORY_ID = 1;

}
