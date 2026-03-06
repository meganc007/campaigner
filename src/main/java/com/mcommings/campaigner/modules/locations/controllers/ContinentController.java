package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.continents.CreateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.UpdateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.ViewContinentDTO;
import com.mcommings.campaigner.modules.locations.services.interfaces.IContinent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/continents")
public class ContinentController {

    private final IContinent continentService;

    @GetMapping
    public List<ViewContinentDTO> getContinents() {
        return continentService.getContinents();
    }

    @GetMapping(path = "/{continentId}")
    public ViewContinentDTO getContinent(@PathVariable("continentId") int continentId) {
        return continentService.getContinent(continentId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewContinentDTO> getContinentsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return continentService.getContinentsByCampaignUUID(uuid);
    }

    @PostMapping
    public ViewContinentDTO saveContinent(@Valid @RequestBody CreateContinentDTO continent) {
        return continentService.saveContinent(continent);
    }

    @PutMapping(path = "{continentId}")
    public Optional<ViewContinentDTO> updateContinent(@RequestBody UpdateContinentDTO continent) {
        return continentService.updateContinent(continent);
    }

    @DeleteMapping(path = "{continentId}")
    public void deleteContinent(@PathVariable("continentId") int continentId) {
        continentService.deleteContinent(continentId);
    }

}