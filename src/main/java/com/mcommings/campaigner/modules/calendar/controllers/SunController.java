package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.dtos.suns.CreateSunDTO;
import com.mcommings.campaigner.modules.calendar.dtos.suns.UpdateSunDTO;
import com.mcommings.campaigner.modules.calendar.dtos.suns.ViewSunDTO;
import com.mcommings.campaigner.modules.calendar.services.SunService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/suns")
public class SunController {

    private final SunService sunService;

    @GetMapping
    public List<ViewSunDTO> getSuns() {

        return sunService.getAll();
    }

    @GetMapping(path = "/{sunId}")
    public ViewSunDTO getSun(@PathVariable int sunId) {
        return sunService.getById(sunId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewSunDTO> getSunsByCampaignUUID(@PathVariable UUID uuid) {
        return sunService.getSunsByCampaignUUID(uuid);
    }

    @PostMapping
    public ViewSunDTO createSun(@Valid @RequestBody CreateSunDTO sun) {
        return sunService.create(sun);
    }

    @PutMapping
    public ViewSunDTO updateSun(@Valid @RequestBody UpdateSunDTO sun) {
        return sunService.update(sun);
    }

    @DeleteMapping(path = "/{sunId}")
    public void deleteSun(@PathVariable int sunId) {

        sunService.delete(sunId);
    }
}
