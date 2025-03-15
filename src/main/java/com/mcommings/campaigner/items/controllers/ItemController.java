package com.mcommings.campaigner.items.controllers;

import com.mcommings.campaigner.items.entities.Item;
import com.mcommings.campaigner.items.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/items/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<Item> Item() {
        return itemService.getItems();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Item> getItemsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return itemService.getItemsByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveItem(@RequestBody Item item) {
        itemService.saveItem(item);
    }

    @DeleteMapping(path = "{itemId}")
    public void deleteItem(@PathVariable("itemId") int itemId) {
        itemService.deleteItem(itemId);
    }

    @PutMapping(path = "{itemId}")
    public void updateItem(@PathVariable("itemId") int itemId, @RequestBody Item item) {
        itemService.updateItem(itemId, item);
    }
}
