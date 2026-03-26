package com.mcommings.campaigner.modules.calendar.dtos.moons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewMoonDTO {
    private int id;
    private String name;
    private String description;
    private UUID campaignUuid;
}
