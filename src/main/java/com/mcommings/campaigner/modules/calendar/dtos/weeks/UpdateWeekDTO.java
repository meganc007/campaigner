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
public class UpdateWeekDTO {

    @NotNull
    private int id;

    private String description;

    private UUID campaignUuid;

    private Integer weekNumber;

    private Integer monthId;
}
