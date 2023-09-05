package com.mcommings.campaigner.interfaces.items;

import com.mcommings.campaigner.models.items.Item;

import java.util.List;

public interface IItem {

    List<Item> getItems();

    void saveItem(Item item);

    void deleteItem(int itemId);

    void updateItem(int itemId, Item item);
}
