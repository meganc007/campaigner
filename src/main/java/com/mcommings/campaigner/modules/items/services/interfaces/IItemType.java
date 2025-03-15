package com.mcommings.campaigner.modules.items.services.interfaces;

import com.mcommings.campaigner.modules.items.entities.ItemType;

import java.util.List;

public interface IItemType {

    List<ItemType> getItemTypes();

    void saveItemType(ItemType itemType);

    void deleteItemType(int itemTypeId);

    void updateItemType(int itemTypeId, ItemType itemType);
}
