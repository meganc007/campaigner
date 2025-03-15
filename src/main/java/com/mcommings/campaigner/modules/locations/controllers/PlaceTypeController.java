package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.PlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.services.interfaces.IPlaceType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/placetypes")
public class PlaceTypeController {

    private final IPlaceType placeTypeService;

    @GetMapping
    public List<PlaceTypeDTO> getPlaceTypes() {
        return placeTypeService.getPlaceTypes();
    }

    @GetMapping(path = "/{placeTypeId}")
    public PlaceTypeDTO getPlaceType(@PathVariable("placeTypeId") int placeTypeId) {
        return placeTypeService.getPlaceType(placeTypeId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @PostMapping
    public void savePlaceType(@Valid @RequestBody PlaceTypeDTO placeType) {
        placeTypeService.savePlaceType(placeType);
    }

    @DeleteMapping(path = "{placeTypeId}")
    public void deletePlaceType(@PathVariable("placeTypeId") int placeTypeId) {
        placeTypeService.deletePlaceType(placeTypeId);
    }

    @PutMapping(path = "{placeTypeId}")
    public void updatePlaceType(@PathVariable("placeTypeId") int placeTypeId, @RequestBody PlaceTypeDTO placeType) {
        placeTypeService.updatePlaceType(placeTypeId, placeType);
    }
}
