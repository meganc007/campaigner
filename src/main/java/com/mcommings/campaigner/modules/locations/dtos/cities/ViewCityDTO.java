package com.mcommings.campaigner.modules.locations.dtos.cities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewCityDTO {
    private int id;
    private String name;
    private String description;
    private UUID campaignUuid;
    private Integer wealthId;
    private Integer countryId;
    private Integer settlementTypeId;
    private Integer regionId;
    private Integer governmentId;
}
