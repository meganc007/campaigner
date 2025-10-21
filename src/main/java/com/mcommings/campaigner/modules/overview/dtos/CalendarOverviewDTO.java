package com.mcommings.campaigner.modules.overview.dtos;

import com.mcommings.campaigner.modules.calendar.dtos.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarOverviewDTO {

    private List<SunDTO> suns;
    private List<MoonDTO> moons;
    private List<MonthDTO> months;
    private List<WeekDTO> weeks;
    private List<DayDTO> days;
    private List<CelestialEventDTO> celestialEvents;
}
