package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.place_types.CreatePlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.place_types.UpdatePlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.place_types.ViewPlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.services.PlaceTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/placetypes")
public class PlaceTypeController {

    private final PlaceTypeService placeTypeService;

    @GetMapping
    public List<ViewPlaceTypeDTO> getPlaceTypes() {

        return placeTypeService.getAll();
    }

    @GetMapping(path = "/{placeTypeId}")
    public ViewPlaceTypeDTO getPlaceType(@PathVariable int placeTypeId) {
        return placeTypeService.getById(placeTypeId);
    }

    @PostMapping
    public ViewPlaceTypeDTO createPlaceType(@Valid @RequestBody CreatePlaceTypeDTO placeType) {
        return placeTypeService.create(placeType);
    }

    @PutMapping
    public ViewPlaceTypeDTO updatePlaceType(@Valid @RequestBody UpdatePlaceTypeDTO placeType) {
        return placeTypeService.update(placeType);
    }

    @DeleteMapping(path = "/{placeTypeId}")
    public void deletePlaceType(@PathVariable int placeTypeId) {
        placeTypeService.delete(placeTypeId);
    }

}
