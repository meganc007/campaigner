package com.mcommings.campaigner.modules.common.controllers;

import com.mcommings.campaigner.modules.common.dtos.climate.CreateClimateDTO;
import com.mcommings.campaigner.modules.common.dtos.climate.UpdateClimateDTO;
import com.mcommings.campaigner.modules.common.dtos.climate.ViewClimateDTO;
import com.mcommings.campaigner.modules.common.services.ClimateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/climates")
public class ClimateController {

    private final ClimateService climateService;

    @GetMapping
    public List<ViewClimateDTO> getClimates() {
        return climateService.getAll();
    }

    @GetMapping(path = "/{climateId}")
    public ViewClimateDTO getClimate(@PathVariable int climateId) {
        return climateService.getById(climateId);
    }

    @PostMapping
    public ViewClimateDTO createClimate(@Valid @RequestBody CreateClimateDTO climate) {
        return climateService.create(climate);
    }

    @PutMapping
    public void updateClimate(@Valid @RequestBody UpdateClimateDTO climate) {
        climateService.update(climate);
    }

    @DeleteMapping(path = "/{climateId}")
    public void deleteClimate(@PathVariable int climateId) {
        climateService.delete(climateId);
    }

}
