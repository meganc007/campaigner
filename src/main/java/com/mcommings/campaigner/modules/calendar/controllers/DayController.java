package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.dtos.DayDTO;
import com.mcommings.campaigner.modules.calendar.services.interfaces.IDay;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/days")
public class DayController {

    private final IDay dayService;

    @GetMapping
    public List<DayDTO> getDays() {
        return dayService.getDays();
    }

    @GetMapping(path = "/{dayId}")
    public DayDTO getDay(@PathVariable("dayId") int dayId) {
        return dayService.getDay(dayId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<DayDTO> getDaysByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return dayService.getDaysByCampaignUUID(uuid);
    }

    @GetMapping(path = "/week/{weekId}")
    public List<DayDTO> getDaysByWeek(@PathVariable("weekId") int weekId) {
        return dayService.getDaysByWeek(weekId);
    }

    @PostMapping
    public void saveDay(@Valid @RequestBody DayDTO day) {
        dayService.saveDay(day);
    }

    @DeleteMapping(path = "{dayId}")
    public void deleteDay(@PathVariable("dayId") int dayId) {
        dayService.deleteDay(dayId);
    }

    @PutMapping(path = "{dayId}")
    public void updateDay(@PathVariable("dayId") int dayId, @RequestBody DayDTO day) {
        dayService.updateDay(dayId, day);
    }
}
