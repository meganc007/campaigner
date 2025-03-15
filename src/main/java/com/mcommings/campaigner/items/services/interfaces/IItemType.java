package com.mcommings.campaigner.items.services.interfaces;

import com.mcommings.campaigner.items.entities.ItemType;

import java.util.List;

public interface IItemType {

    List<ItemType> getItemTypes();

    void saveItemType(ItemType itemType);

    void deleteItemType(int itemTypeId);

    void updateItemType(int itemTypeId, ItemType itemType);
}
