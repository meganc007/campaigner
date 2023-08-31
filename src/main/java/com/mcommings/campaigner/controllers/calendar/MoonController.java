package com.mcommings.campaigner.controllers.calendar;

import com.mcommings.campaigner.models.calendar.Moon;
import com.mcommings.campaigner.services.calendar.MoonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/calendar/moons")
public class MoonController {

    private final MoonService moonService;

    @Autowired
    public MoonController(MoonService moonService) {
        this.moonService = moonService;
    }

    @GetMapping
    public List<Moon> getMoons() {
        return moonService.getMoons();
    }

    @PostMapping
    public void saveMoon(@RequestBody Moon moon) {
        moonService.saveMoon(moon);
    }

    @DeleteMapping(path = "{moonId}")
    public void deleteMoon(@PathVariable("moonId") int moonId) {
        moonService.deleteMoon(moonId);
    }

    @PutMapping(path = "{moonId}")
    public void updateMoon(@PathVariable("moonId") int moonId, @RequestBody Moon moon) {
        moonService.updateMoon(moonId, moon);
    }
}
