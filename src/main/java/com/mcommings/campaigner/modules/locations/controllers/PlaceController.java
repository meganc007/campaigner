package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.places.CreatePlaceDTO;
import com.mcommings.campaigner.modules.locations.dtos.places.UpdatePlaceDTO;
import com.mcommings.campaigner.modules.locations.dtos.places.ViewPlaceDTO;
import com.mcommings.campaigner.modules.locations.services.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/places")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public List<ViewPlaceDTO> getPlaces() {

        return placeService.getAll();
    }

    @GetMapping(path = "/{placeId}")
    public ViewPlaceDTO getPlace(@PathVariable int placeId) {
        return placeService.getById(placeId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewPlaceDTO> getPlacesByCampaignUUID(@PathVariable UUID uuid) {
        return placeService.getPlacesByCampaignUUID(uuid);
    }

    @GetMapping(path = "/placetype/{placeTypeId}")
    public List<ViewPlaceDTO> getPlacesByPlaceTypeId(@PathVariable int placeTypeId) {
        return placeService.getPlacesByPlaceTypeId(placeTypeId);
    }

    @GetMapping(path = "/terrain/{terrainId}")
    public List<ViewPlaceDTO> getPlacesByTerrainId(@PathVariable int terrainId) {
        return placeService.getPlacesByTerrainId(terrainId);
    }

    @GetMapping(path = "/country/{countryId}")
    public List<ViewPlaceDTO> getPlacesByCountryId(@PathVariable int countryId) {
        return placeService.getPlacesByCountryId(countryId);
    }

    @GetMapping(path = "/city/{cityId}")
    public List<ViewPlaceDTO> getPlacesByCityId(@PathVariable int cityId) {
        return placeService.getPlacesByCityId(cityId);
    }

    @GetMapping(path = "/region/{regionId}")
    public List<ViewPlaceDTO> getPlacesByRegionId(@PathVariable int regionId) {
        return placeService.getPlacesByRegionId(regionId);
    }

    @PostMapping
    public ViewPlaceDTO savePlace(@Valid @RequestBody CreatePlaceDTO place) {

        return placeService.create(place);
    }

    @PutMapping
    public ViewPlaceDTO updatePlace(@Valid @RequestBody UpdatePlaceDTO place) {
        return placeService.update(place);
    }

    @DeleteMapping(path = "/{placeId}")
    public void deletePlace(@PathVariable int placeId) {

        placeService.delete(placeId);
    }
}
