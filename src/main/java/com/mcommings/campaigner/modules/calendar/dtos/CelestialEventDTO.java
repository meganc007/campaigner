package com.mcommings.campaigner.modules.calendar.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CelestialEventDTO {

    private int id;
    @NotBlank(message = "Celestial Event name cannot be empty")
    private String name;
    private String description;
    private UUID fk_campaign_uuid;
    private Integer fk_moon;
    private Integer fk_sun;
    private Integer fk_month;
    private Integer fk_week;
    private Integer fk_day;
    private int event_year;
}
