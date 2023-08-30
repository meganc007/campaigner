package com.mcommings.campaigner.controllers.calendar;

import com.mcommings.campaigner.models.calendar.Day;
import com.mcommings.campaigner.services.calendar.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
