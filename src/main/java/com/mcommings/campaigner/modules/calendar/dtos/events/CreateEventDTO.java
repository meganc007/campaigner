package com.mcommings.campaigner.modules.calendar.dtos.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEventDTO {

    @NotBlank(message = "Event name cannot be empty")
    private String name;

    private String description;

    @NotNull(message = "Campaign UUID cannot be null or empty")
    private UUID campaignUuid;

    @NotNull(message = "Year cannot be empty")
    private Integer year;

    @NotNull(message = "Month cannot be empty")
    private Integer monthId;

    @NotNull(message = "Week cannot be empty")
    private Integer weekId;

    @NotNull(message = "Day cannot be empty")
    private Integer dayId;

    @NotNull(message = "Continent cannot be empty")
    private Integer continentId;

    @NotNull(message = "Country cannot be empty")
    private Integer countryId;

    private Integer cityId;
}
