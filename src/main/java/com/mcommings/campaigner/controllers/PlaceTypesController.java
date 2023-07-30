package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.PlaceTypes;
import com.mcommings.campaigner.services.PlaceTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/locations/place-types")
public class PlaceTypesController {

    private final PlaceTypesService placeTypesService;

    @Autowired
    public PlaceTypesController(PlaceTypesService placeTypesService) {this.placeTypesService = placeTypesService;}

    @GetMapping
    public List<PlaceTypes> getPlaceTypes() {
        return placeTypesService.getPlaceTypes();
    }
}
