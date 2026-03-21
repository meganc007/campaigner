package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.cities.CreateCityDTO;
import com.mcommings.campaigner.modules.locations.dtos.cities.UpdateCityDTO;
import com.mcommings.campaigner.modules.locations.dtos.cities.ViewCityDTO;
import com.mcommings.campaigner.modules.locations.services.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/cities")
public class CityController {

    private final CityService cityService;

    @GetMapping
    public List<ViewCityDTO> getCities() {

        return cityService.getAll();
    }

    @GetMapping(path = "/{cityId}")
    public ViewCityDTO getCity(@PathVariable int cityId) {
        return cityService.getById(cityId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewCityDTO> getCitiesByCampaignUUID(@PathVariable UUID uuid) {
        return cityService.getCitiesByCampaignUUID(uuid);
    }

    @GetMapping(path = "/wealth/{wealthId}")
    public List<ViewCityDTO> getCitiesByWealthId(@PathVariable int wealthId) {
        return cityService.getCitiesByWealthId(wealthId);
    }

    @GetMapping(path = "/country/{countryId}")
    public List<ViewCityDTO> getCitiesByCountryId(@PathVariable int countryId) {
        return cityService.getCitiesByCountryId(countryId);
    }

    @GetMapping(path = "/settlementtypes/{settlementTypeId}")
    public List<ViewCityDTO> getCitiesBySettlementTypeId(
            @PathVariable int settlementTypeId) {

        return cityService.getCitiesBySettlementTypeId(settlementTypeId);
    }

    @GetMapping(path = "/government/{governmentId}")
    public List<ViewCityDTO> getCitiesByGovernmentId(
            @PathVariable int governmentId) {

        return cityService.getCitiesByGovernmentId(governmentId);
    }

    @GetMapping(path = "/region/{regionId}")
    public List<ViewCityDTO> getCitiesByRegionId(@PathVariable int regionId) {
        return cityService.getCitiesByRegionId(regionId);
    }

    @PostMapping
    public ViewCityDTO createCity(@Valid @RequestBody CreateCityDTO city) {

        return cityService.create(city);
    }

    @PutMapping
    public ViewCityDTO updateCity(@Valid @RequestBody UpdateCityDTO city) {
        return cityService.update(city);
    }

    @DeleteMapping(path = "/{cityId}")
    public void deleteCity(@PathVariable int cityId) {

        cityService.delete(cityId);
    }

}
