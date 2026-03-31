package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.items.CreateItemDTO;
import com.mcommings.campaigner.modules.items.dtos.items.UpdateItemDTO;
import com.mcommings.campaigner.modules.items.dtos.items.ViewItemDTO;
import com.mcommings.campaigner.modules.items.services.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ViewItemDTO> getItems() {

        return itemService.getAll();
    }

    @GetMapping(path = "/{itemId}")
    public ViewItemDTO getItem(@PathVariable int itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewItemDTO> getItemsByCampaignUUID(@PathVariable UUID uuid) {
        return itemService.getItemsByCampaignUUID(uuid);
    }

    @GetMapping(path = "/itemType/{itemTypeId}")
    public List<ViewItemDTO> getItemsByItemTypeId(@PathVariable int itemTypeId) {
        return itemService.getItemsByItemTypeId(itemTypeId);
    }

    @PostMapping
    public ViewItemDTO createItem(@Valid @RequestBody CreateItemDTO item) {

        return itemService.create(item);
    }

    @PutMapping
    public ViewItemDTO updateItem(@Valid @RequestBody UpdateItemDTO item) {
        return itemService.update(item);
    }

    @DeleteMapping(path = "/{itemId}")
    public void deleteItem(@PathVariable int itemId) {

        itemService.delete(itemId);
    }
}
