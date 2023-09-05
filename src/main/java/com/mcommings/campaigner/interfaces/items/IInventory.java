package com.mcommings.campaigner.interfaces.items;

import com.mcommings.campaigner.models.items.Inventory;

import java.util.List;

public interface IInventory {

    List<Inventory> getInventories();

    void saveInventory(Inventory inventory);

    void deleteInventory(int inventoryId);

    void updateInventory(int inventoryId, Inventory inventory);
}
