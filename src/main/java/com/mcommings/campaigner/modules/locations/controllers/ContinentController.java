package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.continents.CreateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.UpdateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.ViewContinentDTO;
import com.mcommings.campaigner.modules.locations.services.ContinentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/continents")
public class ContinentController {

    private final ContinentService continentService;

    @GetMapping
    public List<ViewContinentDTO> getContinents() {
        return continentService.getAll();
    }

    @GetMapping("/{continentId}")
    public ViewContinentDTO getContinent(@PathVariable int continentId) {
        return continentService.getById(continentId);
    }

    @GetMapping("/campaign/{uuid}")
    public List<ViewContinentDTO> getContinentsByCampaignUUID(@PathVariable UUID uuid) {
        return continentService.getContinentsByCampaignUUID(uuid);
    }

    @PostMapping
    public ViewContinentDTO saveContinent(@Valid @RequestBody CreateContinentDTO continent) {
        return continentService.create(continent);
    }

    @PutMapping
    public ViewContinentDTO updateContinent(@Valid @RequestBody UpdateContinentDTO continent) {
        return continentService.update(continent);
    }

    @DeleteMapping("/{continentId}")
    public void deleteContinent(@PathVariable int continentId) {
        continentService.delete(continentId);
    }

}