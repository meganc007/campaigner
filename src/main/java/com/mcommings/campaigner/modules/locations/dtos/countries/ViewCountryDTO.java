package com.mcommings.campaigner.modules.locations.dtos.countries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewCountryDTO {
    private int id;
    private String name;
    private String description;
    private UUID campaignUuid;
    private Integer continentId;
    private Integer governmentId;
}
