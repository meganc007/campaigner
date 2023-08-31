package com.mcommings.campaigner.controllers.calendar;

import com.mcommings.campaigner.models.calendar.Week;
import com.mcommings.campaigner.services.calendar.WeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
