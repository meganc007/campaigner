package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.models.locations.PlaceType;
import com.mcommings.campaigner.services.locations.PlaceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/locations/place-types")
public class PlaceTypeController {

    private final PlaceTypeService placeTypeService;

    @Autowired
    public PlaceTypeController(PlaceTypeService placeTypeService) {this.placeTypeService = placeTypeService;}

    @GetMapping
    public List<PlaceType> getPlaceTypes() {
        return placeTypeService.getPlaceTypes();
    }

    @PostMapping
    public void savePlaceType(@RequestBody PlaceType placeType) {
        placeTypeService.savePlaceType(placeType);
    }

    @DeleteMapping(path = "{placeTypeId}")
    public void deletePlacetype(@PathVariable("placeTypeId") int placeTypeId) {
        placeTypeService.deletePlaceType(placeTypeId);
    }

    @PutMapping(path = "{placeTypeId}")
    public void updatePlacetype(@PathVariable("placeTypeId") int placeTypeId, @RequestBody PlaceType placeType) {
        placeTypeService.updatePlaceType(placeTypeId, placeType);
    }
}
