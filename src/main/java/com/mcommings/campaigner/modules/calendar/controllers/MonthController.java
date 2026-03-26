package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.dtos.months.CreateMonthDTO;
import com.mcommings.campaigner.modules.calendar.dtos.months.UpdateMonthDTO;
import com.mcommings.campaigner.modules.calendar.dtos.months.ViewMonthDTO;
import com.mcommings.campaigner.modules.calendar.services.MonthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/months")
public class MonthController {

    private final MonthService monthService;

    @GetMapping
    public List<ViewMonthDTO> getMonths() {

        return monthService.getAll();
    }

    @GetMapping(path = "/{monthId}")
    public ViewMonthDTO getMonth(@PathVariable int monthId) {
        return monthService.getById(monthId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewMonthDTO> getMonthsByCampaignUUID(@PathVariable UUID uuid) {
        return monthService.getMonthsByCampaignUUID(uuid);
    }

    @PostMapping
    public ViewMonthDTO createMonth(@Valid @RequestBody CreateMonthDTO month) {
        return monthService.create(month);
    }

    @PutMapping
    public ViewMonthDTO updateMonth(@Valid @RequestBody UpdateMonthDTO month) {
        return monthService.update(month);
    }

    @DeleteMapping(path = "/{monthId}")
    public void deleteMonth(@PathVariable int monthId) {

        monthService.delete(monthId);
    }
}
