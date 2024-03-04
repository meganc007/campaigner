package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.models.locations.City;
import com.mcommings.campaigner.services.locations.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/locations/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public List<City> getCities() {
        return cityService.getCities();
    }

    @GetMapping(path = "/{cityId}")
    public City getCity(@PathVariable("cityId") int cityId) {
        return cityService.getCity(cityId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<City> getCitiesByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return cityService.getCitiesByCampaignUUID(uuid);
    }

    @GetMapping(path = "/country/{countryId}")
    public List<City> getCitiesByCountryId(@PathVariable("countryId") int countryId) {
        return cityService.getCitiesByCountryId(countryId);
    }

    @GetMapping(path = "/region/{regionId}")
    public List<City> getCitiesByRegionId(@PathVariable("regionId") int regionId) {
        return cityService.getCitiesByRegionId(regionId);
    }

    @PostMapping
    public void saveCity(@RequestBody City city) {
        cityService.saveCity(city);
    }

    @DeleteMapping(path = "{cityId}")
    public void deleteCity(@PathVariable("cityId") int cityId) {
        cityService.deleteCity(cityId);
    }

    @PutMapping(path = "{cityId}")
    public void updateCity(@PathVariable("cityId") int cityId, @RequestBody City city) {
        cityService.updateCity(cityId, city);
    }

}
