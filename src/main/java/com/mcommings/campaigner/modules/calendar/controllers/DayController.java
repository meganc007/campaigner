package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.dtos.days.CreateDayDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.UpdateDayDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.ViewDayDTO;
import com.mcommings.campaigner.modules.calendar.services.DayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/days")
public class DayController {

    private final DayService dayService;

    @GetMapping
    public List<ViewDayDTO> getDays() {

        return dayService.getAll();
    }

    @GetMapping(path = "/{dayId}")
    public ViewDayDTO getDay(@PathVariable int dayId) {
        return dayService.getById(dayId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewDayDTO> getDaysByCampaignUUID(@PathVariable UUID uuid) {
        return dayService.getDaysByCampaignUUID(uuid);
    }

    @GetMapping(path = "/week/{weekId}")
    public List<ViewDayDTO> getDaysByWeek(@PathVariable int weekId) {
        return dayService.getDaysByWeekId(weekId);
    }

    @PostMapping
    public ViewDayDTO createDay(@Valid @RequestBody CreateDayDTO day) {
        return dayService.create(day);
    }

    @PutMapping
    public ViewDayDTO updateDay(@Valid @RequestBody UpdateDayDTO day) {
        return dayService.update(day);
    }

    @DeleteMapping(path = "/{dayId}")
    public void deleteDay(@PathVariable int dayId) {

        dayService.delete(dayId);
    }
}
