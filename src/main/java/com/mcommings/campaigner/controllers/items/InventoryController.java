package com.mcommings.campaigner.controllers.items;

import com.mcommings.campaigner.models.items.Inventory;
import com.mcommings.campaigner.services.items.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
