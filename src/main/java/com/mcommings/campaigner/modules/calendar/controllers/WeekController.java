package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.dtos.weeks.CreateWeekDTO;
import com.mcommings.campaigner.modules.calendar.dtos.weeks.UpdateWeekDTO;
import com.mcommings.campaigner.modules.calendar.dtos.weeks.ViewWeekDTO;
import com.mcommings.campaigner.modules.calendar.services.WeekService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/weeks")
public class WeekController {

    private final WeekService weekService;

    @GetMapping
    public List<ViewWeekDTO> getWeeks() {

        return weekService.getAll();
    }

    @GetMapping(path = "/{weekId}")
    public ViewWeekDTO getWeek(@PathVariable int weekId) {
        return weekService.getById(weekId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewWeekDTO> getWeeksByCampaignUUID(@PathVariable UUID uuid) {
        return weekService.getWeeksByCampaignUUID(uuid);
    }

    @GetMapping(path = "/month/{monthId}")
    public List<ViewWeekDTO> getWeeksByMonthId(
            @PathVariable int monthId) {

        return weekService.getWeeksByMonthId(monthId);
    }

    @PostMapping
    public ViewWeekDTO createWeek(@Valid @RequestBody CreateWeekDTO week) {
        return weekService.create(week);
    }

    @PutMapping
    public ViewWeekDTO updateWeek(@Valid @RequestBody UpdateWeekDTO week) {
        return weekService.update(week);
    }

    @DeleteMapping(path = "/{weekId}")
    public void deleteWeek(@PathVariable int weekId) {

        weekService.delete(weekId);
    }
}
