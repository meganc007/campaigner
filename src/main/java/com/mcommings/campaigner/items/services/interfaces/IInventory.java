package com.mcommings.campaigner.items.services.interfaces;

import com.mcommings.campaigner.items.entities.Inventory;

import java.util.List;
import java.util.UUID;

public interface IInventory {

    List<Inventory> getInventories();

    List<Inventory> getInventoriesByCampaignUUID(UUID uuid);

    List<Inventory> getInventoriesByItem(int itemId);

    List<Inventory> getInventoriesByPerson(int personId);

    List<Inventory> getInventoriesByPlace(int placeId);

    List<Inventory> getInventoriesByWeapon(int weaponId);

    void saveInventory(Inventory inventory);

    void deleteInventory(int inventoryId);

    void updateInventory(int inventoryId, Inventory inventory);
}
