package com.mcommings.campaigner.modules.calendar.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewEventDTO {
    private int id;
    private String name;
    private String description;
    private UUID campaignUuid;
    private Integer year;
    private Integer monthId;
    private Integer weekId;
    private Integer dayId;
    private Integer continentId;
    private Integer countryId;
    private Integer cityId;
}
