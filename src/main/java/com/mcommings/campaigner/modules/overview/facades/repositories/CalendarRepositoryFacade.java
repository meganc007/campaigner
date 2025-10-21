package com.mcommings.campaigner.modules.overview.facades.repositories;

import com.mcommings.campaigner.modules.calendar.entities.*;
import com.mcommings.campaigner.modules.calendar.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CalendarRepositoryFacade {

    private final ISunRepository sunRepository;
    private final IMoonRepository moonRepository;
    private final IMonthRepository monthRepository;
    private final IWeekRepository weekRepository;
    private final IDayRepository dayRepository;
    private final ICelestialEventRepository celestialEventRepository;

    public List<Sun> findSuns(UUID uuid) {
        return sunRepository.findByfk_campaign_uuid(uuid);
    }

    public List<Moon> findMoons(UUID uuid) {
        return moonRepository.findByfk_campaign_uuid(uuid);
    }

    public List<Month> findMonths(UUID uuid) {
        return monthRepository.findByfk_campaign_uuid(uuid);
    }

    public List<Week> findWeeks(UUID uuid) {
        return weekRepository.findByfk_campaign_uuid(uuid);
    }

    public List<Day> findDays(UUID uuid) {
        return dayRepository.findByfk_campaign_uuid(uuid);
    }

    public List<CelestialEvent> findCelestialEvents(UUID uuid) {
        return celestialEventRepository.findByfk_campaign_uuid(uuid);
    }
}
