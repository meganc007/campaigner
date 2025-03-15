package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.entities.ItemType;
import com.mcommings.campaigner.modules.items.services.ItemTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/items/item-types")
public class ItemTypeController {

    private final ItemTypeService itemTypeService;

    @Autowired
    public ItemTypeController(ItemTypeService itemTypeService) {
        this.itemTypeService = itemTypeService;
    }

    @GetMapping
    public List<ItemType> ItemType() {
        return itemTypeService.getItemTypes();
    }

    @PostMapping
    public void saveItemType(@RequestBody ItemType itemType) {
        itemTypeService.saveItemType(itemType);
    }

    @DeleteMapping(path = "{itemTypeId}")
    public void deleteItemType(@PathVariable("itemTypeId") int itemTypeId) {
        itemTypeService.deleteItemType(itemTypeId);
    }

    @PutMapping(path = "{itemTypeId}")
    public void updateItemType(@PathVariable("itemTypeId") int itemTypeId, @RequestBody ItemType itemType) {
        itemTypeService.updateItemType(itemTypeId, itemType);
    }
}
