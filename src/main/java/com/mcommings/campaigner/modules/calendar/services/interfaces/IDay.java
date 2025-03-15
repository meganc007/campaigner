package com.mcommings.campaigner.modules.calendar.services.interfaces;

import com.mcommings.campaigner.modules.calendar.dtos.DayDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDay {

    List<DayDTO> getDays();

    List<DayDTO> getDaysByCampaignUUID(UUID uuid);

    List<DayDTO> getDaysByWeek(int weekId);

    void saveDay(DayDTO day);

    void deleteDay(int dayId);

    Optional<DayDTO> updateDay(int dayId, DayDTO day);
}
