package com.mcommings.campaigner.locations.controllers;

import com.mcommings.campaigner.locations.dtos.RegionDTO;
import com.mcommings.campaigner.locations.services.interfaces.IRegion;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/regions")
public class RegionController {

    private final IRegion regionService;

    @GetMapping
    public List<RegionDTO> getRegions() {
        return regionService.getRegions();
    }

    @GetMapping(path = "/{regionId}")
    public RegionDTO getRegion(@PathVariable("regionId") int regionId) {
        return regionService.getRegion(regionId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<RegionDTO> getRegionsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return regionService.getRegionsByCampaignUUID(uuid);
    }

    @GetMapping(path = "/country/{countryId}")
    public List<RegionDTO> getRegionsByCountryId(@PathVariable("countryId") int countryId) {
        return regionService.getRegionsByCountryId(countryId);
    }

    @PostMapping
    public void saveRegion(@Valid @RequestBody RegionDTO region) {
        regionService.saveRegion(region);
    }

    @DeleteMapping(path = "{regionId}")
    public void deleteRegion(@PathVariable("regionId") int regionId) {
        regionService.deleteRegion(regionId);
    }

    @PutMapping(path = "{regionId}")
    public void updateRegion(@PathVariable("regionId") int regionId, @RequestBody RegionDTO region) {
        regionService.updateRegion(regionId, region);
    }
}
