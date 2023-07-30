package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.PlaceType;
import com.mcommings.campaigner.services.PlaceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
