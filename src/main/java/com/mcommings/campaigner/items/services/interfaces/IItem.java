package com.mcommings.campaigner.items.services.interfaces;

import com.mcommings.campaigner.items.entities.Item;

import java.util.List;
import java.util.UUID;

public interface IItem {

    List<Item> getItems();

    List<Item> getItemsByCampaignUUID(UUID uuid);

    void saveItem(Item item);

    void deleteItem(int itemId);

    void updateItem(int itemId, Item item);
}
