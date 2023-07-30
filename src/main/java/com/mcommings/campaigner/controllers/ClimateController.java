package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.Climate;
import com.mcommings.campaigner.services.ClimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
