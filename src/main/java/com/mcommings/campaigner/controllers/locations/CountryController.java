package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.models.locations.Country;
import com.mcommings.campaigner.services.locations.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/locations/countries")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<Country> getCountries() {
        return countryService.getCountries();
    }

    @GetMapping(path = "/{countryId}")
    public Country getCountry(@PathVariable("countryId") int countryId) {
        return countryService.getCountry(countryId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Country> getCountriesByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return countryService.getCountriesByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveCountry(@RequestBody Country country) {
        countryService.saveCountry(country);
    }

    @DeleteMapping(path = "{countryId}")
    public void deleteCountry(@PathVariable("countryId") int countryId) {
        countryService.deleteCountry(countryId);
    }

    @PutMapping(path = "{countryId}")
    public void updateCountry(@PathVariable("countryId") int countryId, @RequestBody Country country) {
        countryService.updateCountry(countryId, country);
    }
}
