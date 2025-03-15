package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.PlaceDTO;
import com.mcommings.campaigner.modules.locations.services.interfaces.IPlace;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/places")
public class PlaceController {

    private final IPlace placeService;

    @GetMapping
    public List<PlaceDTO> getPlaces() {
        return placeService.getPlaces();
    }

    @GetMapping(path = "/{placeId}")
    public PlaceDTO getPlace(@PathVariable("placeId") int placeId) {
        return placeService.getPlace(placeId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<PlaceDTO> getPlacesByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return placeService.getPlacesByCampaignUUID(uuid);
    }

    @GetMapping(path = "/country/{countryId}")
    public List<PlaceDTO> getPlacesByCountryId(@PathVariable("countryId") int countryId) {
        return placeService.getPlacesByCountryId(countryId);
    }

    @GetMapping(path = "/city/{cityId}")
    public List<PlaceDTO> getPlacesByCityId(@PathVariable("cityId") int cityId) {
        return placeService.getPlacesByCityId(cityId);
    }

    @GetMapping(path = "/region/{regionId}")
    public List<PlaceDTO> getPlacesByRegionId(@PathVariable("regionId") int regionId) {
        return placeService.getPlacesByRegionId(regionId);
    }

    @PostMapping
    public void savePlace(@Valid @RequestBody PlaceDTO place) {
        placeService.savePlace(place);
    }

    @DeleteMapping(path = "{placeId}")
    public void deletePlace(@PathVariable("placeId") int placeId) {
        placeService.deletePlace(placeId);
    }

    @PutMapping(path = "{placeId}")
    public void updatePlace(@PathVariable("placeId") int placeId, @RequestBody PlaceDTO place) {
        placeService.updatePlace(placeId, place);
    }
}
