package com.mcommings.campaigner.modules.overview.services;

import com.mcommings.campaigner.modules.overview.dtos.CalendarOverviewDTO;
import com.mcommings.campaigner.modules.overview.facades.mappers.CalendarMapperFacade;
import com.mcommings.campaigner.modules.overview.facades.repositories.CalendarRepositoryFacade;
import com.mcommings.campaigner.modules.overview.services.interfaces.ICalendarOverview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalendarOverviewService implements ICalendarOverview {

    private final CalendarMapperFacade calendarMapperFacade;
    private final CalendarRepositoryFacade calendarRepositoryFacade;

    @Override
    public CalendarOverviewDTO getCalendarOverview(UUID uuid) {
        var suns = calendarRepositoryFacade.findSuns(uuid)
                .stream().map(calendarMapperFacade::toSunDto).toList();

        var moons = calendarRepositoryFacade.findMoons(uuid)
                .stream().map(calendarMapperFacade::toMoonDto).toList();

        var months = calendarRepositoryFacade.findMonths(uuid)
                .stream().map(calendarMapperFacade::toMonthDto).toList();

        var weeks = calendarRepositoryFacade.findWeeks(uuid)
                .stream().map(calendarMapperFacade::toWeekDto).toList();

        var days = calendarRepositoryFacade.findDays(uuid)
                .stream().map(calendarMapperFacade::toDayDto).toList();

        var celestialEvents = calendarRepositoryFacade.findCelestialEvents(uuid)
                .stream().map(calendarMapperFacade::toCelestialEventDto).toList();

        var events = calendarRepositoryFacade.findEvents(uuid)
                .stream().map(calendarMapperFacade::toEventDto).toList();


        return CalendarOverviewDTO.builder()
                .suns(suns)
                .moons(moons)
                .months(months)
                .weeks(weeks)
                .days(days)
                .celestialEvents(celestialEvents)
                .events(events)
                .build();
    }
}
