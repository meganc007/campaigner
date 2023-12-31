package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.models.locations.Place;
import com.mcommings.campaigner.services.locations.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/locations/places")
public class PlaceController {

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public List<Place> getPlaces() {
        return placeService.getPlaces();
    }

    @PostMapping
    public void savePlace(@RequestBody Place place) {
        placeService.savePlace(place);
    }

    @DeleteMapping(path = "{placeId}")
    public void deletePlace(@PathVariable("placeId") int placeId) {
        placeService.deletePlace(placeId);
    }

    @PutMapping(path = "{placeId}")
    public void updatePlace(@PathVariable("placeId") int placeId, @RequestBody Place place) {
        placeService.updatePlace(placeId, place);
    }
}
