package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.entities.Day;
import com.mcommings.campaigner.modules.calendar.services.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/calendar/days")
public class DayController {

    private final DayService dayService;

    @Autowired
    public DayController(DayService dayService) {
        this.dayService = dayService;
    }

    @GetMapping
    public List<Day> getDays() {
        return dayService.getDays();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Day> getDaysByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return dayService.getDaysByCampaignUUID(uuid);
    }

    @GetMapping(path = "/week/{weekId}")
    public List<Day> getDaysByWeek(@PathVariable("weekId") int weekId) {
        return dayService.getDaysByWeek(weekId);
    }

    @PostMapping
    public void saveDay(@RequestBody Day day) {
        dayService.saveDay(day);
    }

    @DeleteMapping(path = "{dayId}")
    public void deleteDay(@PathVariable("dayId") int dayId) {
        dayService.deleteDay(dayId);
    }

    @PutMapping(path = "{dayId}")
    public void updateDay(@PathVariable("dayId") int dayId, @RequestBody Day day) {
        dayService.updateDay(dayId, day);
    }
}
