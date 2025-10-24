package com.mcommings.campaigner.modules.overview.facades.mappers;

import com.mcommings.campaigner.modules.calendar.dtos.*;
import com.mcommings.campaigner.modules.calendar.entities.*;
import com.mcommings.campaigner.modules.calendar.mappers.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalendarMapperFacade {

    private final SunMapper sunMapper;
    private final MoonMapper moonMapper;
    private final MonthMapper monthMapper;
    private final WeekMapper weekMapper;
    private final DayMapper dayMapper;
    private final CelestialEventMapper celestialEventMapper;
    private final EventMapper eventMapper;

    public SunDTO toSunDto(Sun entity) {
        return sunMapper.mapToSunDto(entity);
    }

    public MoonDTO toMoonDto(Moon entity) {
        return moonMapper.mapToMoonDto(entity);
    }

    public MonthDTO toMonthDto(Month entity) {
        return monthMapper.mapToMonthDto(entity);
    }

    public WeekDTO toWeekDto(Week entity) {
        return weekMapper.mapToWeekDto(entity);
    }

    public DayDTO toDayDto(Day entity) {
        return dayMapper.mapToDayDto(entity);
    }

    public CelestialEventDTO toCelestialEventDto(CelestialEvent entity) {
        return celestialEventMapper.mapToCelestialEventDto(entity);
    }

    public EventDTO toEventDto(Event entity) {
        return eventMapper.mapToEventDto(entity);
    }
}
