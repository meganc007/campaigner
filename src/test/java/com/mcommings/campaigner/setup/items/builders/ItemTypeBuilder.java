package com.mcommings.campaigner.setup.items.builders;

import com.mcommings.campaigner.modules.items.entities.ItemType;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;

public class ItemTypeBuilder {

    private int id = ItemsTestConstants.ITEM_TYPE_ID;
    private String name = ItemsTestConstants.ITEM_TYPE_NAME;
    private String description = ItemsTestConstants.ITEM_TYPE_DESCRIPTION;

    public static ItemTypeBuilder aItemType() {
        return new ItemTypeBuilder();
    }

    public ItemTypeBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ItemTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ItemTypeBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ItemType build() {
        ItemType itemType = new ItemType();
        itemType.setId(id);
        itemType.setName(name);
        itemType.setDescription(description);

        return itemType;
    }
}
