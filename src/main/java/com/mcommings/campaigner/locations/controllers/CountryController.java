package com.mcommings.campaigner.locations.controllers;

import com.mcommings.campaigner.locations.dtos.CountryDTO;
import com.mcommings.campaigner.locations.services.interfaces.ICountry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/countries")
public class CountryController {

    private final ICountry countryService;

    @GetMapping
    public List<CountryDTO> getCountries() {
        return countryService.getCountries();
    }

    @GetMapping(path = "/{countryId}")
    public CountryDTO getCountry(@PathVariable("countryId") int countryId) {
        return countryService.getCountry(countryId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<CountryDTO> getCountriesByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return countryService.getCountriesByCampaignUUID(uuid);
    }

    @GetMapping(path = "/continent/{continentId}")
    public List<CountryDTO> getCountriesByContinentId(@PathVariable("continentId") int continentId) {
        return countryService.getCountriesByContinentId(continentId);
    }

    @PostMapping
    public void saveCountry(@Valid @RequestBody CountryDTO country) {
        countryService.saveCountry(country);
    }

    @DeleteMapping(path = "{countryId}")
    public void deleteCountry(@PathVariable("countryId") int countryId) {
        countryService.deleteCountry(countryId);
    }

    @PutMapping(path = "{countryId}")
    public void updateCountry(@PathVariable("countryId") int countryId, @RequestBody CountryDTO country) {
        countryService.updateCountry(countryId, country);
    }
}
