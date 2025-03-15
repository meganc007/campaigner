package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.dtos.WeekDTO;
import com.mcommings.campaigner.modules.calendar.services.interfaces.IWeek;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/weeks")
public class WeekController {

    private final IWeek weekService;

    @GetMapping
    public List<WeekDTO> getWeeks() {
        return weekService.getWeeks();
    }

    @GetMapping(path = "/{weekId}")
    public WeekDTO getWeek(@PathVariable("weekId") int weekId) {
        return weekService.getWeek(weekId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<WeekDTO> getWeeksByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return weekService.getWeeksByCampaignUUID(uuid);
    }

    @GetMapping(path = "/month/{monthId}")
    public List<WeekDTO> getWeeksByMonth(@PathVariable("monthId") int monthId) {
        return weekService.getWeeksByMonth(monthId);
    }

    @PostMapping
    public void saveWeek(@Valid @RequestBody WeekDTO week) {
        weekService.saveWeek(week);
    }

    @DeleteMapping(path = "{weekId}")
    public void deleteWeek(@PathVariable("weekId") int weekId) {
        weekService.deleteWeek(weekId);
    }

    @PutMapping(path = "{weekId}")
    public void updateWeek(@PathVariable("weekId") int weekId, @RequestBody WeekDTO week) {
        weekService.updateWeek(weekId, week);
    }
}
