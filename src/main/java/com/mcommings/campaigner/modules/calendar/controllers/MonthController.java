package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.dtos.MonthDTO;
import com.mcommings.campaigner.modules.calendar.services.interfaces.IMonth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/calendar/months")
public class MonthController {

    private final IMonth monthService;

    @GetMapping
    public List<MonthDTO> getMonths() {
        return monthService.getMonths();
    }

    @GetMapping(path = "/{monthId}")
    public MonthDTO getMonth(@PathVariable("monthId") int monthId) {
        return monthService.getMonth(monthId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<MonthDTO> getMonthsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return monthService.getMonthsByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveMonth(@Valid @RequestBody MonthDTO month) {
        monthService.saveMonth(month);
    }

    @DeleteMapping(path = "{monthId}")
    public void deleteMonth(@PathVariable("monthId") int monthId) {
        monthService.deleteMonth(monthId);
    }

    @PutMapping(path = "{monthId}")
    public void updateMonth(@PathVariable("monthId") int monthId, @RequestBody MonthDTO month) {
        monthService.updateMonth(monthId, month);
    }
}
