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
public class DayDTO {

    private int id;
    @NotBlank(message = "Day name cannot be empty")
    private String name;
    private String description;
    private UUID fk_campaign_uuid;
    private Integer fk_week;
}
