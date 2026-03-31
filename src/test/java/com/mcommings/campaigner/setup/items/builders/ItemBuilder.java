package com.mcommings.campaigner.setup.items.builders;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.items.entities.Item;
import com.mcommings.campaigner.modules.items.entities.ItemType;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;

import java.util.UUID;

public class ItemBuilder {
    private int id = ItemsTestConstants.ITEM_ID;
    private String name = ItemsTestConstants.ITEM_NAME;
    private String description = ItemsTestConstants.ITEM_DESCRIPTION;
    private UUID campaignUuid = ItemsTestConstants.CAMPAIGN_UUID;
    private String rarity = ItemsTestConstants.ITEM_RARITY;
    private Integer goldValue = ItemsTestConstants.ITEM_GOLD_VALUE;
    private Integer silverValue = ItemsTestConstants.ITEM_SILVER_VALUE;
    private Integer copperValue = ItemsTestConstants.ITEM_COPPER_VALUE;
    private float weight = ItemsTestConstants.ITEM_WEIGHT;
    private Integer itemTypeId = ItemsTestConstants.ITEM_TYPE_ID;
    private Boolean isMagical = ItemsTestConstants.ITEM_IS_MAGICAL;
    private Boolean isCursed = ItemsTestConstants.ITEM_IS_CURSED;
    private String notes = ItemsTestConstants.ITEM_NOTES;

    public static ItemBuilder aItem() {
        return new ItemBuilder();
    }

    public ItemBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ItemBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ItemBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public ItemBuilder withRarity(String rarity) {
        this.rarity = rarity;
        return this;
    }

    public ItemBuilder withGoldValue(int goldValue) {
        this.goldValue = goldValue;
        return this;
    }

    public ItemBuilder withSilverValue(int silverValue) {
        this.silverValue = silverValue;
        return this;
    }

    public ItemBuilder withCopperValue(int copperValue) {
        this.copperValue = copperValue;
        return this;
    }

    public ItemBuilder withWeight(float weight) {
        this.weight = weight;
        return this;
    }

    public ItemBuilder withIsMagical(boolean isMagical) {
        this.isMagical = isMagical;
        return this;
    }

    public ItemBuilder withIsCursed(boolean isCursed) {
        this.isCursed = isCursed;
        return this;
    }

    public ItemBuilder withNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public Item build() {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        item.setCampaign(campaign);

        item.setRarity(rarity);
        item.setGoldValue(goldValue);
        item.setSilverValue(silverValue);
        item.setCopperValue(copperValue);
        item.setWeight(weight);

        ItemType itemType = new ItemType();
        itemType.setId(itemTypeId);
        item.setItemType(itemType);

        item.setIsMagical(isMagical);
        item.setIsCursed(isCursed);
        item.setNotes(notes);

        return item;
    }
}
