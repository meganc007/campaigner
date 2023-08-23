package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.City;
import com.mcommings.campaigner.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
