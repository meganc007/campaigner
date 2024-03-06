package com.mcommings.campaigner.controllers.calendar;

import com.mcommings.campaigner.models.calendar.Sun;
import com.mcommings.campaigner.services.calendar.SunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/calendar/suns")
public class SunController {
    
    private final SunService sunService;

    @Autowired
    public SunController(SunService sunService) {
        this.sunService = sunService;
    }

    @GetMapping
    public List<Sun> getSuns() {
        return sunService.getSuns();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Sun> getSunsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return sunService.getSunsByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveSun(@RequestBody Sun sun) {
        sunService.saveSun(sun);
    }

    @DeleteMapping(path = "{sunId}")
    public void deleteSun(@PathVariable("sunId") int sunId) {
        sunService.deleteSun(sunId);
    }

    @PutMapping(path = "{sunId}")
    public void updateSun(@PathVariable("sunId") int sunId, @RequestBody Sun sun) {
        sunService.updateSun(sunId, sun);
    }
}
