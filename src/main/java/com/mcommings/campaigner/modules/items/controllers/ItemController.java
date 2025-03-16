package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.ItemDTO;
import com.mcommings.campaigner.modules.items.services.interfaces.IItem;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/items")
public class ItemController {

    private final IItem itemService;

    @GetMapping
    public List<ItemDTO> Item() {
        return itemService.getItems();
    }

    @GetMapping(path = "/{itemId}")
    public ItemDTO getItem(@PathVariable("itemId") int itemId) {
        return itemService.getItem(itemId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ItemDTO> getItemsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return itemService.getItemsByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveItem(@Valid @RequestBody ItemDTO item) {
        itemService.saveItem(item);
    }

    @DeleteMapping(path = "{itemId}")
    public void deleteItem(@PathVariable("itemId") int itemId) {
        itemService.deleteItem(itemId);
    }

    @PutMapping(path = "{itemId}")
    public void updateItem(@PathVariable("itemId") int itemId, @RequestBody ItemDTO item) {
        itemService.updateItem(itemId, item);
    }
}
