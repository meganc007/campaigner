package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.models.locations.Continent;
import com.mcommings.campaigner.services.locations.ContinentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
