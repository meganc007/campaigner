package com.mcommings.campaigner.modules.calendar.dtos.celestial_events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewCelestialEventDTO {
    private int id;
    private String name;
    private String description;
    private UUID campaignUuid;
    private Integer moonId;
    private Integer sunId;
    private Integer monthId;
    private Integer weekId;
    private Integer dayId;
    private Integer year;
}
