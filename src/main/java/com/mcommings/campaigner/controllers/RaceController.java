package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.Race;
import com.mcommings.campaigner.services.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/people/races")
public class RaceController {

    private final RaceService raceService;

    @Autowired
    public RaceController(RaceService raceService) {this.raceService = raceService;}

    @GetMapping
    public List<Race> getRaces() {
        return raceService.getRaces();
    }

    @PostMapping
    public void saveRace(@RequestBody Race race) {
        raceService.saveRace(race);
    }
}
