package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.dtos.SunDTO;
import com.mcommings.campaigner.modules.calendar.services.interfaces.ISun;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/calendar/suns")
public class SunController {

    private final ISun sunService;

    @GetMapping
    public List<SunDTO> getSuns() {
        return sunService.getSuns();
    }

    @GetMapping(path = "/{sunId}")
    public SunDTO getSun(@PathVariable("sunId") int sunId) {
        return sunService.getSun(sunId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<SunDTO> getSunsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return sunService.getSunsByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveSun(@Valid @RequestBody SunDTO sun) {
        sunService.saveSun(sun);
    }

    @DeleteMapping(path = "{sunId}")
    public void deleteSun(@PathVariable("sunId") int sunId) {
        sunService.deleteSun(sunId);
    }

    @PutMapping(path = "{sunId}")
    public void updateSun(@PathVariable("sunId") int sunId, @RequestBody SunDTO sun) {
        sunService.updateSun(sunId, sun);
    }
}
