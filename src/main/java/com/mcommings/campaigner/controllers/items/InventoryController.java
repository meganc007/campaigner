package com.mcommings.campaigner.controllers.items;

import com.mcommings.campaigner.entities.items.Inventory;
import com.mcommings.campaigner.services.items.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/items/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<Inventory> Inventory() {
        return inventoryService.getInventories();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Inventory> getInventoriesByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return inventoryService.getInventoriesByCampaignUUID(uuid);
    }

    @GetMapping(path = "/item/{itemId}")
    public List<Inventory> getInventoriesByItem(@PathVariable("itemId") int itemId) {
        return inventoryService.getInventoriesByItem(itemId);
    }

    @GetMapping(path = "/person/{personId}")
    public List<Inventory> getInventoriesByPerson(@PathVariable("personId") int personId) {
        return inventoryService.getInventoriesByPerson(personId);
    }

    @GetMapping(path = "/place/{placeId}")
    public List<Inventory> getInventoriesByPlace(@PathVariable("placeId") int placeId) {
        return inventoryService.getInventoriesByPlace(placeId);
    }

    @GetMapping(path = "/weapon/{weaponId}")
    public List<Inventory> getInventoriesByWeapon(@PathVariable("weaponId") int weaponId) {
        return inventoryService.getInventoriesByWeapon(weaponId);
    }

    @PostMapping
    public void saveInventory(@RequestBody Inventory inventory) {
        inventoryService.saveInventory(inventory);
    }

    @DeleteMapping(path = "{inventoryId}")
    public void deleteInventory(@PathVariable("inventoryId") int inventoryId) {
        inventoryService.deleteInventory(inventoryId);
    }

    @PutMapping(path = "{inventoryId}")
    public void updateInventory(@PathVariable("inventoryId") int inventoryId, @RequestBody Inventory inventory) {
        inventoryService.updateInventory(inventoryId, inventory);
    }
}
