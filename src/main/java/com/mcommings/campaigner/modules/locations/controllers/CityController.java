package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.CityDTO;
import com.mcommings.campaigner.modules.locations.services.interfaces.ICity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/cities")
public class CityController {

    private final ICity cityService;

    @GetMapping
    public List<CityDTO> getCities() {
        return cityService.getCities();
    }

    @GetMapping(path = "/{cityId}")
    public CityDTO getCity(@PathVariable("cityId") int cityId) {
        return cityService.getCity(cityId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<CityDTO> getCitiesByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return cityService.getCitiesByCampaignUUID(uuid);
    }

    @GetMapping(path = "/country/{countryId}")
    public List<CityDTO> getCitiesByCountryId(@PathVariable("countryId") int countryId) {
        return cityService.getCitiesByCountryId(countryId);
    }

    @GetMapping(path = "/region/{regionId}")
    public List<CityDTO> getCitiesByRegionId(@PathVariable("regionId") int regionId) {
        return cityService.getCitiesByRegionId(regionId);
    }

    @PostMapping
    public void saveCity(@Valid @RequestBody CityDTO city) {
        cityService.saveCity(city);
    }

    @DeleteMapping(path = "{cityId}")
    public void deleteCity(@PathVariable("cityId") int cityId) {
        cityService.deleteCity(cityId);
    }

    @PutMapping(path = "{cityId}")
    public void updateCity(@PathVariable("cityId") int cityId, @RequestBody CityDTO city) {
        cityService.updateCity(cityId, city);
    }
}
