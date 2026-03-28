package com.mcommings.campaigner.setup.items.fixtures;

import java.util.UUID;

public class ItemsTestConstants {

    private ItemsTestConstants() {
    }

    //SHARED
    public static final UUID CAMPAIGN_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");

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

}
