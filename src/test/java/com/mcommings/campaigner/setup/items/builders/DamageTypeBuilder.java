package com.mcommings.campaigner.setup.items.builders;

import com.mcommings.campaigner.modules.items.entities.DamageType;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;

public class DamageTypeBuilder {

    private int id = ItemsTestConstants.DAMAGE_TYPE_ID;
    private String name = ItemsTestConstants.DAMAGE_TYPE_NAME;
    private String description = ItemsTestConstants.DAMAGE_TYPE_DESCRIPTION;

    public static DamageTypeBuilder aDamageType() {
        return new DamageTypeBuilder();
    }

    public DamageTypeBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public DamageTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DamageTypeBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DamageType build() {
        DamageType damageType = new DamageType();
        damageType.setId(id);
        damageType.setName(name);
        damageType.setDescription(description);

        return damageType;
    }
}
