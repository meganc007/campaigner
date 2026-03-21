package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.countries.CreateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.UpdateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.ViewCountryDTO;
import com.mcommings.campaigner.modules.locations.services.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/countries")
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public List<ViewCountryDTO> getCountries() {

        return countryService.getAll();
    }

    @GetMapping(path = "/{countryId}")
    public ViewCountryDTO getCountry(@PathVariable int countryId) {
        return countryService.getById(countryId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewCountryDTO> getCountriesByCampaignUUID(@PathVariable UUID uuid) {
        return countryService.getCountriesByCampaignUUID(uuid);
    }

    @GetMapping(path = "/continent/{continentId}")
    public List<ViewCountryDTO> getCountriesByContinentId(
            @PathVariable int continentId) {

        return countryService.getCountriesByContinentId(continentId);
    }

    @GetMapping(path = "/government/{governmentId}")
    public List<ViewCountryDTO> getCountriesByGovernmentId(
            @PathVariable int governmentId) {

        return countryService.getCountriesByGovernmentId(governmentId);
    }

    @PostMapping
    public ViewCountryDTO createCountry(@Valid @RequestBody CreateCountryDTO country) {
        return countryService.create(country);
    }

    @PutMapping
    public ViewCountryDTO updateCountry(@Valid @RequestBody UpdateCountryDTO country) {
        return countryService.update(country);
    }

    @DeleteMapping(path = "/{countryId}")
    public void deleteCountry(@PathVariable int countryId) {

        countryService.delete(countryId);
    }

}
