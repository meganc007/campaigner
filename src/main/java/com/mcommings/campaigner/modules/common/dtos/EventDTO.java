package com.mcommings.campaigner.modules.common.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    private int id;
    @NotBlank(message = "Event name cannot be empty")
    private String name;
    @NotBlank(message = "Event description cannot be empty")
    private String description;
    private int eventYear;
    private Integer fk_month;
    private Integer fk_week;
    private Integer fk_day;
    private Integer fk_city;
    private Integer fk_continent;
    private Integer fk_country;
    @NotNull(message = "Campaign UUID cannot be null or empty.")
    private UUID fk_campaign_uuid;
}
