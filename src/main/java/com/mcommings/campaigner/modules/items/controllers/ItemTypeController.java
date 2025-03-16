package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.ItemTypeDTO;
import com.mcommings.campaigner.modules.items.services.interfaces.IItemType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/itemtypes")
public class ItemTypeController {

    private final IItemType itemTypeService;

    @GetMapping
    public List<ItemTypeDTO> ItemType() {
        return itemTypeService.getItemTypes();
    }

    @GetMapping(path = "/{itemTypeId}")
    public ItemTypeDTO getItemType(@PathVariable("itemTypeId") int itemTypeId) {
        return itemTypeService.getItemType(itemTypeId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @PostMapping
    public void saveItemType(@Valid @RequestBody ItemTypeDTO itemType) {
        itemTypeService.saveItemType(itemType);
    }

    @DeleteMapping(path = "{itemTypeId}")
    public void deleteItemType(@PathVariable("itemTypeId") int itemTypeId) {
        itemTypeService.deleteItemType(itemTypeId);
    }

    @PutMapping(path = "{itemTypeId}")
    public void updateItemType(@PathVariable("itemTypeId") int itemTypeId, @RequestBody ItemTypeDTO itemType) {
        itemTypeService.updateItemType(itemTypeId, itemType);
    }
}
