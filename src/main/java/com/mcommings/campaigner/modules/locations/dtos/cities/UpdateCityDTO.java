package com.mcommings.campaigner.modules.locations.dtos.cities;

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
public class UpdateCityDTO {

    @NotNull
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
