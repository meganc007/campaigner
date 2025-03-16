package com.mcommings.campaigner.modules.items.services.interfaces;

import com.mcommings.campaigner.modules.items.dtos.InventoryDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IInventory {

    List<InventoryDTO> getInventories();

    Optional<InventoryDTO> getInventory(int inventoryId);

    List<InventoryDTO> getInventoriesByCampaignUUID(UUID uuid);

    List<InventoryDTO> getInventoriesByItem(int itemId);

    List<InventoryDTO> getInventoriesByPerson(int personId);

    List<InventoryDTO> getInventoriesByPlace(int placeId);

    List<InventoryDTO> getInventoriesByWeapon(int weaponId);

    void saveInventory(InventoryDTO inventory);

    void deleteInventory(int inventoryId);

    Optional<InventoryDTO> updateInventory(int inventoryId, InventoryDTO inventory);
}
