package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.item_types.CreateItemTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.item_types.UpdateItemTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.item_types.ViewItemTypeDTO;
import com.mcommings.campaigner.modules.items.services.ItemTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/itemtypes")
public class ItemTypeController {

    private final ItemTypeService itemTypeService;

    @GetMapping
    public List<ViewItemTypeDTO> getItemTypes() {

        return itemTypeService.getAll();
    }

    @GetMapping(path = "/{itemTypeId}")
    public ViewItemTypeDTO getItemType(@PathVariable int itemTypeId) {
        return itemTypeService.getById(itemTypeId);
    }

    @PostMapping
    public ViewItemTypeDTO createItemType(@Valid @RequestBody CreateItemTypeDTO itemType) {

        return itemTypeService.create(itemType);
    }

    @PutMapping
    public ViewItemTypeDTO updateItemType(@Valid @RequestBody UpdateItemTypeDTO itemType) {
        return itemTypeService.update(itemType);
    }

    @DeleteMapping(path = "/{itemTypeId}")
    public void deleteItemType(@PathVariable int itemTypeId) {

        itemTypeService.delete(itemTypeId);
    }
}
