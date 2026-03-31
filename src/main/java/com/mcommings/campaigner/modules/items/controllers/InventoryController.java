package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.inventories.CreateInventoryDTO;
import com.mcommings.campaigner.modules.items.dtos.inventories.UpdateInventoryDTO;
import com.mcommings.campaigner.modules.items.dtos.inventories.ViewInventoryDTO;
import com.mcommings.campaigner.modules.items.services.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<ViewInventoryDTO> getInventories() {

        return inventoryService.getAll();
    }

    @GetMapping(path = "/{inventoryId}")
    public ViewInventoryDTO getInventory(@PathVariable int inventoryId) {
        return inventoryService.getById(inventoryId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewInventoryDTO> getInventoriesByCampaignUUID(@PathVariable UUID uuid) {
        return inventoryService.getInventoriesByCampaignUUID(uuid);
    }

    @GetMapping(path = "/person/{personId}")
    public List<ViewInventoryDTO> getInventoriesByPersonId(@PathVariable int personId) {
        return inventoryService.getInventoriesByPersonId(personId);
    }

    @GetMapping(path = "/item/{itemId}")
    public List<ViewInventoryDTO> getInventoriesByItemId(@PathVariable int itemId) {
        return inventoryService.getInventoriesByItemId(itemId);
    }

    @GetMapping(path = "/weapon/{weaponId}")
    public List<ViewInventoryDTO> getInventoriesByWeaponId(@PathVariable int weaponId) {
        return inventoryService.getInventoriesByWeaponId(weaponId);
    }

    @GetMapping(path = "/place/{placeId}")
    public List<ViewInventoryDTO> getInventoriesByPlaceId(@PathVariable int placeId) {
        return inventoryService.getInventoriesByPlaceId(placeId);
    }

    @PostMapping
    public ViewInventoryDTO createInventory(@Valid @RequestBody CreateInventoryDTO inventory) {

        return inventoryService.create(inventory);
    }

    @PutMapping
    public ViewInventoryDTO updateInventory(@Valid @RequestBody UpdateInventoryDTO inventory) {
        return inventoryService.update(inventory);
    }

    @DeleteMapping(path = "/{inventoryId}")
    public void deleteInventory(@PathVariable int inventoryId) {

        inventoryService.delete(inventoryId);
    }
}
