package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.dtos.MoonDTO;
import com.mcommings.campaigner.modules.calendar.services.interfaces.IMoon;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/moons")
public class MoonController {

    private final IMoon moonService;

    @GetMapping
    public List<MoonDTO> getMoons() {
        return moonService.getMoons();
    }

    @GetMapping(path = "/{moonId}")
    public MoonDTO getMoon(@PathVariable("moonId") int moonId) {
        return moonService.getMoon(moonId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<MoonDTO> getMoonsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return moonService.getMoonsByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveMoon(@Valid @RequestBody MoonDTO moon) {
        moonService.saveMoon(moon);
    }

    @DeleteMapping(path = "{moonId}")
    public void deleteMoon(@PathVariable("moonId") int moonId) {
        moonService.deleteMoon(moonId);
    }

    @PutMapping(path = "{moonId}")
    public void updateMoon(@PathVariable("moonId") int moonId, @RequestBody MoonDTO moon) {
        moonService.updateMoon(moonId, moon);
    }
}
