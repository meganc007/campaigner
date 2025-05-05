package com.mcommings.campaigner.modules.common.controllers;

import com.mcommings.campaigner.modules.common.dtos.ClimateDTO;
import com.mcommings.campaigner.modules.common.services.interfaces.IClimate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/climates")
public class ClimateController {

    private final IClimate climateService;

    @GetMapping
    public List<ClimateDTO> getClimates() {
        return climateService.getClimates();
    }

    @GetMapping(path = "/{climateId}")
    public ClimateDTO getClimate(@PathVariable("climateId") int climateId) {
        return climateService.getClimate(climateId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @PostMapping
    public void saveClimate(@Valid @RequestBody ClimateDTO climate) {
        climateService.saveClimate(climate);
    }

    @DeleteMapping(path = "{climateId}")
    public void deleteClimate(@PathVariable("climateId") int climateId) {
        climateService.deleteClimate(climateId);
    }

    @PutMapping(path = "{climateId}")
    public void updateClimate(@PathVariable("climateId") int climateId, @RequestBody ClimateDTO climate) {
        climateService.updateClimate(climateId, climate);
    }
}
