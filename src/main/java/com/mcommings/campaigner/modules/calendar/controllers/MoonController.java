package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.dtos.moons.CreateMoonDTO;
import com.mcommings.campaigner.modules.calendar.dtos.moons.UpdateMoonDTO;
import com.mcommings.campaigner.modules.calendar.dtos.moons.ViewMoonDTO;
import com.mcommings.campaigner.modules.calendar.services.MoonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/moons")
public class MoonController {

    private final MoonService moonService;

    @GetMapping
    public List<ViewMoonDTO> getMoons() {

        return moonService.getAll();
    }

    @GetMapping(path = "/{moonId}")
    public ViewMoonDTO getMoon(@PathVariable int moonId) {
        return moonService.getById(moonId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewMoonDTO> getMoonsByCampaignUUID(@PathVariable UUID uuid) {
        return moonService.getMoonsByCampaignUUID(uuid);
    }

    @PostMapping
    public ViewMoonDTO createMoon(@Valid @RequestBody CreateMoonDTO moon) {
        return moonService.create(moon);
    }

    @PutMapping
    public ViewMoonDTO updateMoon(@Valid @RequestBody UpdateMoonDTO moon) {
        return moonService.update(moon);
    }

    @DeleteMapping(path = "/{moonId}")
    public void deleteMoon(@PathVariable int moonId) {

        moonService.delete(moonId);
    }
}
