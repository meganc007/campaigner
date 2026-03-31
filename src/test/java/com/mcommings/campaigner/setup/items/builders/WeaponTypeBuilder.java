package com.mcommings.campaigner.setup.items.builders;

import com.mcommings.campaigner.modules.items.entities.WeaponType;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;

public class WeaponTypeBuilder {

    private int id = ItemsTestConstants.WEAPON_TYPE_ID;
    private String name = ItemsTestConstants.WEAPON_TYPE_NAME;
    private String description = ItemsTestConstants.WEAPON_TYPE_DESCRIPTION;

    public static WeaponTypeBuilder aWeaponType() {
        return new WeaponTypeBuilder();
    }

    public WeaponTypeBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public WeaponTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public WeaponTypeBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public WeaponType build() {
        WeaponType weaponType = new WeaponType();
        weaponType.setId(id);
        weaponType.setName(name);
        weaponType.setDescription(description);

        return weaponType;
    }
}
