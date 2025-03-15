package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.entities.Week;
import com.mcommings.campaigner.modules.calendar.services.WeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/calendar/weeks")
public class WeekController {

    private final WeekService weekService;

    @Autowired
    public WeekController(WeekService weekService) {
        this.weekService = weekService;
    }

    @GetMapping
    public List<Week> getWeeks() {
        return weekService.getWeeks();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Week> getWeeksByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return weekService.getWeeksByCampaignUUID(uuid);
    }

    @GetMapping(path = "/month/{monthId}")
    public List<Week> getWeeksByMonth(@PathVariable("monthId") int monthId) {
        return weekService.getWeeksByMonth(monthId);
    }

    @PostMapping
    public void saveWeek(@RequestBody Week week) {
        weekService.saveWeek(week);
    }

    @DeleteMapping(path = "{weekId}")
    public void deleteWeek(@PathVariable("weekId") int weekId) {
        weekService.deleteWeek(weekId);
    }

    @PutMapping(path = "{weekId}")
    public void updateWeek(@PathVariable("weekId") int weekId, @RequestBody Week week) {
        weekService.updateWeek(weekId, week);
    }
}
