package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.ContinentDTO;
import com.mcommings.campaigner.modules.locations.services.interfaces.IContinent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/continents")
public class ContinentController {

    private final IContinent continentService;

    @GetMapping
    public List<ContinentDTO> getContinents() {
        return continentService.getContinents();
    }

    @GetMapping(path = "/{continentId}")
    public ContinentDTO getContinent(@PathVariable("continentId") int continentId) {
        return continentService.getContinent(continentId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ContinentDTO> getContinentsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return continentService.getContinentsByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveContinent(@Valid @RequestBody ContinentDTO continent) {
        continentService.saveContinent(continent);
    }

    @DeleteMapping(path = "{continentId}")
    public void deleteContinent(@PathVariable("continentId") int continentId) {
        continentService.deleteContinent(continentId);
    }

    @PutMapping(path = "{continentId}")
    public void updateContinent(@PathVariable("continentId") int continentId, @RequestBody ContinentDTO continent) {
        continentService.updateContinent(continentId, continent);
    }

}