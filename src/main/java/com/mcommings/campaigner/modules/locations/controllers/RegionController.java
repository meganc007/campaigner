package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.regions.CreateRegionDTO;
import com.mcommings.campaigner.modules.locations.dtos.regions.UpdateRegionDTO;
import com.mcommings.campaigner.modules.locations.dtos.regions.ViewRegionDTO;
import com.mcommings.campaigner.modules.locations.services.RegionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/regions")
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public List<ViewRegionDTO> getRegions() {

        return regionService.getAll();
    }

    @GetMapping(path = "/{regionId}")
    public ViewRegionDTO getRegion(@PathVariable int regionId) {
        return regionService.getById(regionId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewRegionDTO> getRegionsByCampaignUUID(@PathVariable UUID uuid) {
        return regionService.getRegionsByCampaignUUID(uuid);
    }

    @GetMapping(path = "/climate/{climateId}")
    public List<ViewRegionDTO> getRegionsByClimateId(@PathVariable int climateId) {
        return regionService.getRegionsByClimateId(climateId);
    }

    @GetMapping(path = "/country/{countryId}")
    public List<ViewRegionDTO> getRegionsByCountryId(@PathVariable int countryId) {
        return regionService.getRegionsByCountryId(countryId);
    }

    @PostMapping
    public ViewRegionDTO createRegion(@Valid @RequestBody CreateRegionDTO region) {

        return regionService.create(region);
    }

    @PutMapping
    public ViewRegionDTO updateRegion(@Valid @RequestBody UpdateRegionDTO region) {
        return regionService.update(region);
    }

    @DeleteMapping(path = "/{regionId}")
    public void deleteRegion(@PathVariable int regionId) {

        regionService.delete(regionId);
    }
}
