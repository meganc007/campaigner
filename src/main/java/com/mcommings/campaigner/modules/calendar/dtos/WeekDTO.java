package com.mcommings.campaigner.modules.calendar.dtos;

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
public class WeekDTO {

    private int id;
    private String description;
    @NotNull
    private UUID fk_campaign_uuid;
    private Integer week_number;
    private Integer fk_month;
}
