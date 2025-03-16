package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.InventoryDTO;
import com.mcommings.campaigner.modules.items.services.interfaces.IInventory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/inventory")
public class InventoryController {

    private final IInventory inventoryService;

    @GetMapping
    public List<InventoryDTO> Inventory() {
        return inventoryService.getInventories();
    }

    @GetMapping(path = "/{inventoryId}")
    public InventoryDTO getInventory(@PathVariable("inventoryId") int inventoryId) {
        return inventoryService.getInventory(inventoryId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<InventoryDTO> getInventoriesByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return inventoryService.getInventoriesByCampaignUUID(uuid);
    }

    @GetMapping(path = "/item/{itemId}")
    public List<InventoryDTO> getInventoriesByItem(@PathVariable("itemId") int itemId) {
        return inventoryService.getInventoriesByItem(itemId);
    }

    @GetMapping(path = "/person/{personId}")
    public List<InventoryDTO> getInventoriesByPerson(@PathVariable("personId") int personId) {
        return inventoryService.getInventoriesByPerson(personId);
    }

    @GetMapping(path = "/place/{placeId}")
    public List<InventoryDTO> getInventoriesByPlace(@PathVariable("placeId") int placeId) {
        return inventoryService.getInventoriesByPlace(placeId);
    }

    @GetMapping(path = "/weapon/{weaponId}")
    public List<InventoryDTO> getInventoriesByWeapon(@PathVariable("weaponId") int weaponId) {
        return inventoryService.getInventoriesByWeapon(weaponId);
    }

    @PostMapping
    public void saveInventory(@Valid @RequestBody InventoryDTO inventory) {
        inventoryService.saveInventory(inventory);
    }

    @DeleteMapping(path = "{inventoryId}")
    public void deleteInventory(@PathVariable("inventoryId") int inventoryId) {
        inventoryService.deleteInventory(inventoryId);
    }

    @PutMapping(path = "{inventoryId}")
    public void updateInventory(@PathVariable("inventoryId") int inventoryId, @RequestBody InventoryDTO inventory) {
        inventoryService.updateInventory(inventoryId, inventory);
    }
}
