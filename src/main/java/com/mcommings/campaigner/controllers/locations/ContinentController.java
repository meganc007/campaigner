package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.entities.locations.Continent;
import com.mcommings.campaigner.services.locations.ContinentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/locations/continents")
public class ContinentController {

    private final ContinentService continentService;

    @Autowired
    public ContinentController(ContinentService continentService) {
        this.continentService = continentService;
    }

    @GetMapping
    public List<Continent> getContinents() {
        return continentService.getContinents();
    }

    @GetMapping(path = "/{continentId}")
    public Continent getContinent(@PathVariable("continentId") int continentId) {
        return continentService.getContinent(continentId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Continent> getContinentsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return continentService.getContinentsByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveContinent(@RequestBody Continent continent) {
        continentService.saveContinent(continent);
    }

    @DeleteMapping(path = "{continentId}")
    public void deleteContinent(@PathVariable("continentId") int continentId) {
        continentService.deleteContinent(continentId);
    }

    @PutMapping(path = "{continentId}")
    public void updateContinent(@PathVariable("continentId") int continentId, @RequestBody Continent continent) {
        continentService.updateContinent(continentId, continent);
    }

}
