package com.mcommings.campaigner.interfaces.items;

import com.mcommings.campaigner.entities.items.ItemType;

import java.util.List;

public interface IItemType {

    List<ItemType> getItemTypes();

    void saveItemType(ItemType itemType);

    void deleteItemType(int itemTypeId);

    void updateItemType(int itemTypeId, ItemType itemType);
}
