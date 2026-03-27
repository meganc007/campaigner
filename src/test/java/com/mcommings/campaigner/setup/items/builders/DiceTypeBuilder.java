package com.mcommings.campaigner.setup.items.builders;

import com.mcommings.campaigner.modules.items.entities.DiceType;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;

public class DiceTypeBuilder {

    private int id = ItemsTestConstants.DICE_TYPE_ID;
    private String name = ItemsTestConstants.DICE_TYPE_NAME;
    private String description = ItemsTestConstants.DICE_TYPE_DESCRIPTION;
    private int maxRoll = ItemsTestConstants.DICE_TYPE_MAX_ROLL;

    public static DiceTypeBuilder aDiceType() {
        return new DiceTypeBuilder();
    }

    public DiceTypeBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public DiceTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DiceTypeBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DiceTypeBuilder withMaxRoll(Integer maxRoll) {
        this.maxRoll = maxRoll;
        return this;
    }

    public DiceType build() {
        DiceType diceType = new DiceType();
        diceType.setId(id);
        diceType.setName(name);
        diceType.setDescription(description);
        diceType.setMaxRoll(maxRoll);

        return diceType;
    }
}
