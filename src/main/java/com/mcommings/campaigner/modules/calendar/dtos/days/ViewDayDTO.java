package com.mcommings.campaigner.modules.calendar.dtos.days;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewDayDTO {
    private int id;
    private String name;
    private String description;
    private UUID campaignUuid;
    private Integer weekId;
}
