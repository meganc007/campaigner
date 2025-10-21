package com.mcommings.campaigner.modules.overview.controllers;

import com.mcommings.campaigner.modules.overview.dtos.CalendarOverviewDTO;
import com.mcommings.campaigner.modules.overview.services.interfaces.ICalendarOverview;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/calendar-overview")
public class CalendarOverviewController {

    private final ICalendarOverview calendarService;

    @GetMapping(path = "/{uuid}")
    public CalendarOverviewDTO getCalendarOverview(@PathVariable("uuid") UUID uuid) {
        return calendarService.getCalendarOverview(uuid);
    }
}
