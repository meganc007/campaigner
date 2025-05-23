package com.mcommings.campaigner.modules.people.controllers;

import com.mcommings.campaigner.modules.people.dtos.RaceDTO;
import com.mcommings.campaigner.modules.people.services.interfaces.IRace;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/races")
public class RaceController {

    private final IRace raceService;

    @GetMapping
    public List<RaceDTO> getRaces() {
        return raceService.getRaces();
    }

    @GetMapping("/{raceId}")
    public RaceDTO getRace(@PathVariable("raceId") int raceId) {
        return raceService.getRace(raceId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping("/exotic/{isExotic}")
    public List<RaceDTO> getRacesByIsExotic(@PathVariable("isExotic") boolean isExotic) {
        return raceService.getRacesByIsExotic(isExotic);
    }

    @PostMapping
    public void saveRace(@Valid @RequestBody RaceDTO race) {
        raceService.saveRace(race);
    }

    @DeleteMapping(path = "{raceId}")
    public void deleteRace(@PathVariable("raceId") int raceId) {
        raceService.deleteRace(raceId);
    }

    @PutMapping(path = "{raceId}")
    public void updateRace(@PathVariable("raceId") int raceId, @RequestBody RaceDTO race) {
        raceService.updateRace(raceId, race);
    }
}
