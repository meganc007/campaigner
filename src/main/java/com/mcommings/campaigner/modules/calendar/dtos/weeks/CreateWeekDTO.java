package com.mcommings.campaigner.modules.calendar.dtos.weeks;

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
public class CreateWeekDTO {

    private String description;

    @NotNull(message = "Campaign UUID cannot be null or empty")
    private UUID campaignUuid;

    private Integer weekNumber;

    @NotNull(message = "Month cannot be empty")
    private Integer monthId;
}
