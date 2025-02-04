package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.entities.Climate;
import com.mcommings.campaigner.services.ClimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/climates")
public class ClimateController {

    private final ClimateService climateService;

    @Autowired
    public ClimateController (ClimateService climateService) {this.climateService = climateService;}

    @GetMapping
    public List<Climate> getClimates() {
        return climateService.getClimates();
    }

    @PostMapping
    public void saveClimate(@RequestBody Climate climate) {
        climateService.saveClimate(climate);
    }

    @DeleteMapping(path = "{climateId}")
    public void deleteClimate(@PathVariable("climateId") int climateId) {
        climateService.deleteClimate(climateId);
    }

    @PutMapping(path = "{climateId}")
    public void updateClimate(@PathVariable("climateId") int climateId, @RequestBody Climate climate) {
        climateService.updateClimate(climateId, climate);
    }
}
